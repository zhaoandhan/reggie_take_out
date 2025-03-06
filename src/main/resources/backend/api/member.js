//在前端，要传递json数据，必须使用 POST、PUT 请求，并在 body 里发送 JSON 数据
//但GET 请求通常只支持 URL 参数
function getMemberList (params) {
  return $axios({
    url: '/employee/page',//所以这个请求会被request.js中的拦截器拦截，修改成url参数的格式
    method: 'get',
    params
  })
}

// 修改---启用禁用接口
function enableOrDisableEmployee (params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

// 新增---添加员工
function addEmployee (params) {
  return $axios({
    url: '/employee',
    method: 'post',
    data: { ...params }
  })
}

// 修改---添加员工
function editEmployee (params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

// 修改页面反查详情接口
function queryEmployeeById (id) {
  return $axios({
    url: `/employee/${id}`,
    method: 'get'
  })
}