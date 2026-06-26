import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';   // ⬅️ Import this
import { Patient } from '../patient/patient';
import { ApiService } from '../service/api';
import { CommonModule } from '@angular/common';    


@Component({
  selector: 'app-patient-form',
  standalone: true, 
  imports: [
    CommonModule,          // ⬅️ Provides *ngIf, ngClass, ngStyle, etc.[reference:3][reference:4]
    ReactiveFormsModule,   // ⬅️ Provides formGroup, formControlName, etc.[reference:5]
    RouterLink             // ⬅️ If you use routerLink in the template
  ],
  templateUrl: './patientform.html',
  styleUrls: ['./patientform.css']
})
export class Patientform implements OnInit {
  patientForm!: FormGroup;
  isEdit = false;
  patientId?: number;
  error = '';

  constructor(
    private fb: FormBuilder,
    private patientService: ApiService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      patientName: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(1)]],
      gender: ['', Validators.required],
      mobileNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      chiefComplaint: [''],
      arrivalDateTime: ['', Validators.required],
      status: ['WAITING']
    });

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEdit = true;
        this.patientId = +params['id'];
        this.loadPatient(this.patientId);
      }
    });
  }

loadPatient(id: number): void {
  this.patientService.getPatientById(id).subscribe({
    next: (patient) => {
      const arrival = patient.arrivalDateTime
        ? new Date(patient.arrivalDateTime).toISOString().slice(0, 16)
        : '';
      this.patientForm.patchValue({
        patientName: patient.patientName,
        age: patient.age,
        gender: patient.gender,
        mobileNumber: patient.mobileNumber,
        chiefComplaint: patient.chiefComplaint || '',
        arrivalDateTime: arrival,
        status: patient.status || 'WAITING'
      });
    },
    error: (err) => this.error = err.message
  });
}

  onSubmit(): void {
    if (this.patientForm.invalid) {
      this.error = 'Please fill in all required fields correctly.';
      return;
    }

    const formValue = this.patientForm.value;

    if (this.isEdit && this.patientId) {
      this.patientService.updatePatient(this.patientId, formValue).subscribe({
        next: () => this.router.navigate(['/patients']),
        error: (err: HttpErrorResponse) => {   // ✅ Typed
          this.error = err.error?.message || err.message || 'Update failed';
        }
      });
    } else {
      this.patientService.addPatient(formValue).subscribe({
        next: () => this.router.navigate(['/patients']),
        error: (err: HttpErrorResponse) => {   // ✅ Typed
          this.error = err.error?.message || err.message || 'Registration failed';
        }
      });
    }
  }



    }
  