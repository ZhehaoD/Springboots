import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// 引入 Element Plus
import ElementPlus from 'element-plus'
// 引入 Element Plus 样式
import 'element-plus/dist/index.css'
import axios from "axios";

// 配置 axios - 后端运行在8080端口
axios.defaults.withCredentials = true;

// 创建请求实例并配置基础URL（指向后端8080端口）
const request = axios.create({
    // 如果前端与后端端口不同，需要指定完整的后端地址
    baseURL: 'http://localhost:8080',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

// 请求拦截器
request.interceptors.request.use(config => {
    // 可以在这里添加token等信息
    return config
}, error => {
    return Promise.reject(error)
})



const app = createApp(App)

// 定义全局属性
app.config.globalProperties.$request = request

// 使用 Element Plus
app.use(ElementPlus)
app.use(router)
app.mount('#app')
