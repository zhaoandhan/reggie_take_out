function loginApi(data) {
  return $axios({//通过ajax向后端服务栏发送请求
    'url': '/employee/login',//请求地址
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
