import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';



export interface DashboardStats {
  totalPatients: number;
  redPriorityPatients: number;
  waitingPatients: number;
  dischargedPatients: number;
}

export interface PriorityDistribution {
  red: number;
  yellow: number;
  green: number;
  noTriage: number;
} 

@Injectable({
  providedIn: 'root',
})

export class ApiService {

  // private API_URL = "http://18.217.39.141:3030/api" //prod url
  private API_URL = "http://localhost:3030/api"

  constructor(private http: HttpClient) { }


  saveToken(token: string): void {
    localStorage.setItem("token", token);
  }

  getToken(): string | null {
    return localStorage.getItem("token");
  }

  isAthenticated(): boolean {
    return !!localStorage.getItem("token");
  }

  logout(): void {
    localStorage.removeItem("token");
  }

  private getHeader(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    });
  }

 

  //REGISTER USER
  registerUser(body: any): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/register`, body);
  }

  loginUser(body: any): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/login`, body);
  }

   getCurrentUser(): string {
  const user = localStorage.getItem('user');
  if (user) {
    return JSON.parse(user).username || 'User';
  }
  return 'User';
}

   // ---------- 1. Register a new patient ----------
  // POST /api/patients
  addPatient(body: any): Observable<any> {
    return this.http.post(`${this.API_URL}/patients`, body, {
      headers: this.getHeader()
    });
  }
   
    // ---------- 2. Get all patients (patient list screen) ----------
  // GET /api/patients
  getAllPatients(): Observable<any> {
    return this.http.get(`${this.API_URL}/patients`, {
      headers: this.getHeader()
    });
  }

  // ---------- 3. Get patient by ID ----------
  // GET /api/patients/{id}
  getPatientById(patientId: any): Observable<any> {
    return this.http.get(`${this.API_URL}/patients/${patientId}`, {
      headers: this.getHeader()
    });
  }

     // ---------- 4. Update patient details ----------
  // PUT /api/patients
  updatePatient(id: number,body: any): Observable<any> {
    return this.http.put(`${this.API_URL}/patients/${id}`, body, {
      headers: this.getHeader()
    });
  }

    // ---------- 5. Search patients by name ----------
  // GET /api/patients/search?name=John
  searchPatientsByName(name: string): Observable<any> {
    const params = new HttpParams().set('name', name);
    return this.http.get(`${this.API_URL}/patients/search`, {
      headers: this.getHeader(),
      params: params
    });
  }

   // ---------- 6. Filter patients by priority ----------
  // GET /api/patients/filter?priority=RED
  filterPatientsByPriority(priority: string): Observable<any> {
    const params = new HttpParams().set('priority', priority);
    return this.http.get(`${this.API_URL}/patients/filter`, {
      headers: this.getHeader(),
      params: params
    });
  }

  // ---------- 7. Update patient status ----------
  // PATCH /api/patients/{id}/status?status=UNDER_TREATMENT
  updatePatientStatus(patientId: string, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.patch(`${this.API_URL}/patients/${patientId}/status`, null, {
      headers: this.getHeader(),
      params: params
    });
  }

    // ---------- 8. Add triage assessment for a patient ----------
  // POST /api/patients/{patientId}/triage
  addTriageForPatient(patientId: number, body: any): Observable<any> {
    return this.http.post(`${this.API_URL}/patients/${patientId}/triage`, body, {
      headers: this.getHeader()
    });
  }

  // ---------- 9. Get triage assessment for a patient ----------
  // GET /api/patients/{patientId}/triage
  getTriageForPatient(patientId: string): Observable<any> {
    return this.http.get(`${this.API_URL}/patients/${patientId}/triage`, {
      headers: this.getHeader()
    });
  }
//------------------------SUMMARY DASHBOARD API CALLS--------------------
   // ---------- 1. Get dashboard statistics ----------
  // GET /api/dashboard/stats
  getDashboardStats(): Observable<any> {
    return this.http.get(`${this.API_URL}/dashboard/stats`, {
      headers: this.getHeader()
    });
  }

  // ---------- 2. Get priority distribution for chart ----------
  // GET /api/dashboard/priority-distribution
  getPriorityDistribution(): Observable<any> {
    return this.http.get(`${this.API_URL}/dashboard/priority-distribution`, {
      headers: this.getHeader()
    });
  }



}
