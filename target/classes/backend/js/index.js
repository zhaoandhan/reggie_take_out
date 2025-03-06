/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数
function requestUrlParam(argname){//argname就相当于比如传入id，找id=”多少“，获取这个”多少“
  var url = location.href//获取完整的请求url路径
  var arrStr = url.substring(url.indexOf("?")+1).split("&")//url.substring(url.indexOf("?")+1)表示取url从?后面第一个字符开始的子串
    //类似localhost:8080/a.html?id=1&name="Jack"
  for(var i =0;i<arrStr.length;i++)
  {
      var loc = arrStr[i].indexOf(argname+"=")//检查数组 arrStr 中第 i 个元素是否包含以 argname= 开头的子字符串，argname + "=" 会将参数名与等号拼接形成一个字符串
      //indexOf 方法返回该子字符串在 arrStr[i] 中的起始位置。如果存在，返回位置索引（≥0）；否则返回 -1
      if(loc!=-1){
          return arrStr[i].replace(argname+"=","").replace("?","")
      }
  }
  return ""
}
