import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import 'xterm/dist/xterm.css'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // global css
import $ from 'jquery'
import App from './App'
import store from './store'
import router from './router'
import '@/icons' // icon
import '@/permission' // permission control
import Contextmenu from 'vue-contextmenujs'
import qs from 'qs'
/**
 * This project originally used easy-mock to simulate data,
 * but its official service is very unstable,
 * and you can build your own service if you need it.
 * So here I use Mock.js for local emulation,
 * it will intercept your request, so you won't see the request in the network.
 * If you remove `../mock` it will automatically request easy-mock data.
 */
import i18n from './lang'

// 引入makrdown插件
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import x2js from 'x2js'
Vue.prototype.$x2js = new x2js()
Vue.use(Contextmenu)
Vue.use(mavonEditor)
Vue.use(qs)
Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.config.productionTip = false

const vueMain = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})

Vue.prototype.validForbid = function(value) {
  if (value.length === 0) {
    // this.$message('请输入要搜索的内容')
    return true
  }
  var vuelength = value
  vuelength = vuelength.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '').replace(/\s/g, '')
  if (value.length > vuelength.length) {
    // this.$message('搜索内容不能包含特殊字符')
    return false
  }
  return true
}
export default vueMain
