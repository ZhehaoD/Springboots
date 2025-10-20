<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询名称" v-model="name"></el-input>
      <el-button type="primary" @click="load(1)" style="margin-left: 10px">查询</el-button>
      <el-button type="primary" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0 ">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <el-table :data='tableData' stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#333'}" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="30" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" align="center"></el-table-column>
      <el-table-column prop="no" label="订单编号"></el-table-column>
      <el-table-column prop="name" label="订单名称"></el-table-column>
      <el-table-column prop="money" label="订单金额"></el-table-column>
      <el-table-column prop="user" label="用户"></el-table-column>
      <el-table-column prop="date" label="创建时间"></el-table-column>

      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope">
          <el-button type="primary" plain size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" plain size="small" @click="del(scope.row.id)">删除</el-button>
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

    <el-dialog v-model="formVisible" title="公告信息" width="60%">
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="订单名称" prop="name" style="margin-left: 15px">
          <el-input v-model="form.name" placeholder="订单名称"></el-input>
        </el-form-item>
        <el-form-item label="订单金额" prop="money" style="margin-left: 15px">
          <el-input v-model="form.money" placeholder="订单金额"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="category" style="margin-left: 15px">
          <el-select style="width: 100%" v-model="form.category">
            <el-option v-for="item in ['水果','蔬菜','零食','饮料','奶制品','糕点']" :key="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" style="display: flex; justify-content: flex-end;">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save" style="margin-left: 10px;">
          保 存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import { ElMessage } from 'element-plus'; // 引入消息提示组件
export default ({
  name: "OrdersView",
  data() {
    return {
      tableData: [],
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      pageNum: 1,
      pageSize: 5,
      username: '',
      name: '',
      content:'',
      total: 0,
      formVisible: false,
      dialogTitle: 'Shipping address',
      form: {},
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
        ],
        money: [
          { required: true, message: '请输入金额', trigger: 'blur' },
        ],
        category: [
          { required: true, message: '请输入分类', trigger: 'blur' },
        ],
      },
      ids:[],
      editor:null,

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
      request.delete('/orders/delete/batch',{
        data:this.ids
      }).then(res =>{
        if(res.code === "200"){
          this.$message.success('操作成功')
          this.load(1)
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
        request.delete('/orders/delete/'+id).then(res =>{
          if(res.code === "200"){
            this.$message.success('操作成功')
            this.load(1)
          }else{
            this.$message.error(res.msg || '保存失败');
          }
        })
      }).catch(()=>{})
    },
    handleEdit(row) {
      if (!row) {
        ElMessage.error('数据异常，无法编辑');
        return;
      }
      this.dialogTitle = '编辑用户信息';
      this.form = JSON.parse(JSON.stringify(row));
      this.formVisible = true;
    },
    handleAdd() {
      this.dialogTitle = '新增用户信息';
      this.form = {};
      this.formVisible = true;

    },
    save(){
      this.$refs.formRef.validate((valid) => {
        if(valid) {
          this.sendSaveRequest()
        }
      })
    },
    sendSaveRequest(){
      request({
        url: this.form.id ? '/orders/update':'/orders/add',
        method: this.form.id ? 'PUT' : 'POST',
        data: this.form
      }).then(res => {
        if(res.code === "200"){
          this.$message.success('保存成功')
          this.formVisible = false;
          this.load(); // 重新加载数据
        }else{
          this.$message.error(res.msg || '保存失败');
        }
      })
    },
    reset() {
      this.title = '';
      this.load()
    },
    load(targetPageNum) { // 接收可选的目标页码参数
      console.log(targetPageNum)
      if (targetPageNum !== undefined) {
        this.pageNum = targetPageNum;
      }
      // 2. 发起请求时，使用更新后的 this.pageNum
      request.get('orders/selectByPage', {
        params: {
          pageNum: this.pageNum, // 此时已同步为传入的 targetPageNum（如1）
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
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