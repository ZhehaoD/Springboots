<template>
  <div style="height: 100vh; display: flex; align-items: center;justify-content: center;background-color: #42b983">
    <div style="display: flex; background-color: white; width: 65%; border-radius: 5px; overflow: hidden; height: 65%">
      <div style="flex: 1; display: flex; align-items: center; justify-content: center;margin-left: 25px">
        <img src="@/assets/login.png" alt="登录图片">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%; margin-bottom: 20px">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px">欢迎登录后台管理系统</div>
          <el-form-item prop="username">
            <el-input
                size="large"
                placeholder="请输入账号"
                v-model="user.username"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input size="large" show-password placeholder="请输入密码" v-model="user.password">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">登 录</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1">还没有账号？请<span style="color: #40a9ff; cursor: pointer"> 注册</span></div>
            <div style="flex: 1; text-align: right"><span style="color: #40a9ff; cursor: pointer">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { Lock, User} from "@element-plus/icons-vue";

export default {
  name:"LoginPage",
  components: {Lock, User},
  data(){
    return{
      user:{
        username:'',
        password:''
      }
    }
  },
  methods:{
    login(){
      this.$request.post('login', this.user).then(res=>{
        console.log(res.data)
      }).catch(err=>{
        console.error("登录失败：", err)
      })
    }
  }
}
</script>

<style scoped>

</style>