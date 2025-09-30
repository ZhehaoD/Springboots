import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 30000
})

// request 拦截器
// 可以在请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(
    config => {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        // 从localStorage获取用户信息并设置token请求头
        const userStr = localStorage.getItem("user");
        if (userStr) {
            const user = JSON.parse(userStr);
            config.headers['token'] = user.token; // 设置请求头
        }
        return config;
    },
    error => {
        console.error('request error: ' + error); // for debug
        return Promise.reject(error);
    }
);

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data; // 修正变量名不一致问题

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        return res;
    },
    error => {
        console.error('response error: ' + error); // for debug
        return Promise.reject(error);
    }
);

export default request
