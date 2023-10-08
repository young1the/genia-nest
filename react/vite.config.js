import { resolve } from 'path'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  base: '/react',
  build: {
    rollupOptions: {
      input: {
        ocr: resolve(__dirname, 'ocr.html'),
        paper: resolve(__dirname, 'paper.html'),
      },
    },
    outDir: "../src/main/resources/static/react",
    emptyOutDir: true,
  },
})