<template>
  <div style="width: 100%">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="asideWidth" style="min-height: 100vh; background-color: #001529">
        <div
            style="
            height: 60px;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          <img src="@/assets/logo.webp" style="width: 40px; height: 40px">
          <span class="logo" v-show="!isCollapse">honey2025</span>
        </div>
        <el-menu  :collapse="isCollapse" :collapse-transition="false" router style="border: none" :default-active="$route.path" background-color="#001529" text-color="rgba(255,255,255,0.65)" active-text-color="#fff" >
          <el-menu-item index="/">
            <el-icon><House /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/1">
            <el-icon><House /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/2">
            <el-icon><House /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu>
            <!-- 正确的插槽使用方式，v-slot:title 表示使用名为 title 的插槽 -->
            <template v-slot:title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item  index="/">用户信息</el-menu-item>
            <el-menu-item>管理员信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <!-- 头部区域 -->
        <el-header>

          <el-icon style="font-size: 35px; cursor: pointer;" @click="handleCollapse">
            <!-- 根据折叠状态动态显示不同图标 -->
            <template v-if="isCollapse">
              <Expand /> <!-- 折叠状态时显示"展开"图标 -->
            </template>
            <template v-else>
              <Fold /> <!-- 展开状态时显示"折叠"图标 -->
            </template>
          </el-icon>

          <el-breadcrumb style="margin-left: 20px">
            <el-breadcrumb-item :to="{path:'/'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{path:'/user'}">用户管理</el-breadcrumb-item>
          </el-breadcrumb>

          <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: end;">
            <el-icon style="font-size: 20px; margin: 5px" @click="handleFull"><FullScreen /></el-icon>
            <el-dropdown>
              <div style="display: flex; align-items: center; justify-content: end;">
                <img
                    src="@/assets/logo.webp"
                    style="width: 40px; height: 40px; cursor: default"
                alt="用户头像"
                >
                <span>管理员</span>
              </div>
              <template v-slot:dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人信息</el-dropdown-item>
                  <el-dropdown-item>修改密码</el-dropdown-item>
                  <el-dropdown-item>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主体区域 -->
        <el-main>
          <div style="box-shadow: 0 0 10px rgba(0,0,0,.1); padding: 10px 20px; border-radius: 5px; margin-bottom: 10px">
            早安，骚年，祝你开心每一天！
          </div>

          <div style="display: flex">
            <el-card style="width: 50%; margin-right: 10px">
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

            <el-card style="width: 50%">
              <template v-slot:header>
                <div class="clearfix">
                  <span>渲染用户数据</span>
                </div>
              </template>
              <div>
                <el-table :data="users">
                  <el-table-column label="ID" prop="id"></el-table-column>
                  <el-table-column label="用户名" prop="username"></el-table-column>
                  <el-table-column label="姓名" prop="name"></el-table-column>
                  <el-table-column label="地址" prop="address"></el-table-column>
                </el-table>
              </div>
            </el-card>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {House, Menu,Expand,Fold,FullScreen} from "@element-plus/icons-vue";
import request from '@/utils/request';

export default ({
  name:"HomeView",
  data(){
    return {
      isCollapse:false,
      asideWidth:'200px',
      users:[]
    }
  },
  mounted() {
    // axios.get('http://localhost:8080/user/selectAll')
    //     .then(res => {
    //       console.log('请求成功', res.data);
    //       this.users=res.data.data
    //     })
    //     .catch(error => {
    //       console.error('请求失败', error);
    //     });
    request.get('user/selectAll').then(res=>{
      this.users = res.data
    })
  },
  methods:{
    handleFull(){
      document.documentElement.requestFullscreen()
    },
    handleCollapse(){
      this.isCollapse =! this.isCollapse
      this.asideWidth = this.isCollapse ? '64px' : '200px'
    }
  },
  components: {Menu, House,Expand,Fold,FullScreen}
})

</script>

<style>
  body{
    margin: 0;
  }
  .el-menu--inline .el-menu-item{
    background-color: #000c17 !important;
    padding-left: 50px !important;
  }

  .el-menu-item:hover, .el-sub-menu__title:hover{
    color: #fff !important;
  }

  .el-menu-item.is-active{
    padding: 20px 20px 20px 16px!important;
    background-color: #40a9ff !important;
    border-radius: 5px !important;
    width:calc(100% - 8px);
    margin-left: 4px;
  }
  .el-menu.el-menu--inline .el-menu-item.is-active{
    padding: 20px 20px 20px 46px!important;

  }

  .el-menu-item, .el-sub-menu__title{
    height: 40px !important;
    line-height: 40px !important;
  }

  .el-sub-menu .el-menu-item{
    min-width: 0 !important;
  }

  .el-aside{
    transition: width .3s;
    box-shadow: 2px 0 6px rgba(0,21,41,.35);
  }

  .logo{
    margin-left: 5px;
    font-size: 20px;
    transition: all .3s;
  }

  .el-header{
    box-shadow: 2px 0 6px rgba(0,21,41,.35);
    display: flex;
    align-items: center;
  }

</style>