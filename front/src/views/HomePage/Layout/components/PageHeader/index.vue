<template>
  <div class="head">
    <el-row>
      <el-col :span="3">
        <img src="~@/assets/Workflow/logo-head-3.jpg" style="height:60px;padding-left:126px;" alt="">
      </el-col>
      <el-col :span="13" style="padding-left:35px;padding-top:2px">
        <el-menu :default-active="active" mode="horizontal" text-color="rgba(255, 255, 255, .75)" router
          active-text-color="#ffffff" style=" background:#233A40;color:rgba(255, 255, 255, .75)">
          <el-menu-item index="/siteMap"
            style="font-size:18px;font-weight: 500;padding-left:24px;font-family: 微软雅黑;background-color:#233A40;">{{
              $t('headerNav.SaR') }}{{
    siteName }}</el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="8" style="display: flex;align-items: center; padding-left: 200px;">
        <img style="" src="~@/assets/white_icon/search.svg">
        <img style="margin-left:24px;" src="~@/assets/white_icon/Upload.svg">
        <img style="margin-left:24px;" src="~@/assets/white_icon/Sign in.svg">
        <a class="link" @click="signIn">{{ $t('headerNav.login') }}</a>
        <img style="margin-left:24.88px;" src="~@/assets/white_icon/login.svg">
        <a class="link" @click="register">{{ $t('headerNav.register') }}</a>
        <template>
          <lang-select></lang-select>
        </template>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import vueEvent from '../../../../Coronavirus/mock/vueEvent'
import LangSelect from '../LangSelect/index.vue';
export default {
  name: 'MyHead',
  data() {
    return {
      active: this.$route.path,
      siteName: '',
      nameArr: [['(鼎湖山)', '(Dinghushan)'], ['(千烟洲)', '(Qianyanzhou)'], ['(会同)', '(Huitong)'], ['(尖峰岭)', '(Jianfengling)']]
    }
  },
  components: {
    LangSelect
  },
  watch: {
    $route() {
      this.active = this.$route.path
      if (this.$route.path === '/siteMap') {
        this.siteName = ''
      } else {
        return
      }
    },
    language(newval, old) {
      this.nameArr.map(item => {
        if (this.siteName == item[0]) {
          this.siteName = item[1]
        } else if(this.siteName == item[1]) {
          this.siteName = item[0]
        }
      })
    }
  },
  computed: {
    language() {
      return this.$store.getters.language
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
    vueEvent.$on('changePlatform', (data) => {
      if (data) {
        this.changeTitle(data)
      }
    })
  },
  methods: {
    signIn() {
      // this.$router.push('signIn')
    },
    register() {
      // this.$router.push('register')
    },
    changeTitle(data) {
      this.siteName = '(' + data + ')'
    },
    getTitle() {
      vueEvent.$on('changePlatform', (data) => {
        console.log(data);
        if (data) {
          this.changeTitle(data)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.head {
  position: static;
  min-width: 1280px;
  width: 100%;
  z-index: 1993;
  margin-left: -17px;
  height: 64px;
  margin: auto;
  // padding-top:17px;
  background: #233A40;
  color: rgba(255, 255, 255, .75);
  box-shadow: 0px 1px 4px 0px rgba(0, 21, 41, 0.12);
  // font-family: PingFangSC-Medium;
  // font-family: PINGFANG MEDIUM
}

.el-menu--horizontal>.el-menu-item.is-active {
  border-bottom: 0px solid #ffffff;
}

.el-menu--horizontal>.el-menu-item {
  border-bottom: 0px solid #ffffff;
}

.link {
  margin-left: 5px;
  font-weight: 400;
  font-size: 18px;
  color: rgba(0, 0, 0, 1);
  vertical-align: 10%;
  font-family: Microsoft YaHei;
  color: rgba(255, 255, 255, .75)
}

// @import '~@/styles/font.css'
</style>
