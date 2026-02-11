import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

const apiUrl = process.env.services__api__https__0 || process.env.services__api__http__0 || 'http://localhost:8000';

export default defineConfig({
  plugins: [react()],
  server: {
    port: parseInt(process.env.PORT || '5173'),
    proxy: {
      '/api': {
        target: apiUrl,
        changeOrigin: true,
      }
    }
  }
})
