<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询标题" v-model="title"></el-input>
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
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="description" label="简介"></el-table-column>
      <el-table-column prop="content" label="内容" >
        <template v-slot="scope">
          <el-button @click="showContent(scope.row.content)" size="small">显示内容</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="author" label="发布人"></el-table-column>
      <el-table-column prop="time" label="发布时间"></el-table-column>

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

    <el-dialog v-model="formVisible" title="新闻信息" width="60%" @close="closeDialog">
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title" style="margin-left: 15px">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="description" style="margin-left: 25px">
          <el-input v-model="form.description" placeholder="简介"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content" style="margin-left: 25px">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" style="display: flex; justify-content: flex-end;">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save" style="margin-left: 10px;">
          保 存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog v-model="formVisible1" title="内容" width="60%">
      <div class="w-e-text">
        <div v-html="content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import { ElMessage } from 'element-plus'; // 引入消息提示组件
import E from "wangeditor"
import hljs from "highlight.js";
export default ({
  name: "NewsView",
  data() {
    return {
      tableData: [],
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      pageNum: 1,
      pageSize: 5,
      username: '',
      title: '',
      total: 0,
      formVisible: false,
      dialogTitle: 'Shipping address',
      form: {},
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
        ],
      },
      ids:[],
      editor:null,
      content:'',
      formVisible1:'false'
    }
  },
  created() {
    this.load();
  },
  methods: {
    showContent(content){
      this.content=content
      this.formVisible1=true
    },
    closeDialog(){
      this.editor.destroy()
      this.editor=null
    },
    delBatch(){
      if(!this.ids.length){
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('确认批量删除这些数据吗吗？', '确认删除', {type:'warning'}).then(response => {
      console.log(response)
      request.delete('/news/delete/batch',{
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
        request.delete('/news/delete/'+id).then(res =>{
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
      this.$nextTick(()=>{
        this.editor=new E(`#editor`)
        this.editor.highlight=hljs
        this.editor.create()
        this.editor.txt.html(row.content)
      })
    },
    handleAdd() {
      this.dialogTitle = '新增用户信息';
      this.form = {};
      this.formVisible = true;
      this.$nextTick(()=>{
        this.editor=new E(`#editor`)
        this.editor.highlight=hljs
        this.editor.create()
      })

    },
    save(){
      this.$refs.formRef.validate((valid) => {
        if(valid) {
          let content=this.editor.txt.html()
          this.form.content = content
          request({
            url: this.form.id ? '/news/update':'/news/add',
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
      request.get('news/selectByPage', {
        params: {
          pageNum: this.pageNum, // 此时已同步为传入的 targetPageNum（如1）
          pageSize: this.pageSize,
          title: this.title,
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

<style scoped>
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
</style>