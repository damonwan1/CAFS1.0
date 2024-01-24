import request from "@/utils/request";
import requestLogin from "@/utils/requestLogin";
// export function login(username, password) {
//   return request({
//     url: '/user/login',
//     method: 'post',
//     data: {
//       username,
//       password
//     }
//   })
// }

export function getInfo(token) {
  return request({
    url: "/user/info",
    method: "get",
    params: { token }
  });
}
export function getActivityConfig() {
  return request({
    url: `/demo/show-result/page?year=2019&current=1&size=1`,
    method: "get",
    headers: {
      "Content-type": "application/json;charset=UTF-8"
    }
  });
}
export function logout() {
  return request({
    url: "/isoftstone/dms/auth/logout",
    method: "get"
  });
}

// 获取登录用户的用户名称
export function getusername(token) {
  return request({
    url: `/isoftstone/dms/auth/getusername?token=${token}`,
    method: "get"
  });
}

export function login(username, password) {
  return request({
    url: "/isoftstone/dms/auth/login",
    method: "post",
    data: {
      username,
      password
    }
  });
}

export function forwardResults() {
  return request({
    url: `/demo/show-result/page`,
    method: "get"
  });
}

export function getAllData(code) {
  return request({
    url: `/demo/show-result/all?site_code=${code}`,
    method: "get"
  });
}

export function getDriverData(code) {
  return request({
    url: `/demo/forecast-result/all?site_code=${code}`,
    method: "get"
  });
}
export function checkUserRole(data) {
  return request({
    url: `/isoftstone/dms/auth/checkRole?token=${data}`,
    method: "get",
    params: data
  });
}

export function register(username, password, email) {
  const data = {
    username,
    password,
    email
  };
  return requestLogin({
    url: "/auth/register",
    method: "post",
    data
  });
}

export function loginByUsername(username, password) {
  const data = {
    username,
    password
  };
  return requestLogin({
    url: "/auth/login",
    method: "post",
    data
  });
}
export function getUserInfo() {
  return requestLogin({
    url: "/auth/user",
    method: "get"
  });
}

// 误差棒
export function errorbar(code) {
  return request({
    url: `/demo/forecast-result/errorbar?site_code=${code}`,
    method: "get"
  });
}
// 多站点查询
export function allstation() {
  return request({
    url: `/demo/operation/allstation`,
    method: "get"
  });
}

// 生态数据误差棒
export function ecologyerrorbar(code) {
  return request({
    url: `/demo/show-result/errorbar?siteCode=${code}`,
    method: "get"
  });
}

// 生态数据真实数据
export function getEcologyData(code, start, end) {
  return request({
    url: `/demo/zoologHistory-drive/all?site_code=${code}&startDate=${start}&endDate=${end}`,
    method: "get"
  });
}
