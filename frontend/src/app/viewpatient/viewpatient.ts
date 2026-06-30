import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../service/api';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-view-patient',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './viewpatient.html',
  styleUrls: ['./viewpatient.css']
})
export class Viewpatient implements OnInit {
  patient: any = null;
  error = '';
  isLoading = true;
  patientId!: number;
  private routeSub!: Subscription; 

  constructor(
    private patientService: ApiService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef // <-- Inject here
  ) {}
/*
  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.patientId = +idParam;
      if (isNaN(this.patientId) || this.patientId <= 0) {
        this.error = 'Invalid patient ID.';
        this.isLoading = false;
        return;
      }
      this.loadPatient();
    } else {
      this.error = 'Patient ID missing.';
      this.isLoading = false;
    }
  }*/
    ngOnInit(): void {
      
    // ✅ Subscribe to route parameter changes
    this.routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.patientId = +id;
        if (isNaN(this.patientId) || this.patientId <= 0) {
          this.error = 'Invalid patient ID.';
          this.isLoading = false;
          return;
        }
        this.loadPatient();   // Reload patient for this ID
      } else {
        this.error = 'Patient ID missing.';
        this.isLoading = false;
      }
    });
  }


  loadPatient(): void {
    this.isLoading = true;
        this.error = '';
    this.patient = null;

    this.patientService.getPatientById(this.patientId).subscribe({
      next: (response) => {
        console.log('🔍 Patient response:', response);
        this.patient = response.data || response;
        this.isLoading = false;

        // FORCE UI UPDATE
      this.cdr.detectChanges(); 
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.error?.message || err.message || 'Failed to load patient.';
        this.isLoading = false;
        // FORCE UI UPDATE (to hide spinner and show error)
      this.cdr.detectChanges();
      }
    });
  }

  goToTriage(): void {
    this.router.navigate(['/patients', this.patientId, 'triage']);
  }

  goBack(): void {
    this.router.navigate(['/patients']);
  }

    ngOnDestroy(): void {
    // ✅ Prevent memory leaks
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
}
}