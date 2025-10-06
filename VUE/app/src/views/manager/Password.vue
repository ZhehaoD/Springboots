<template>
  <div>
    <el-card style="width: 50%">
      <el-form ref="formRef" :model="passwords" :rules="rules">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="passwords.password" placeholder="原始密码" ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword" style="margin-left: 15px">
          <el-input show-password v-model="passwords.newPassword" placeholder="新密码" ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="passwords.confirmPassword" placeholder="确认密码" ></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update" >确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"PasswordS",
  data(){
    // 修复：验证确认密码与新密码是否一致
    const validatePassword = (rule, confirmPass, callback) =>{
      if (confirmPass === ''){
        callback(new Error('请再次输入密码'))
      }else if (confirmPass !== this.passwords.newPassword){  // 修复错误：this.password → this.passwords.newPassword
        callback(new Error('两次密码不一样'))
      }else{
        callback()
      }
    }
    return{
      user:JSON.parse(localStorage.getItem('honey-user') || '{}'),
      passwords:{
        password:'',
        newPassword:'',
        confirmPassword:''
      },
      rules:{
        password:[
          { required:true, message:'请输入原始密码', trigger:'blur' },
        ],
        newPassword:[
          { required:true, message:'请输入新密码', trigger:'blur' },
        ],
        confirmPassword:[
          {required:true, validator: validatePassword, trigger:'blur'}
        ],
      }
    }
  },
  methods:{
    update() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {

          this.user.data.password=this.passwords.newPassword
          request.put('/user/update', this.user.data)
              .then(res => {
                // 注意：成功提示应该用success，不是error
                if (res.code === '200') {
                  this.$message.success('保存成功');
                  this.$router.push('/login')
                }
              })
              .catch(error => {
                // 捕获请求异常（比如网络错误）
                this.$message.error('请求失败，请稍后重试');
                console.error('请求错误:', error);
              });
        }
      })
    }
  }
}
</script>
<style scoped>

</style>
