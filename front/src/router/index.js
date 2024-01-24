
import Vue from 'vue'
import Router from 'vue-router'
// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
// import Layout from '@/views/layout/Layout'
// import singin from '@/views/Coronavirus/signIn'
import login from '@/views/Coronavirus/background'
/* buttom Layout */
// import buttomLayout from '@/views/HomePage/Layout/components/BottomRouter/layout'

/* HomePage */
import HomePage from '@/views/HomePage/Layout/index.vue'
/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in subMenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
**/
// const originalPush = Router.prototype.push
// Router.prototype.push = function push(location, onResolve, onReject) {
//   if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
//   return originalPush.call(this, location).catch((err) => err)
// }

export const constantRouterMap = [
  {
    path: '',
    component: login,
    hidden: true,
    children: [{
      path: '',
      name: 'signIn',
      meta: { requireAuth: true },
      component: () => import('@/views/Coronavirus/signIn')
      // component: () => import('@/views/login')
    }]
  },
  // {
  //   path: '/404',
  //   name: '404',
  //   component: () => import('@/views/404'),
  //   hidden: true
  // },
  // {
  //   path: '/',
  //   component: HomePage,
  //   hidden: true,
  //   children: [
  //     {
  //       path: 'colony/:id',
  //       name: 'colony',
  //       component: () => import('@/views/PersonalCenter/colony')
  //     }
  //   ]
  // },
  {
    path: '/',
    component: login,
    hidden: true,
    children: [
      {
        path: '/signIn',
        name: 'signIn',
        meta: { requireAuth: true },
        component: () => import('@/views/Coronavirus/signIn')
      }
    ]
  },
  {
    path: '/',
    component: login,
    hidden: true,
    children: [
      {
        path: '/register',
        name: 'register',
        meta: { requireAuth: true },
        component: () => import('@/views/Coronavirus/register')
      }
    ]
  },
  // 从这里开始是同化数据的ROUTER
  {
    path: '/',
    component: HomePage,
    hidden: true,
    children: [
      {
        path: '/siteMap',
        name: 'siteMap',
        meta: { requireAuth: true },
        component: () => import('@/views/Coronavirus/siteMap')
      },
      {
        path: '/siteDataPrediction',
        name: 'siteDataPrediction',
        meta: { requireAuth: true },
        component: () => import('@/views/Coronavirus/siteDataPrediction')
      },
      {
        path: '/test',
        name: 'test',
        meta: { requireAuth: true },
        component: () => import('@/views/Coronavirus/test')
      }
    ]
  }
]

export const asyncRoutes = [
  // {
  //   path: '',
  //   component: Layout,
  //   meta: { title: '' },
  //   children: [
  //     {
  //       path: 'myData',
  //       component: () => import('@/views/PersonalCenter/MyData'),
  //       name: 'MyData',
  //       meta: { title: '个人资料', icon: 'excel' }
  //     }
  //   ]
  // },
  // {
  //   path: '/',
  //   component: Layout,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'adminAudit',
  //       name: 'adminAudit',
  //       component: () => import('@/views/PersonalCenter/adminAudit'),
  //       meta: { title: '用户绑定管理', icon: '参数-01', roles: ['admin'] }
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'myAuthority',
  //       component: () => import('@/views/PersonalCenter/myAuthority/index'),
  //       name: 'MyAuthority',
  //       meta: { title: '用户管理', icon: '权限-01', roles: ['admin'] }
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'FTP',
  //       component: () => import('@/views/PersonalCenter/Ftp/index'),
  //       name: 'FTP',
  //       meta: { title: 'FTP服务器', icon: '上传-01', roles: ['admin'] }
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'Content',
  //       component: () => import('@/views/PersonalCenter/content/index'),
  //       name: 'Content',
  //       meta: { title: '内容管理', icon: 'chart', roles: ['admin'] }
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   hidden: true,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'typical_content/:id',
  //       component: () => import('@/views/PersonalCenter/content/TypicalContent'),
  //       name: 'typical_content'
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   hidden: true,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'document_content/:id',
  //       component: () => import('@/views/PersonalCenter/content/TechnicalDocumentatio'),
  //       name: 'document_content'
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   hidden: true,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'dynamic_content/:id',
  //       name: 'dynamic_content',
  //       component: () => import('@/views/PersonalCenter/content/DynamicContent')
  //     }
  //   ]
  // },
  // {
  //   path: '',
  //   component: Layout,
  //   hidden: true,
  //   meta: { title: '', roles: ['admin'] },
  //   children: [
  //     {
  //       path: 'resource_content/:id',
  //       name: 'resource_content',
  //       component: () => import('@/views/PersonalCenter/content/ResourceContent')
  //     }
  //   ]
  // },

  // { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  // { path: '/404', component: () => import('@/views/404'), hidden: true },

  // { path: '*', redirect: '/404', hidden: true }
]
export default new Router({
  mode: 'hash', // 后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
