

<template>
    <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding: 10px 20px; border-radius: 5px; margin-bottom: 10px">
      早安，{{user.data.name }}，祝你开心每一天！
    </div>
    <div style="display: flex">
      <el-card style="width: 100%">
        <template v-slot:header>
          <div class="clearfix">
            <span>毕设2025</span>
          </div>
        </template>
        <div>
          2025毕设正式开始了！
          <div style="margin-top: 20px">
            <div style="margin: 10px 0"><strong>主题色</strong></div>
            <el-button type="primary">按钮</el-button>
            <el-button type="success">按钮</el-button>
            <el-button type="warning">按钮</el-button>
            <el-button type="danger">按钮</el-button>
            <el-button type="info">按钮</el-button>
          </div>
        </div>
      </el-card>
    </div>
    <div style="display: flex; margin: 15px 0 ">
      <el-card style="width: 50%">
        <div style="margin-bottom: 15px">系统公告</div>
        <el-timeline style="padding: 0">
          <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time" placement="top">
            <el-card>
              <h4>{{ item.title }}</h4>
              <p>{{item.content}}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
</template>

<script>
import request from '@/utils/request';
export default ({
  name:"HomeView",
  data(){
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
      notices:[]
    }
  },
  created(){
    this.loadNotice()
  },
  methods:{
    loadNotice(){
      request.get('/notice/selectUserData').then(res => {
        this.notices=res.data
      })
    }
  }
})
</script>

<style scoped>

</style>