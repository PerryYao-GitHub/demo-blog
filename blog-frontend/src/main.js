import './assets/main.css'

import {createApp, onBeforeMount} from 'vue'
import { createPinia } from 'pinia'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.full.min.mjs'
import 'element-plus/theme-chalk/index.css';

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

onBeforeMount(() => {
  const storedToken = localStorage.getItem("token");
  if (storedToken) {
    userStore.setToken(storedToken);
  }
});

app.mount('#app')
