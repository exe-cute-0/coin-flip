import './assets/main.scss'

import { createApp } from 'vue'
import App from './App.vue'

const player = localStorage.getItem('player')
if (player == null) {
    localStorage.setItem('player', prompt('Please enter a player name', 'Anonymous') ?? 'Anonymous')
}

createApp(App).mount('#app')
