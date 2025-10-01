import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";

const router = createRouter({
    // Vue CLI 创建的 Vue 3 项目使用 process.env
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path:'/login',
            name:'Login',
            component: Login
        },
        {
            path:'/register',
            name:'Register',
            component: Register
        }
    ]
})

export default router
