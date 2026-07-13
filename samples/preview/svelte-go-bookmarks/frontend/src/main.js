// Bootstrap browser OpenTelemetry FIRST so the global tracer is installed
// before any fetch() runs from a Svelte component.
import './otel.js';
import './app.css';
import { mount } from 'svelte';
import App from './App.svelte';

const app = mount(App, {
  target: document.getElementById('app'),
});

export default app;
