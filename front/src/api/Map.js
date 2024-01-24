import request from "@/utils/requestMap";

// export function getMap(data) {
//   return request({
//     url: "isoftstone/dms/result/mapDisplay",
//     method: "get",
//     params: data
//   });
// }

export function getMap(data) {
  return request({
    url: "result/mapDisplay",
    method: "get",
    params: data
  });
}
