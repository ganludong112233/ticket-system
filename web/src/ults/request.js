/**
 * @author lcx
 * @date 19/1/21
 * @Description:  封装的axios请求
 * 
 */
import axios from 'axios';
import store from '@/store/index.js';
import router from '@/router'
import Vue from 'vue'
import { Message} from 'element-ui';


// create an axios instance

const service = axios.create({
  baseURL:process.env.VUE_APP_URL,
  timeout: 30000, // request timeout,
  withCredentials: false
});

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'; // 配置请求头（推荐）
// service.interceptors.push((request, next) => {
//   request.credentials = true;
//   next();
// });
// request interceptor
// axios.defaults.withCredentials=true
// service.interceptors.request.credentials = true;
// service.interceptors.request.use(
//   config => {
//     // Do something before request is sent
//     if (localStorage.token || store.getters.token) {
//       // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
//       // config.headers['accessToken'] = localStorage.token || '';
//     }
//     return config;
//   },
//   error => {
//     // Do something with request error
//     console.log(error); // for debug
//     Promise.reject(error);
//   }
// );

// response interceptor
service.interceptors.response.use(
  // response => response,
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 
   * 如想通过 xmlhttprequest 来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  response => {
    const res = response.data;
      if(res.code==401){
          Message.warning("您的登录信息已过期！")
          router.push("/login");
      }
    return res
  },
  error => {
    console.log('err' + error); // for debug
    return Promise.reject(error)
  }
);
const myaxios = function ({
    data,
    params,
    url,
    type,
    load
  }) {
    if(load||load == undefined){
      store.commit('changeLoading',true);
    }
    
    return new Promise((resolve, reject)=>{
      service({
        method: type,
        url: url,
        data: data,
        params:params
      }).then((res)=>{
        //如果动画为true，返回之后需要关闭动画
        if (load || load == undefined) {
          resolve(res); 
          store.commit('changeLoading',false)
        }else{
          resolve(res); 
        }

      }).catch(res=>{
        if (load || load == undefined) {
          reject(res)
          store.commit('changeLoading',false)
        }else{
          reject(res)
        }
        
      })
    });
    }
    export default myaxios