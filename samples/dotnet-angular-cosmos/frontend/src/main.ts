// OpenTelemetry browser SDK must initialize BEFORE Angular bootstraps so that
// DocumentLoad spans cover the first tick and Zone.js patching is in place
// before any Angular HttpClient/fetch/XHR call is issued.
import './otel';
import { provideZoneChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [
    provideZoneChangeDetection(),
    provideHttpClient()
  ]
}).catch(err => console.error(err));
