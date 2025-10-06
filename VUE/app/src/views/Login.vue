<template>
  <div style="height: 100vh; display: flex; align-items: center;justify-content: center;background-color: #42b983">
    <div style="display: flex; background-color: white; width: 65%; border-radius: 5px; overflow: hidden; height: 65%">
      <div style="flex: 1; display: flex; align-items: center; justify-content: center;margin-left: 25px">
        <img src="@/assets/login.png" alt="登录图片">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%; margin-bottom: 20px" :rules="rules" ref="loginRef">
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
            <div style="flex: 1">还没有账号？请<span style="color: #40a9ff; cursor: pointer" @click="$router.push('/register')"> 注册</span></div>
            <div style="flex: 1; text-align: right"><span style="color: #40a9ff; cursor: pointer" @click="handleForgetPass">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>
    <el-dialog v-model="forgetPassDialogVis" title="忘记密码" width="30%">
      <el-form :model="forgetUserForm" label-width="80px" style="padding-right: 20px">
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="forgetUserForm.username" autocomplete="off" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth">
          <el-input v-model="forgetUserForm.phone" autocomplete="off" placeholder="请输入手机号"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="forgetPassDialogVis = false">取消</el-button>
          <el-button type="primary" @click="resetPassword">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Lock, User} from "@element-plus/icons-vue";
import request from "@/utils/request"; // 导入封装的request

export default {
  name:"LoginPage",
  components: {Lock, User},
  data(){
    return{
      user:{
        username:'',
        password:''
      },
      forgetUserForm:{}, // 移到data中
      forgetPassDialogVis:false, // 移到data中
      formLabelWidth: '80px', // 定义表单标签宽度
      rules:{
        username:[
          { required:true, message:'请输入账号', trigger:'blur' },
        ],
        password:[
          { required:true, message:'请输入密码', trigger:'blur' },
        ]
      }
    }
  },
  methods:{
    handleForgetPass(){
      this.forgetUserForm={}
      this.forgetPassDialogVis=true
    },
    resetPassword(){
      this.$request.put('/password',this.forgetUserForm).then(res =>{
        if(res.data.code === '200'){
          this.$message.success("重制成功")
          this.forgetPassDialogVis=false
        }else{
          this.$message.error(res.data.msg)
        }
      })
    },
    login(){
      this.$refs["loginRef"].validate((valid) => {
        if(valid){
          // 使用导入的request而不是this.$request
          request.post('login', this.user).then(res=>{
            // 调整响应数据访问路径
            if(res.code ==='200'){
              this.$router.push('/')
              this.$message.success('登陆成功')
              localStorage.setItem('honey-user',JSON.stringify(res))
            }else{
              this.$message.error(res.msg)
            }
          }).catch(error => {
            console.error('登录请求失败:', error)
            this.$message.error('登录失败，请重试')
          })
        }
      })
    },
    // 新增确认重置密码的处理方法
    handleResetPassword() {
      // 这里可以添加重置密码的逻辑
      this.forgetPassDialogVis = false
    }
  }
}
</script>

<style scoped>

</style>
