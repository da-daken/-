import request from '@/utils/request'

// 查询用户、家政员列表
export function listOrder(query) {
  return request({
    url: '/order/order/list',
    method: 'get',
    params: query
  })
}

// 查询用户、家政员详细
export function getOrder(id) {
  return request({
    url: '/order/order/' + id,
    method: 'get'
  })
}

// 新增用户、家政员
export function addOrder(data) {
  return request({
    url: '/order/order',
    method: 'post',
    data: data
  })
}

// 修改用户、家政员
export function updateOrder(data) {
  return request({
    url: '/order/order',
    method: 'put',
    data: data
  })
}

// 删除用户、家政员
export function delOrder(id) {
  return request({
    url: '/order/order/' + id,
    method: 'delete'
  })
}

// 创建订单
export function createOrder(data) {
  return request({
    url: '/order/order/create',
    method: 'post',
    data: data
  })
}

// 生成防重令牌
export function generateToken(id) {
  return request({
    url: '/order/order/generateToken',
    method: 'get',
    param: id
  })
}

// 支付订单
export function payOrder(data) {
  return request({
    url: '/order/order/parOrder',
    method: 'post',
    data: data
  })
}

// 取消订单
export function cancelOrder(data) {
  return request({
    url: '/order/order/parOrder',
    method: 'post',
    data: data
  })
}

// 评价订单
export function commitOrder(data) {
  return request({
    url: '/order/order/commit',
    method: 'post',
    data: data
  })
}

// 订单核销
export function checkOrder(data) {
  return request({
    url: '/order/order/check',
    method: 'post',
    data: data
  })
}

// 获取角色id
export function getRoleInfo(userId) {
  return request({
    url: '/order/order/generateToken',
    method: 'get',
    param: userId
  })
}

