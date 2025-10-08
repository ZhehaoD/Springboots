<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询用户名" v-model="username"></el-input>
      <el-input style="width: 200px; margin: 0 5px" placeholder="查询姓名" v-model="name"></el-input>
      <el-button type="primary" @click="load(1)">查询</el-button>
    </div>
    <div style="margin: 10px 0 ">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="primary" @click="reset">重置</el-button>
      <el-button type="danger" @click="delBatch">批量删除</el-button>
    </div>
    <el-table :data='tableData' stripe :header-cell-style="{backgroundColor:'aliceblue',color:'#333'}" @selection-change="handleSelectionChange">
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
          :page-sizes="[100, 200, 300, 400]"
          layout="total, prev, pager, next"
          :total="total"
          @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog v-model="formVisible" :title="dialogTitle" width="30%">
      <el-form :model="form" :rules="rules" ref="formRef">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/file/upload"
              :headers="{token:user.data.token}"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="form.avatar" :src="form.avatar" style="width: 50px; height: 50px; border-radius: 50%"/>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name" style="margin-left: 25px">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone" style="margin-left: 25px">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" style="margin-left: 25px">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address" style="margin-left: 25px">
          <el-input type="textarea" v-model="form.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="权限" prop="role" style="margin-left: 25px">
          <el-radio v-model="form.role" label="管理员" name="userRole">管理员</el-radio>
          <el-radio v-model="form.role" label="用户" name="userRole" style="margin-left: 20px;">用户</el-radio>
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
import { Plus } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus'; // 引入消息提示组件

export default ({
  name: "UserView",
  components: { Plus },
  data() {
    return {
      tableData: [],
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      pageNum: 1,
      pageSize: 5,
      username: '',
      name: '',
      total: 0,
      formVisible: false,
      dialogTitle: 'Shipping address',
      form: {},
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
      },
      ids:[]
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
      request.delete('/user/delete/batch',{
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
        request.delete('/user/delete/'+id).then(res =>{
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
      this.form = { role:'用户'};
      this.formVisible = true;
    },
    save(){
      this.$refs.formRef.validate((valid) => {
        if(valid) {
          request({
            url: this.form.id ? '/user/update':'/user/add',
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
      this.name = '';
      this.username = '';
      this.load()
    },
    load(targetPageNum) { // 接收可选的目标页码参数
      console.log(targetPageNum)
      if (targetPageNum !== undefined) {
        this.pageNum = targetPageNum;
      }
      // 2. 发起请求时，使用更新后的 this.pageNum
      request.get('user/selectByPage', {
        params: {
          pageNum: this.pageNum, // 此时已同步为传入的 targetPageNum（如1）
          pageSize: this.pageSize,
          username: this.username,
          name: this.name
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
    handleAvatarSuccess(response) {
      console.log(response)
      if (response.data) {
        this.form.avatar = response.data;
      }
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