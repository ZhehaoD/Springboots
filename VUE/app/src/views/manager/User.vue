<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询用户名" v-model="username"></el-input>
      <el-input style="width: 200px; margin: 0 5px" placeholder="查询姓名" v-model="name"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
    </div>
    <div style="margin: 10px 0 ">
      <el-button type="primary">新增</el-button>
      <el-button type="primary" @click="reset">重置</el-button>
      <el-button type="danger">批量删除</el-button>
    </div>
    <el-table :data='tableData' stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#333'}">
      <el-table-column type="selection" width="30" align="center"></el-table-column>
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
      <el-table-column label="操作" align="center" width="180">
          <el-button type="primary" plain size="small">编辑</el-button>
          <el-button type="danger" plain size="small">删除</el-button>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[100, 200, 300, 400]"
          :size="10"
          :disabled="disabled"
          :background="background"
          layout="total, prev, pager, next"
          :total="total"
          @current-change="handleCurrentChange"
      />
    </div>
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
      pageNum:1,
      pageSize:3,
      username:'',
      name:'',
      total:0
    }
  },
  created(){
    this.load();
  },
  methods:{
    reset(){
      this.name = '';
      this.username = '';
      this.load()
    },
    load(){
      request.get('user/selectByPage',{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          name:this.name
        }
      }).then(res=>{
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum
      this.load()
    }
  }
})
</script>

<style scoped>

</style>