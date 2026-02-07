import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  build: {
      rollupOptions: {
          external: [
              /\.test\.(ts|tsx|js|jsx)$/
          ],
      }
  },
  plugins: [
      react(),
      tailwindcss(),
  ],
})
