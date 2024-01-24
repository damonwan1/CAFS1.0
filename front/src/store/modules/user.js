import Cookies from 'js-cookie'
import { login, logout, getusername } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    language: Cookies.get('language') || 'zh'
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_LANGUAGE: (state, language) => {
      state.language = language
      Cookies.set('language', language)
    }
  },

  actions: {
    setLanguage({ commit }, language) {
      commit('SET_LANGUAGE', language)
    },
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          // const data = response.data
          // setToken(data.token)
          // commit('SET_TOKEN', data.token)
          const token = response.msg
          setToken(token)
          commit('SET_TOKEN', token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    getInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        commit('SET_TOKEN', getToken())
        getusername(state.token).then(response => {
          const data = response.data
          const roles = []
          if (data) { // 验证返回的roles是否是一个非空数组
            if (response.msg == '501' || response.msg === 'user') {
              roles.push('user')
            } else {
              roles.push('admin')
            }
            // commit('SET_ROLES', data.roles)
            commit('SET_ROLES', roles)
          } else {
            if (response.msg == '501' || response.msg === 'user') {
              roles.push('user')
            } else {
              roles.push('admin')
            }
            // commit('SET_ROLES', data.roles)
            commit('SET_ROLES', roles)
            reject('getInfo: roles must be a non-null array !')
          }
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          resolve(roles)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
