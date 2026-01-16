import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'

const isTest = process.env.VITEST === 'true' || process.env.NODE_ENV === 'test'

export default defineConfig({
    plugins: [
        vue(),
        // Devtools/Inspect aus im Test-Modus
        !isTest && VueDevTools(),
    ].filter(Boolean),
})
