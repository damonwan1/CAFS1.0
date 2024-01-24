<template>
  <div class="head">
    <el-row>
      <el-col :span="4">
        <!-- <p style="font-size:24px;margin-top:17px;padding-left:126px;font-weight: 400;">LOGO</p> -->
        <img src="~@/assets/logo-head-2.png" style="height:60px;padding-left:126px;" alt="">
      </el-col>
      <el-col :span="13" style="padding-left:35px;;padding-top:2px">
        <el-menu
          :default-active="active"
          mode="horizontal"
          text-color="black"
          router
          active-text-color="#1890FF"
        >
          <el-menu-item index="/content" style="font-size:16px;font-weight: 500;font-family: Microsoft YaHei">首页</el-menu-item>
          <el-menu-item index="/publisher" style="font-size:16px;font-weight: 500;padding-left:24px;font-family: Microsoft YaHei">工作流</el-menu-item>
          <el-menu-item index="/drag" style="font-size:16px;font-weight: 500;padding-left:24px;font-family: Microsoft YaHei">计算任务</el-menu-item>
          <el-menu-item index="/echarts" style="font-size:16px;font-weight: 500;padding-left:24px;font-family: Microsoft YaHei">实时预测</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="7" >
        <img style="padding-top:24px;padding-left:161px;" src="~@/assets/icon-begin-4-active.svg" >
        <img
          style="margin-left:24px;"
          src="~@/assets/icon-begin-4-active.svg"
        >
        <img
          style="margin-left:24px;"
          src="~@/assets/icon-begin-4-active.svg"
        >
        <span style="margin-left:5px;font-weight:400;font-size: 16px;color: rgba(0,0,0,1); ;vertical-align: 10%;font-family:Microsoft YaHei">登入</span>
        <img
          style="margin-left:24.88px;"
          src="~@/assets/icon-begin-4-active.svg"
        >
        <span style=";margin-left:5px;font-weight:400;font-size: 16px;color: rgba(0,0,0,1);vertical-align: 10%;font-family:Microsoft YaHei">注册</span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'MyHead',
  data() {
    return {
      active: this.$route.path
    }
  },
  mounted() {
    $('.el-menu').css({
      'border-bottom': '0px'
    })
    $('.is-active').css({
      'border-bottom': '0px'
    })
    this.$router.push(this.active)
  },
  methods: {
    // home() {
    //   this.$router.push("/content");
    // },
    // jump() {
    //   this.$router.push("/publisher");
    // },
    // jumpDrag() {
    //   this.$router.push("/drag");
    // },
    // jumpEcharts() {
    //   this.$router.push("/echarts");
    // }
  }
}
</script>

<style lang="scss" scoped>
.head {
  height: 64px;
  margin: auto;
  // padding-top:17px;
  background: #ffffff;
  box-shadow: 0px 1px 4px 0px rgba(0,21,41,0.12);
  // font-family: PingFangSC-Medium;
  // font-family: PINGFANG MEDIUM
}
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: 0px solid #ffffff;
}
.el-menu--horizontal > .el-menu-item {
  border-bottom: 0px solid #ffffff;
}

// @import '~@/styles/font.css'
</style>
