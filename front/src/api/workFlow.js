import request2 from '@/utils/request2'

export function getAssortAll() {
  // 新建模板计算方式与功能选项      1
  return request2({
    url: '/isoftstone/dms/template/getAssortAll?assortType',
    method: 'get'
  })
}
export function addCore(obj) {
  // 新建模板，确定选项，将所选择的数据添加至工作流列表并查询 与上方的getCorePageAll配合使用     2
  return request2({
    url: '/isoftstone/dms/core/addCore',
    method: 'post',
    data: obj
  })
}

export function getCorePageAll(currentPage, pageSize) {
  // 分页查询工作流任务列表     3   每个工作流任务缺少一个assortIds
  return request2({
    url: `/isoftstone/dms/core/getCorePageAll?currentPage=${currentPage}&pageSize=${pageSize}`,
    method: 'get'
  })
}

export function getTemplateAll() {
  // 工作流左侧导航栏信息     4
  return request2({
    url: '/isoftstone/dms/template/getTemplateAll',
    method: 'get'
  })
}

export function getModelByType(str) {
  // 查询模型选择节点模型的选项 5
  return request2({
    url: `/isoftstone/dms/basics/getModelByType?type=${str}`,
    method: 'get'
  })
}

export function getBasicsByCoreId(id) {
  // 根据id查询模型选择节点的数据上传与时间步长的数据   6
  return request2({
    url: `/isoftstone/dms/basics/getBasicsByCoreId?core_id=${id}`,
    method: 'get'
  })
}

export function addBasics(obj) {
  // 工作流模型选择节点中 确定保存所选数据    7
  return request2({
    url: '/isoftstone/dms/basics/addBasics',
    method: 'post',
    data: obj
  })
}

export function addTemplate(obj) {
  // 工作流往左侧导航栏添加新节点
  return request2({
    url: '/isoftstone/dms/template/addTemplate',
    method: 'post',
    data: obj
  })
}

export function updateCore(obj) {
  // 修改已经存在的工作流任务列表中的任务基本信息
  return request2({
    url: '/isoftstone/dms/core/updateCore',
    method: 'post',
    data: obj
  })
}

export function getCoreAssortByCoreId(id) {
  // 根据id查询模型选择节点的数据上传与时间步长的数据   6
  return request2({
    url: `/isoftstone/dms/core/getCoreAssortByCoreId?core_id=${id}`,
    method: 'get'
  })
}
export function getModelById(id) {
  // 根据id查询模型选择节点的数据上传与时间步长的数据   6
  return request2({
    url: `/isoftstone/dms/basics/getModelById?id=${id}`,
    method: 'get'
  })
}

export function modelFile(type) {
  // 根据id查询模型选择节点的数据上传与时间步长的数据   6
  return request2({
    url: `/isoftstone/dms/upload/modelFile?type=${type}`,
    method: 'get'
  })
}

export function addBasicsAttribute(obj) {
  // 修改已经存在的工作流任务列表中的任务基本信息
  return request2({
    url: '/isoftstone/dms/basics/addBasicsAttribute',
    method: 'post',
    data: obj
  })
}

export function getBaseAttrbuteByCoreId(id) {
  // 根据id查询模型选择节点的数据上传与时间步长的数据   6
  return request2({
    url: `/isoftstone/dms/basics/getBaseAttrbuteByCoreId?core_id=${id}`,
    method: 'get'
  })
}

export function startWorkFlow(core_id, filename) {
  // 检查选择的模型，生成历史ID
  return request2({
    url: `/isoftstone/dms/template/startWorkFlow?core_id=${core_id}&fileName=${filename}`,
    method: 'get'
  })
}

export function getResults(id) {
  // 根据历史ID获得数据
  return request2({
    url: `/isoftstone/dms/template/getResults?id=${id}`,
    method: 'get'
  })
}

export function getHistoryPageAll(page, total) {
  // 分页查看所有历史记录
  return request2({
    url: `/isoftstone/dms/history/getHistoryPageAll?currentPage=${page}&pageSize=${total}`,
    method: 'get'
  })
}

export function getHistoryByCoreId(id) {
  // 根据历史ID查询单个历史记录
  return request2({
    url: `/isoftstone/dms/history/getHistoryByCoreId?id=${id}`,
    method: 'get'
  })
}
