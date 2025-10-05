<template>
  <div style="height: 100vh; display: flex; align-items: center;justify-content: center;background-color: cornflowerblue">
    <div style="display: flex; background-color: white; width: 65%; border-radius: 5px; overflow: hidden; height: 65%">
      <div style="flex: 1; display: flex; align-items: center; justify-content: center;margin-left: 25px">
        <img src="@/assets/login.png" alt="登录图片">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%; margin-bottom: 20px" :rules="rules" ref="registerRef">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px">欢迎后台管理系统</div>
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
          <el-form-item prop="confirmPass">
            <el-input size="large" show-password placeholder="请确认密码" v-model="user.confirmPass">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="role">
            <el-radio-group v-model="user.role">
              <el-radio label="用户"></el-radio>
              <el-radio label="管理员"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="register">注 册</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1" @click="$router.push('/login')">已经有账号了？请<span style="color: #40a9ff; cursor: pointer"> 登陆</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { Lock, User} from "@element-plus/icons-vue";
import router from "@/router";

export default {
  name:"RegisterPage",
  components: {Lock, User},
  data(){
    const validatePassword = (rule, confirmPass, callback) =>{
      if (confirmPass === ''){
        callback(new Error('请再次输入密码'))
      }else if (confirmPass !== this.user.password){
        callback(new Error('两次密码不一样'))
      }else{
        callback()
      }
    }
    return{
      user:{
        username:'',
        password:'',
        confirmPass:''
      },
      rules:{
        username:[  // 这里应该和prop属性值保持一致
          { required:true, message:'请输入账号', trigger:'blur' },
        ],
        password:[
          { required:true, message:'请输入密码', trigger:'blur' },
        ],
        confirmPass:[
          {validator: validatePassword, trigger:'blur'}
        ],
        role:[
          { required:true, message:'请选择角色', trigger:'blur' },

        ]
      }
    }
  },
  methods:{
    router() {
      return router
    },
    register(){
      this.$refs["registerRef"].validate((valid) => {
        if(valid){
          this.$request.post('register', this.user).then(res=>{
            if(res.data.code ==='200'){
              this.$router.push('/login')
              this.$message.success('注册成功')
            }else{
              this.$message.error(res.data.msg)
            }
          })
        }
      })

    }
  }
}
</script>

<style scoped>

</style>