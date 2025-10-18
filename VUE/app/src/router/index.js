import { createRouter, createWebHistory } from 'vue-router'
import Manager from '../views/Manager.vue'
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import error from "@/views/404.vue";

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Manager',
            component: Manager,
            redirect:'home',
            children:[
                { path:'home', meta:{name:'系统首页'},name:'HomeView', component:()=>import('../views/manager/HomeView.vue')},
                { path:'user', meta:{name:'用户信息'},name:'User', component:()=>import('../views/manager/User.vue')},
                { path:'403', meta:{name:'无权限'}, name:'Auth', component:()=>import('../views/manager/Auth.vue')},
                { path:'person', meta:{name:'个人信息'}, name:'Person', component:()=>import('../views/manager/Person.vue')},
                { path:'password', meta:{name:'修改密码'}, name:'Password', component:()=>import('../views/manager/Password.vue')},
                { path:'News', meta:{name:'新闻信息'}, name:'News', component:()=>import('../views/manager/News')},
                { path:'notice', meta:{name:'系统公告'}, name:'Notice', component:()=>import('../views/manager/Notice')}
            ]
        },
        {
            path:'/login',
            name:'Login',
            component: Login,
            meta:{name:'登陆'},
        },
        {
            path:'/register',
            name:'Register',
            component: Register,
            meta:{name:'注册'},

        },
        {
            path:'/:pathMatch(.*)*',
            name:'404',
            component: error,
            meta:{name:'无法访问'},

        }
    ]
})
router.beforeEach((to, from, next) => {
    const adminPath = ['/user']  // 管理员才能访问的路径
    const userStr = localStorage.getItem('honey-user')
    const user = userStr ? JSON.parse(userStr) : null  // 更严谨的解析方式

    // 1. 未登录状态（没有用户信息）
    if (!user) {
        // 如果要去的不是登录页，就重定向到登录页
        if (to.path !== '/login' && to.path !== '/register') {
            next('/login')
            return  // 终止后续逻辑
        }
    }
    // 2. 已登录状态但权限不足
    else if (user.data?.role !== '管理员' && adminPath.includes(to.path)) {
        next('/403')  // 跳转到无权限页面
        return  // 终止后续逻辑
    }

    // 3. 正常情况，继续访问
    next()
})


export default router
