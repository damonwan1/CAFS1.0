import { asyncRoutes, constantRouterMap } from '@/router'
import { loginByUsername, getUserInfo } from '@/api/login'
import { setLoginFlag, getLoginFlag } from '@/utils/auth'
import router from '../../router'
const USER_LOGIN_FLAG = 1
// const USER_LOGOUT_FLAG = 0;
/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: [],
  workFlowData: {
    cored_id: null,
    calculation: null,
    func: null
  },
  userLoginFlag: getLoginFlag(),
  introduction: '',
  user: '',
  roles: [],
  code: '',
  setting: {
    articlePlatform: []
  },
  status: '',
  avatar: '',
  name: '',
  requests: []
  // switchRoute: '我是默认切换路由'
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRouterMap.concat(routes)
  },
  setType(state, data) {
    state.workFlowData.cored_id = data.core_id
    state.workFlowData.calculation = data.calculation
    state.workFlowData.func = data.func
  },
  SET_LOGIN_FLAG: (state, flag) => {
    state.userLoginFlag = flag
  },
  SET_USER: (state, user) => {
    state.user = user
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  }
  // SET_ROUTE: (state, data) => {
  //   state.switchRoute = data
  //   router.push('/content')
  // }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      console.log('roles:' + roles)
      if (roles.includes('1')) {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },
  jump(state, items) {
    state.type = items.items
    console.log(state.type)
  },
  LoginByUsername({ commit }, userInfo) {
    const username = userInfo.username.trim()
    return new Promise((resolve, reject) => {
      loginByUsername(username, userInfo.password).then(response => {
        commit('SET_LOGIN_FLAG', USER_LOGIN_FLAG) // 设置登录状态userLoginFlag为1
        setLoginFlag(USER_LOGIN_FLAG) //
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  GetUserInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getUserInfo().then(response => {
        if (response.status != 200 || !response.data) { // 由于mockjs 不支持自定义状态码只能这样hack
          reject('error')
        }
        const user = response.data.data
        commit('SET_USER', user)

        if (user.roles && user.roles.length > 0) { // 验证返回的roles是否是一个非空数组
          commit('SET_ROLES', user.roles.map(role => role.roleName))
        } else {
          reject('getInfo: roles must be a non-null array !')
        }

        commit('SET_NAME', user.username)
        // TODO
        commit('SET_AVATAR', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif')
        commit('SET_INTRODUCTION', '')
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  },
  GenerateRoutes({ commit }, data) {
    return new Promise(resolve => {
      let accessedRouters
      if (data.includes('admin')) {
        accessedRouters = asyncRouterMap
      } else {
        accessedRouters = filterAsyncRouter(asyncRouterMap, data)
      }
      commit('SET_ROUTERS', accessedRouters)
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
