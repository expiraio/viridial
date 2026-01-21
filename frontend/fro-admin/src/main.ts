import { createApp } from 'vue'
import App from './App.vue'
import { registerPlugins } from './core/plugins'
import { logger } from './shared/utils/logger'

import './assets/style.css'

const app = createApp(App)

// Register all plugins
registerPlugins(app)

app.mount('#app')

// Initialize authentication after app is mounted
// This will be handled by the App component or a plugin
logger.info('Application initialized', {}, 'main')
