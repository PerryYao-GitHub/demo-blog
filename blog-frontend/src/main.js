import './assets/main.css'

import {createApp, onBeforeMount} from 'vue'
import { createPinia } from 'pinia'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.full.min.mjs'
import 'element-plus/theme-chalk/index.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

import App from './App.vue'
import router from './router'
import {Calendar, Edit, Key, Message, Phone, User, Watch} from "@element-plus/icons-vue";

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

app.component('EditIcon', Edit);
app.component('KeyIcon', Key);
app.component('UserIcon', User);
app.component('PhoneIcon', Phone);
app.component('MessageIcon', Message);
app.component('CalendarIcon', Calendar);
app.component('WatchIcon', Watch);

app.mount('#app')
