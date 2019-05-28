import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: { title: '自述文件' },
            children:[
                {
                    path: '/edition',
                    component: resolve => require(['../components/page/Edition.vue'], resolve),
                    meta: { title: '发版列表' }
                },
                {
                    path: '/editionoperation',
                    component: resolve => require(['../components/page/Editionoperation.vue'], resolve),
                    meta: { title: '发版新增与编辑' }
                },
                {
                    path: '/editionDeatil',
                    component: resolve => require(['../components/page/Editiondetail.vue'], resolve),
                    meta: { title: '发版查看与审核' }
                },
                {
                    path: '/personnel',
                    component: resolve => require(['../components/page/Personnel.vue'], resolve),
                    meta: { title: '人员管理' }
                },
                {
                    path: '/workorder',
                    component: resolve => require(['../components/page/Workorder.vue'], resolve),
                    meta: { title: '工单列表' }
                },
                {
                    path: '/orderoperation',
                    component: resolve => require(['../components/page/Orderoperation.vue'], resolve),
                    meta: { title: '工单新增与编辑' }
                },
                {
                    path: '/orderdetail',
                    component: resolve => require(['../components/page/Orderdetail.vue'], resolve),
                    meta: { title: '工单详情与审核' }
                },
                {
                    path: '/404',
                    component: resolve => require(['../components/page/404.vue'], resolve),
                    meta: { title: '未知' }
                },
                {
                    path: '/dashboard',
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve),
                    meta: { title: '首页' }
                },

            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})
