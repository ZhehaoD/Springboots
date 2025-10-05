<template>
  <div>
    <div style="height: 100vh; overflow: hidden;display: flex; align-items: center;justify-content: center">
      <div style="font-size: 40px">404 找不到页面 <router-link to="/home">返回首页</router-link></div>
    </div>
  </div>
</template>

<style scoped>

</style>
<script>
export default ({
  name:"404S",
  data(){
    return {
      user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
    }
  }
})
</script>