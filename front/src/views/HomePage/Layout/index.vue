<template>
  <div class="page-container">
    <page-header />
    <!-- <page-main/> -->
    <router-view />
    <page-footer />
  </div>
</template>

<script>
import { PageHeader, PageFooter, PageMain } from './components'
import vueEvent from '../../Coronavirus/mock/vueEvent'

export default {
  name: 'HomePage',
  components: {
    PageHeader,
    PageFooter,
    PageMain
  },
  data() {
    return {
    }
  },
  mounted() {
    window.addEventListener('scroll', this.mybutton, true)
  },
  updated() {
  },
  methods: {
    mybutton() {
      var scroll = $('div').scrollTop()
      var myheight = document.body.clientHeight
      var webpage = $('div')[0].scrollHeight
      vueEvent.$emit('scroll', scroll)
    }
  }
}
</script>
<style lang="scss" scoped>
.page-container{
min-width:1280px
}
</style>
