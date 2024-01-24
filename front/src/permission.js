import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // getToken from cookie
import { asyncRoutes, constantRoutes } from '@/router'
import { getLoginState } from './utils/auth'

NProgress.configure({ showSpinner: false })// NProgress configuration

const whiteList = ['/'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  const token = getLoginState()
  if (!token && !whiteList.includes(to.path)) {
    // 没登录跳转到登录界面
    next('/')
  } else if (getLoginState() && to.path === '/') {
    // 登录后无法回退到登录界面
    next('/siteMap')
  } else {
    // 登录后的正常跳转
    next()
  }
})

router.afterEach((to, from, next) => {
  window.scrollTo(0, 0)
})
