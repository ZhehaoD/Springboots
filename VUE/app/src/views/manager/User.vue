<template>
  <div>
    <el-table :data='tableData' stripe>
      <el-table-column prop="id" label="序号" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="phone" label="手机"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="avatar" label="头像">
        <template v-slot="scope">
          <div style="display: flex;align-items: center">
            <el-image style="width: 50px; height: 50px; border-radius: 50%" v-if="scope.row.avatar" :src="scope.row.avatar"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>
      <el-table-column label="操作">
          <el-button type="primary" plain size="small">编辑</el-button>
          <el-button type="danger" plain size="small">删除</el-button>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from '@/utils/request';
export default ({
  name:"UserView",
  data(){
    return {
      tableData:[],
      user:JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  },
  mounted() {
    request.get('user/selectAll').then(res=>{
      this.tableData = res.data
    })
  }
})
</script>

<style scoped>

</style>