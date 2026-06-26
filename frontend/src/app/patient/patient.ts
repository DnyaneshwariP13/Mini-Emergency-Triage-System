import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../service/api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './patient.html',
  styleUrls: ['./patient.css']
})
export class Patient implements OnInit {
  // Array to store all patients
  patients: any[] = [];
  
  // Array to store filtered patients for display
  filteredPatients: any[] = [];
  
  // Variable to store error messages
  error: string = '';
  
  // Current priority filter setting
  priorityFilter: string = 'ALL';
  
  // Search term for patient name
  searchTerm: string = '';

  // Loading state
  isLoading: boolean = false;
  paginatedPatients: any[] = [];

    // Pagination properties
  currentPage: number = 1;
  pageSize: number = 10;
  totalPages: number = 0;

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    // Redirect to login if user is not authenticated
    if (!this.apiService.isAthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    
    // Fetch patients when component initializes
    this.fetchPatients();
  }

  /**
   * Fetches all patients 
   */
  fetchPatients(): void {
    this.isLoading = true;
    this.error = '';
    
    this.apiService.getAllPatients().subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.statusCode === 200) {
          // Store patients and initialize filtered list
          this.patients = res.data || [];
          //this.filteredPatients = [...this.patients];
          this.applyFilters(); // will also apply pagination
        } else {
          this.error = res.message || 'Failed to fetch patients';
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.error = error.error?.message || error.message || 'Error fetching patients';
        console.error('Error fetching patients:', error);
      }
    });
  }

  /**
   * Applies the current filters to the patient list
   * Combines search by name and filter by priority
   */
  applyFilters(): void {
    // 1. Start with all patients
    let result = [...this.patients];

    // 2. Filter by search term (local)
    if (this.searchTerm && this.searchTerm.trim() !== '') {
      const term = this.searchTerm.trim().toLowerCase();
      result = result.filter(p => p.patientName.toLowerCase().includes(term));
    }

    // 3. Filter by priority (local)
    if (this.priorityFilter !== 'ALL') {
      result = result.filter(p => p.triageAssessment?.priority === this.priorityFilter);
    }

    this.filteredPatients = result;
    this.currentPage = 1; // Reset to first page when filters change
    this.updatePagination();
  }

  updatePagination(): void {
    // Calculate total pages
    this.totalPages = Math.ceil(this.filteredPatients.length / this.pageSize);
    if (this.totalPages === 0) this.totalPages = 1;

    // Ensure current page is within bounds
    if (this.currentPage > this.totalPages) {
      this.currentPage = this.totalPages;
    }
    if (this.currentPage < 1) {
      this.currentPage = 1;
    }

    // Slice the filtered list for the current page
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedPatients = this.filteredPatients.slice(startIndex, endIndex);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updatePagination();
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }



  /*
  applyFilters(): void {
    // Start with all patients
    let result = [...this.patients];

    // First filter by search term (patient name)
    if (this.searchTerm && this.searchTerm.trim() !== '') {
      this.apiService.searchPatientsByName(this.searchTerm.trim()).subscribe({
        next: (res) => {
          if (res.statusCode === 200) {
            let searchResults = res.data || [];
            
            // Then filter by priority if not 'ALL'
            if (this.priorityFilter !== 'ALL') {
              this.applyPriorityFilter(searchResults);
            } else {
              this.filteredPatients = searchResults;
            }
          }
        },
        error: (error) => {
          this.error = error.error?.message || error.message || 'Error searching patients';
        }
      });
    } else if (this.priorityFilter !== 'ALL') {
      // Only filter by priority if search term is empty
      this.applyPriorityFilter(result);
    } else {
      // No filters applied - show all patients
      this.filteredPatients = result;
    }
  }

  /**
   * Helper method to apply priority filter
   * @param currentResult The current filtered patient list
   */
  private applyPriorityFilter(currentResult: any[]): void {
    this.apiService.filterPatientsByPriority(this.priorityFilter).subscribe({
      next: (res) => {
        if (res.statusCode === 200) {
          const priorityPatients = res.data || [];
          
          if (this.searchTerm && this.searchTerm.trim() !== '') {
            // Combine both filters by finding intersection
            this.filteredPatients = currentResult.filter(patient => 
              priorityPatients.some((pp: any) => pp.patientId === patient.patientId)
            );
          } else {
            this.filteredPatients = priorityPatients;
          }
        }
      },
      error: (error) => {
        this.error = error.error?.message || error.message || 'Error applying priority filter';
      }
    });
  }

  /**
   * Updates the status of a patient
   * @param patient The patient to update
   * @param status The new status (WAITING, UNDER_TREATMENT, DISCHARGED)
   */
 updatePatientStatus(patient: any, event: Event): void {
  if (!patient || !patient.patientId) return;

  const select = event.target as HTMLSelectElement;
  const status = select?.value;
  if (!status) return;

  this.isLoading = true;
  this.apiService.updatePatientStatus(patient.patientId, status).subscribe({
    next: (res) => {
      this.isLoading = false;
      if (res.statusCode === 200) {
        const updated = res.data;
        this.patients = this.patients.map(p =>
          p.patientId === updated.patientId ? updated : p
        );
        this.applyFilters();
      } else {
        this.error = res.message || 'Failed to update status';
      }
    },
    error: (err) => {
      this.isLoading = false;
      this.error = err.error?.message || err.message || 'Error updating status';
    }
  });
}

  /**
   * Navigate to patient details page
   * @param patientId The patient ID
   */
  viewPatient(patientId: number): void {
    this.router.navigate(['/patients', patientId]);
  }

  /**
   * Navigate to edit patient page
   * @param patientId The patient ID
   */
  editPatient(patientId: string): void {
    this.router.navigate(['/patients/update', patientId]);
  }

  /**
   * Navigate to add triage page for a patient
   * @param patientId The patient ID
   */
addTriage(patientId: number): void {
  this.router.navigate(['/patients', patientId, 'triage']);
}



  /**
   * Resets all filters to their default values
   */
  resetFilters(): void {
    this.priorityFilter = 'ALL';
    this.searchTerm = '';
    this.applyFilters();
  }

  /**
   * Get priority badge class based on priority
   */
  getPriorityClass(priority: string): string {
    if (!priority) return 'priority-none';
    switch (priority.toUpperCase()) {
      case 'RED': return 'priority-red';
      case 'YELLOW': return 'priority-yellow';
      case 'GREEN': return 'priority-green';
      default: return 'priority-none';
    }
  }

  /**
   * Get status badge class based on status
   */
  getStatusClass(status: string): string {
    if (!status) return 'status-waiting';
    switch (status.toUpperCase()) {
      case 'WAITING': return 'status-waiting';
      case 'UNDER_TREATMENT': return 'status-treatment';
      case 'DISCHARGED': return 'status-discharged';
      default: return 'status-waiting';
    }
  }

  /**
   * Get status display name
   */
  getStatusDisplay(status: string): string {
    if (!status) return 'Waiting';
    switch (status.toUpperCase()) {
      case 'WAITING': return 'Waiting';
      case 'UNDER_TREATMENT': return 'Under Treatment';
      case 'DISCHARGED': return 'Discharged';
      default: return status;
    }
  }

  /**
   * Get priority display name
   */
  getPriorityDisplay(priority: string): string {
    if (!priority) return 'N/A';
    switch (priority.toUpperCase()) {
      case 'RED': return 'Critical';
      case 'YELLOW': return 'Urgent';
      case 'GREEN': return 'Stable';
      default: return priority;
    }
  }
}