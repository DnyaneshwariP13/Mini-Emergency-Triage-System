import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../service/api';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  dropdownOpen = false;   
  

  constructor(private readonly apiService: ApiService, private router: Router) {}

  get isAuthenticated(): boolean {
    return this.apiService.isAthenticated();  
  }

  
  getCurrentUser(): string {
    return this.apiService.getCurrentUser();   // synchronous, reads from localStorage
  }

  getUserInitials(): string {
    const name = this.getCurrentUser();
    if (!name) return 'U';
    const parts = name.trim().split(' ');
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  handleLogout(): void {
    const isLogout = window.confirm('Are you sure you want to logout?');
    if (isLogout) {
      this.apiService.logout();
      this.router.navigate(['/login']);
    }
  }

    
}