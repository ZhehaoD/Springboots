<template>
  <div>
    <el-card style="width: 50%;">
      <el-form :model="user">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/file/upload"
              :headers="{token:user.data.token}"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.data.avatar" :src="user.data.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.data.username" :placeholder="user.data.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name" style="margin-left: 15px">
          <el-input v-model="user.data.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone" style="margin-left: 15px">
          <el-input v-model="user.data.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" style="margin-left: 15px">
          <el-input v-model="user.data.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address" style="margin-left: 15px">
          <el-input type="textarea" v-model="user.data.address" placeholder="地址"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update" >保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:"PersonS",
  data(){
    return{
      user:JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  },
  created() {
  },
  methods: {
    update() {
      // 使用导入的request实例，而非this.$request
      request.put('/user/update', this.user.data)
          .then(res => {
            // 注意：成功提示应该用success，不是error
            if (res.code === '200') {
              this.$message.success('保存成功');
              localStorage.setItem('honey-user', JSON.stringify(this.user))
              this.$emit('update:user', this.user);
            } else {
              this.$message.error(res.msg || '保存失败');
            }
          })
          .catch(error => {
            // 捕获请求异常（比如网络错误）
            this.$message.error('请求失败，请稍后重试');
            console.error('请求错误:', error);
          });
    },
    handleAvatarSuccess(response){
      console.log(response.data)
      this.user.data.avatar = response.data
    }
  }
}
</script>
<style scoped>
:deep(.el-form-item__label) {
  font-weight: bold;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}

avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  border-radius: 50%;

}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

</style>