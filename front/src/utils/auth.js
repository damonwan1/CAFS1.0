import Cookies from 'js-cookie'

const TokenKey = 'gateway_token'
const Language = 'language'
const loginFlagKey = 'LoginFlag'
const loginState = 'loginState'

export function getLoginFlag() {
  return Cookies.get(loginFlagKey)
}
export function getToken() {
  return Cookies.get(TokenKey)
}
// 获取language类型
export function getLanguage() {
  return Cookies.get(Language)
}

export function setToken(token) {
  // var inFifteenMinutes = new Date(new Date().getTime() + 60 * 2000)
  // return Cookies.set(TokenKey, token, { expires: inFifteenMinutes })
  return Cookies.set(TokenKey, token, 1)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
export function setLoginFlag(token) {
  return Cookies.set(loginFlagKey, token)
}
export function setLoginState(state) {
  return Cookies.set(loginState, state, {
    expires: 7
  })
}
export function getLoginState() {
  return Cookies.get(loginState)
}
