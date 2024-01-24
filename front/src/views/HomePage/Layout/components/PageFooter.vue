<template>
  <div class="foot">
    <el-row>
      <el-col
        style="font-family:Microsoft YaHei; font-size: 14px;color: #FFFFFF;; line-height: 20px; font-weight: 400;margin-top:64px;text-align:center" >
        <span >{{ $t('footerList.Home') }}</span>
        <span style="padding-left:20px;">{{ $t('footerList.Help') }}</span>
        <span style="padding-left:20px;">{{ $t('footerList.Contact') }}</span>
        <span style="padding-left:20px;">{{ $t('footerList.Privacy') }}</span>
        <span style="padding-left:20px;">{{ $t('footerList.Feedback') }}</span>
      </el-col>
      <el-col style="margin-top:14px;text-align:center">
        <img src="~@/assets/white_icon/email.svg" >
        <img src="~@/assets/white_icon/weibo.svg" style="padding-left:16px;" >
        <img src="~@/assets/white_icon/wetach.svg" style="padding-left:16px" >
      </el-col>
      <el-col>
        <el-divider class="el-divider--horizontal "/>
      </el-col>
      <el-col style="font-size: 14px;color: rgba(255,255,255,0.65);text-align:center;margin-bottom:16px">Copyright © cnic, All Rights Reserved.</el-col>
    </el-row>
    <!-- <el-row>
      <el-col :span="3" style="border:1px solid  #122627"/>
      <el-col :span="18" style="text-align:center;padding-top:64px">
        <el-row>
          <el-col
            style="font-family:Microsoft YaHei; font-size: 14px; color: rgba(0,0,0,1); line-height: 20px; font-weight: 400;" >
            <span style="color:rgba(255, 255, 255, .75)">网站首页</span>
            <span style="padding-left:20px;color:rgba(255, 255, 255, .75)">帮助中心</span>
            <span style="padding-left:20px;color:rgba(255, 255, 255, .75)">联系我们</span>
            <span style="padding-left:20px;color:rgba(255, 255, 255, .75)">隐私政策</span>
            <span style="padding-left:20px;color:rgba(255, 255, 255, .75)">意见反馈</span>
          </el-col>
          <el-col style="padding-top:8px;font-size:14px">
            <span
              style="font-family:Microsoft YaHei; font-size: 14px; line-height: 20px; font-weight: 400; color:rgba(255, 255, 255, .75)" >Copyright © cnic, All Rights Reserved.</span>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="3" style="padding-top:62px">
        <img src="~@/assets/Workflow/icon_email.png" >
        <img src="~@/assets/Workflow/icon_webo.png" style="padding-left:16px;" >
        <img src="~@/assets/Workflow/icon_wetach.png" style="padding-left:16px" >
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
export default {
  name: 'Bottom',
  data() {
    return {}
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.foot {
  height:198px;
  margin: auto;
    background: #122627;
  // border:1px solid red
}
.el-divider--horizontal{
  margin-bottom:16px;
  margin-top:16px;
  height: 1px;
background: rgba(255,255,255,0.15)
}
</style>

