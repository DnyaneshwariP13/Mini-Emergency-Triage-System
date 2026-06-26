import { Component, OnInit, AfterViewInit, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import {  DashboardStats, PriorityDistribution, ApiService } from '../service/api';
import { HttpErrorResponse } from '@angular/common/http';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit, AfterViewInit {
  @ViewChild('chartCanvas', { static: false }) chartCanvas!: ElementRef<HTMLCanvasElement>;
  
  stats: DashboardStats = { totalPatients: 0, redPriorityPatients: 0, waitingPatients: 0, dischargedPatients: 0 };
  distribution: PriorityDistribution = { red: 0, yellow: 0, green: 0, noTriage: 0 };
  isLoading = true;
  error = '';
  chartInstance: Chart | null = null;
  dataLoaded = false;
  chartRendered = false;

  constructor(
    private dashboardService: ApiService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  ngAfterViewInit(): void {
    // If data is already loaded, render the chart
    if (this.dataLoaded && !this.chartRendered) {
      this.renderChart();
    }
  }

  loadStats(): void {
    this.isLoading = true;
    this.dashboardService.getDashboardStats().subscribe({
      next: (response) => {
        this.stats = response.data;
        this.loadPriorityDistribution();
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.error?.message || err.message || 'Failed to load stats.';
        this.isLoading = false;
      }
    });
  }

  loadPriorityDistribution(): void {
    this.dashboardService.getPriorityDistribution().subscribe({
      next: (response) => {
        this.distribution = response.data;
        this.isLoading = false;
        this.dataLoaded = true;
        // Force change detection so the *ngIf updates the DOM
        this.cdr.detectChanges();
        // Wait a bit for the DOM to render the canvas
        setTimeout(() => {
          this.renderChart();
        }, 100);
      },
      error: (err: HttpErrorResponse) => {
        this.error = err.error?.message || err.message || 'Failed to load distribution.';
        this.isLoading = false;
      }
    });
  }

  renderChart(): void {
    // Prevent multiple renders
    if (this.chartRendered) return;
    // Check if canvas exists
    if (!this.chartCanvas || !this.chartCanvas.nativeElement) {
      console.warn('Chart canvas not available yet. Retrying...');
      // Retry after a short delay
      setTimeout(() => this.renderChart(), 200);
      return;
    }
    // If an existing chart exists, destroy it
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }

    const ctx = this.chartCanvas.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Could not get 2D context for canvas.');
      return;
    }

    this.chartInstance = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['🔴 Red (Critical)', '🟡 Yellow (Urgent)', '🟢 Green (Stable)', '⚪ No Triage'],
        datasets: [{
          data: [
            this.distribution.red,
            this.distribution.yellow,
            this.distribution.green,
            this.distribution.noTriage
          ],
          backgroundColor: ['#d32f2f', '#fbc02d', '#388e3c', '#e0e0e0'],
          borderColor: '#ffffff',
          borderWidth: 2,
          hoverOffset: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              usePointStyle: true,
              padding: 20,
              font: { size: 14 }
            }
          },
          tooltip: {
            callbacks: {
              label: function(context) {
                const value = context.parsed || 0;
                const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                const percentage = total > 0 ? (value / total * 100).toFixed(1) : 0;
                return `${context.label}: ${value} (${percentage}%)`;
              }
            }
          }
        },
        cutout: '65%'
      }
    });
    this.chartRendered = true;
  }
}