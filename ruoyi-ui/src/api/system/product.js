import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listProduct(query) {
  return request({
    url: '/system/product/list',
    method: 'get',
    params: query
  })
}
// 查询【请填写功能名称】列表
export function listProductForRe(query) {
  return request({
    url: '/system/product/listForRe',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getProduct(id) {
  return request({
    url: '/system/product/' + id,
    method: 'get'
  })
}

// 查询【请填写功能名称】详细 forRe
export function getInfoForRe(id) {
  return request({
    url: '/system/product/forRe/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addProduct(data) {
  return request({
    url: '/system/product',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateProduct(data) {
  return request({
    url: '/system/product',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delProduct(id) {
  return request({
    url: '/system/product/' + id,
    method: 'delete'
  })
}
