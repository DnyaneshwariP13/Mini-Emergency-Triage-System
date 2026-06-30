import { CommonModule } from '@angular/common';
import { Component, ElementRef, HostListener } from '@angular/core';
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
  

  constructor(private readonly apiService: ApiService, private router: Router,private elementRef: ElementRef) {}

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

  toggleDropdown(event?: MouseEvent) {
    if (event) {
      event.stopPropagation(); // Prevent bubbling
    }
    this.dropdownOpen = !this.dropdownOpen;
  }

    // LISTEN FOR CLICKS ANYWHERE ON THE PAGE
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    // Get the element that was clicked
    const targetElement = event.target as HTMLElement;

    // Check if the click happened INSIDE this component
    const clickedInside = this.elementRef.nativeElement.contains(targetElement);

    if (!clickedInside) {
      // Click was outside → close the dropdown
      this.dropdownOpen = false;
    }
  }

  handleLogout(): void {
    const isLogout = window.confirm('Are you sure you want to logout?');
    if (isLogout) {
      this.apiService.logout();
      this.router.navigate(['/login']);
    }
  }

    

}