import { resolve } from 'path'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: '/react',
  server: {
    proxy: {
      '/api': { 
        target: 'http://localhost:8080/api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        secure: false,
        ws: true
      }
    }
  },
  build: {
    rollupOptions: {
      input: {
        ocr: resolve(__dirname, 'ocr.html'),
        paper: resolve(__dirname, 'paper.html'),
      },
    },
  },
})