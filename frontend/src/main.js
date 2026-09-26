import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import './fontawesome'

const app = createApp(App)
app.component('FontAwesomeIcon', FontAwesomeIcon)
app.use(router)
app.mount('#app')