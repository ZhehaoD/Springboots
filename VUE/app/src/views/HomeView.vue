<template>
  <div style="width: 100%">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside style="width: 200px; min-height: 100vh; background-color: #001529">
        <div
            style="
            height: 60px;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
          "
        >
          logo
        </div>
        <el-menu router style="border: none" :default-active="$route.path" background-color="#001529" text-color="rgba(255,255,255,0.65)" active-text-color="#fff" >
          <el-menu-item index="/">
            <template v-slot:title>
              <el-icon><House /></el-icon>
              <span>系统首页</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/1">
            <template v-slot:title>
              <el-icon><House /></el-icon>
              <span>系统首页</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/2">
            <template v-slot:title>
              <el-icon><House /></el-icon>
              <span>系统首页</span>
            </template>
          </el-menu-item>
          <el-sub-menu>
            <!-- 正确的插槽使用方式，v-slot:title 表示使用名为 title 的插槽 -->
            <template v-slot:title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item>用户信息</el-menu-item>
            <el-menu-item>管理员信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <!-- 头部区域 -->
        <el-header>头部区域</el-header>
        <!-- 主体区域 -->
        <el-main>主体区域</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {House, Menu} from "@element-plus/icons-vue";
import {defineComponent} from "vue";

export default defineComponent({
  components: {Menu, House}
})

</script>

<style>
  .el-menu--inline .el-menu-item{
    background-color: #000c17 !important;
    padding-left: 50px !important;
  }

  .el-menu-item:hover, .el-sub-menu__title:hover{
    color: #fff !important;
  }

  .el-menu-item.is-active{
    background-color: #40a9ff;
    border-radius: 5px !important;
    margin: 4px;
  }

  .el-menu-item, .el-sub-menu__title{
    height: 40px !important;
    line-height: 40px !important;
    margin: 4px;
  }
  
</style>