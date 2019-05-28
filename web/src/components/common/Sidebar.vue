<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color=""
            text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>

        <p  class="control"  @click="collapseChage(1)"  v-show="temp==0"><i  class="el-icon-d-arrow-left  message-title"></i></p>
        <p  class="control"  @click="collapseChage(0)"  v-show="temp==1"><i  class="el-icon-d-arrow-right  message-title"></i></p>
    </div>
</template>

<script>
    import bus from '../common/bus';
    export default {
        data() {
            return {
                collapse: false,
                temp:0,
                items: [
                    {
                        icon: 'el-icon-lx-home',
                        index: 'dashboard',
                        title: '首页'
                    },
                    {
                        icon: 'el-icon-lx-cascades',
                        index: 'workorder',
                        title: '工单提交'
                    },
                    {
                        icon: 'el-icon-lx-copy',
                        index: 'edition',
                        title: '发版申请'
                    },
                    {
                        icon: 'el-icon-news',
                        index: 'personnel',
                        title: '人员管理'
                    },
                ]
            }
        },
        methods:{
            // 侧边栏折叠
            collapseChage(num){
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
                this.temp=num;
            },
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        },
        created(){
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 155px;
    }
    .sidebar > ul {
        height:100%;
    }
    .control{
        position: absolute;
        bottom: 0;
        right: 0;
        padding: 7%;
        background-color: #ecf5ff;
        text-align: center;
        font-weight: bold;
    }
</style>
