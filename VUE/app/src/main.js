import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// 引入 Element Plus
import ElementPlus from 'element-plus'
// 引入 Element Plus 样式
import 'element-plus/dist/index.css'
import axios from "axios";
axios.defaults.withCredentials = true;
const app = createApp(App)

// 使用 Element Plus
app.use(ElementPlus)
app.use(router)
app.mount('#app')
