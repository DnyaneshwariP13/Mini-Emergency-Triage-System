/*import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../service/api';

@Component({
  selector: 'app-triage-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './triageform.html',
  styleUrls: ['./triageform.css']
})
export class Triageform implements OnInit {
  triageForm!: FormGroup;
  patientId!: number;
  error = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private patientService: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  
  ngOnInit(): void {
    // Get patientId from route parameter
    this.route.params.subscribe(params => {
      this.patientId = +params['patientId'];
      if (!this.patientId) {
        this.error = 'Patient ID is missing.';
        return;
      }
    });

    

    this.triageForm = this.fb.group({
      priority: ['', Validators.required],
      bloodPressure: ['', [Validators.required, Validators.pattern(/^\d{2,3}\/\d{2,3}$/)]], // e.g., 120/80
      pulseRate: ['', [Validators.required, Validators.min(30), Validators.max(200)]],
      temperature: ['', [Validators.required, Validators.min(95), Validators.max(105)]]
    });


  onSubmit(): void {
   if (this.triageForm.invalid) {
      this.error = 'Please fill all required fields correctly.';
      return;
    }

     if (!this.patientId || isNaN(this.patientId)) {
      this.error = 'Patient ID is invalid. Please go back.';
      return;
    }

    this.isLoading = true;
    const formValue = this.triageForm.value;

    this.patientService.addTriageForPatient(this.patientId, formValue).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/patients', this.patientId]); // or back to list
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.error = err.error?.message || err.message || 'Failed to add triage assessment.';
      }
    });
  }
}*/
/*
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../service/api';

@Component({
  selector: 'app-triage-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './triageform.html',
  styleUrls: ['./triageform.css']
})
export class Triageform implements OnInit {
  triageForm!: FormGroup;
  patientId!: number;
  error = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private patientService: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // 🔧 1. Always create the form first
    this.triageForm = this.fb.group({
      priority: ['', Validators.required],
      bloodPressure: ['', [Validators.required, Validators.pattern(/^\d{2,3}\/\d{2,3}$/)]],
      pulseRate: ['', [Validators.required, Validators.min(30), Validators.max(200)]],
      temperature: ['', [Validators.required, Validators.min(95), Validators.max(105)]]
    });

    // 🔧 2. Then extract the patient ID
    const idParam = this.route.snapshot.paramMap.get('patientId');
    if (idParam) {
      this.patientId = +idParam;
      if (isNaN(this.patientId) || this.patientId <= 0) {
        this.error = 'Invalid patient ID.';
        this.patientId = 0;
      }
    } else {
      this.error = 'Patient ID is missing. Please go back and try again.';
      this.patientId = 0;
    }

    // Optionally, if you want to disable the form when ID is invalid
    if (this.patientId <= 0) {
      this.triageForm.disable();
    } else {
      this.triageForm.enable();
    }
  }

  onSubmit(): void {
    // Guard: if patientId is invalid, show error
    if (!this.patientId || isNaN(this.patientId)) {
      this.error = 'Patient ID is invalid. Please go back.';
      return;
    }

    if (this.triageForm.invalid) {
      this.error = 'Please fill all required fields correctly.';
      return;
    }

    this.isLoading = true;
    const formValue = this.triageForm.value;

    this.patientService.addTriageForPatient(this.patientId, formValue).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/patients']);
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.error = err.error?.message || err.message || 'Failed to add triage.';
      }
    });
  }
}*/
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiService } from '../service/api';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-triage-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './triageform.html',
  styleUrls: ['./triageform.css']
})
export class Triageform implements OnInit, OnDestroy {
  triageForm!: FormGroup;
  patientId!: number;
  error = '';
  isLoading = false;
  private routeSub!: Subscription;

  constructor(
    private fb: FormBuilder,
    private patientService: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
ngOnInit(): void {
  // ✅ Always create the form first
  this.triageForm = this.fb.group({
    priority: ['', Validators.required],
    bloodPressure: ['', [Validators.required, Validators.pattern(/^\d{2,3}\/\d{2,3}$/)]],
    pulseRate: ['', [Validators.required, Validators.min(30), Validators.max(200)]],
    temperature: ['', [Validators.required, Validators.min(95), Validators.max(105)]]
  });

  // Then process the ID
  const idParam = this.route.snapshot.paramMap.get('id');
  if (idParam) {
    this.patientId = +idParam;
    if (isNaN(this.patientId) || this.patientId <= 0) {
      this.error = 'Invalid patient ID.';
      this.triageForm.disable();
    } else {
      this.triageForm.enable();
    }
  } else {
    this.error = 'Patient ID is missing.';
    this.triageForm.disable();
  }
}



  onSubmit(): void {
    if (!this.patientId || isNaN(this.patientId)) {
      this.error = 'Patient ID is invalid. Please go back.';
      return;
    }
    if (this.triageForm.invalid) {
      this.error = 'Please fill all required fields correctly.';
      return;
    }

    this.isLoading = true;
    const formValue = this.triageForm.value;

    this.patientService.addTriageForPatient(this.patientId, formValue).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/patients', this.patientId]); // go to detail page
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.error = err.error?.message || err.message || 'Failed to add triage.';
      }
    });
  }

  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }
}