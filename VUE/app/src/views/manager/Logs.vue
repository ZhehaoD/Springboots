<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询模块" v-model="operation"></el-input>
      <el-select style="margin: 0 5px; width: 130px" v-model="type" :label="type">
        <el-option v-for="item in ['新增','修改','删除']" :key="item" :value="item"  ></el-option>
      </el-select>
      <el-button type="primary" @click="load(1)" style="margin-left: 10px">查询</el-button>
      <el-button type="primary" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0 ">
      <el-button type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <el-table :data='tableData' stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#333'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" align="center"></el-table-column>
      <el-table-column prop="operation" label="操作模块"></el-table-column>
      <el-table-column prop="type" label="操作类型">
        <template v-slot="scope">
          <el-tag type="primary" v-if="scope.row.type === '新增'">{{scope.row.type}}</el-tag>
          <el-tag type="info" v-if="scope.row.type === '修改'">{{scope.row.type}}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '删除'">{{scope.row.type}}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '批量删除'">{{scope.row.type}}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '登陆'">{{scope.row.type}}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '注册'">{{scope.row.type}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="ip" label="操作人IP"></el-table-column>
      <el-table-column prop="user" label="操作人"></el-table-column>
      <el-table-column prop="time" label="操作时间"></el-table-column>
      <el-table-column label="操作" align="center" width="100%">
        <template v-slot="scope">
          <el-button size="small" type="danger" plain @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
          @current-change="handleCurrentChange"
      />
    </div>

  </div>
</template>

<script>
import request from '@/utils/request';
import { ElMessage } from 'element-plus'; // 引入消息提示组件
export default ({
  name: "LogsView",
  data() {
    return {
      tableData: [],
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      pageNum: 1,
      pageSize: 5,
      total: 0,
      operation:'',
      form: {},
      ids:[],
      type:''
    }
  },
  created() {
    this.load();
  },
  methods: {
    delBatch(){
      if(!this.ids.length){
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('确认批量删除这些数据吗吗？', '确认删除', {type:'warning'}).then(response => {
        console.log(response)
        request.delete('/logs/delete/batch',{
          data:this.ids
        }).then(res =>{
          if(res.code === "200"){
            this.$message.success('操作成功')
            this.load()
          }else{
            this.$message.error(res.msg || '保存失败');
          }
        })
      }).catch(()=>{})},
    handleSelectionChange(rows){
      this.ids = rows.map(v => v.id)

    },
    del(id){
      this.$confirm('确认删除吗？', '确认删除', {type:'warning'}).then(response => {
        console.log(response)
        request.delete('/logs/delete/'+id).then(res =>{
          if(res.code === "200"){
            this.$message.success('操作成功')
            this.load(1)
          }else{
            this.$message.error(res.msg || '保存失败');
          }
        })
      }).catch(()=>{})
    },

    reset() {
      this.operation = '';
      this.type=''
      this.load()
    },
    load(targetPageNum) { // 接收可选的目标页码参数
      if (targetPageNum !== undefined) {
        this.pageNum = targetPageNum;
      }
      // 2. 发起请求时，使用更新后的 this.pageNum
      request.get('/logs/selectByPage', {
        params: {
          pageNum: this.pageNum, // 此时已同步为传入的 targetPageNum（如1）
          pageSize: this.pageSize,
          operation: this.operation,
          type:this.type
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data?.records || [];
        this.total = res.data?.total || 0;
      }).catch(err => {
        console.error('加载数据失败：', err);
        ElMessage.error('加载数据失败，请重试');
      });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  }
})
</script>

<style>
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 1px dashed #dcdfe6;
  border-radius: 50%;
}

.el-tooltip__popper{max-width: 10px !important}
</style>