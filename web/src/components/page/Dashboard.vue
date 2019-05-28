<template>
    <div id="index">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:180px;">
                    <div class="user-info">
                        <img src="../../assets/photo.jpg" class="user-avator" alt="">
                        <div class="user-info-cont">
                            <div class="user-info-name">{{name}}</div>
                            <div>{{role}}</div>
                        </div>
                    </div>
                    <!--<div class="user-info-list">上次登录时间：<span>2018-01-01</span></div>-->
                    <!--<div class="user-info-list">上次登录地点：<span>成都</span></div>-->
                </el-card>
                <el-card shadow="hover">
                    <div slot="header" class="clearfix">
                        <span>操作记录</span>
                    </div>
                    <el-table
                            :data="operation"
                            :show-header="false"
                            id="historyTable"
                            style="width: 100%;font-size:14px;height: 200px;overflow-y: scroll"
                    >
                        <el-table-column    prop="operator"  min-width="28" :show-overflow-tooltip="true">
                        </el-table-column>
                        <el-table-column  prop="op"   min-width="30"  :show-overflow-tooltip="true">
                            <template slot-scope="scope">
                                <el-tag  type="success">{{scope.row.op}}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column  prop="opTarget"   min-width="90"  :show-overflow-tooltip="true">
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="16">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-lx-notice grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{orderNum||0}}</div>
                                    <div>待处理工单</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-lx-goods grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{editionNum || 0}}</div>
                                    <div>待处理发布申请</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card shadow="hover">
                    <div slot="header" class="clearfix">
                        <span>待处理工单</span>
                    </div>
                    <el-table
                            :data="untreated"
                            :show-header="false"
                            style="width: 100%;font-size:14px;"
                            :cell-class-name="hoverStyle"
                            @cell-click="linkDetail"
                    >
                        <el-table-column  prop="title"   :show-overflow-tooltip="true"   @click="check(1, scope.row.id)">
                        </el-table-column>
                        <el-table-column width="60">
                            <template slot-scope="scope">
                                <i class="el-icon-search"></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
                <el-card shadow="hover">
                    <div slot="header" class="clearfix">
                        <span>待发布申请</span>
                    </div>
                    <el-table
                            :data="unpublished"
                            :show-header="false"
                            style="width: 100%;font-size:14px;"
                            :cell-class-name="hoverStyle"
                            @cell-click="linkDetail2"
                    >
                        <el-table-column  prop="title"   :show-overflow-tooltip="true"   @click="check(1, scope.row.id)">
                        </el-table-column>
                        <el-table-column width="60">
                            <template slot-scope="scope">
                                <i class="el-icon-search"></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>

        <!--图表统计-->
        <el-row :gutter="20">
            <el-col :span="12">

                <div id="orderCanvas" style="width:100%; height:400px;"></div>
                <P class="changeTab">
                    <span   :class="canvasindex==0?'masteColor':''"   @click="orderTable(0)">周</span> / <span   :class="canvasindex==1?'masteColor':''"   @click="orderTable(1)">月</span></P>
            </el-col>
            <el-col :span="12">
                <div id="editionsCanvas" style="width:100%; height:400px;"></div>
                <P class="changeTab">
                    <span  :class="canvasindex2==3?'masteColor':''"  @click="editionsTable(3)">月</span> / <span   :class="canvasindex2==4?'masteColor':''"   @click="editionsTable(4)">周</span></P>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import Schart from 'vue-schart';
    import bus from '../common/bus';

    export default {
        name: 'dashboard',
        data() {
            return {
                canvasindex:0,
                canvasindex2:3,
                orderNum: "",    //待处理工单总数
                editionNum: "",    //待处理发版总数
                userType: "",
                name: "",   //用户名展示
                untreated: [],   //待处理工单列表
                unpublished: [],    //未发布工单
                operation: [],   //操作记录

                orderWeekkey:[],     //工单图表x坐标数组
                orderWeekvalue:[],     //工单图表x坐标数组

                editionsWeekkey:[],     //发版图表x坐标数组
                editionsWeekvalue:[],     //发版图表x坐标数组

                optionline: {
                    backgroundColor: ['white'],
                    title: {
                        text: '发版申请',
                        textStyle: {
                            color: ['#303133'],
                            fontWeight: ['normal'],
                            fontSize: ['16'],
                        },
                        padding: [15, 15]
                    },
                    tooltip: {},
                    legend: {
                        data: ['']
                    },
                    xAxis: {
                        data: []
                    },
                    yAxis: {},
                    series: [{
                        name: '发版处理',
                        type: 'line', //设置图表主题
                        data: []
                    }]
                },

                optionbar: {
                    backgroundColor: ['white'],
                    title: {
                        text: '工单处理',
                        textStyle: {
                            color: ['#303133'],
                            fontWeight: ['normal'],
                            fontSize: ['16'],
                        },
                        padding: [15, 15]
                    },
                    color: ['#3398DB'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: [],
                            axisTick: {
                                alignWithLabel: true
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '处理工单',
                            type: 'bar',
                            barWidth: '60%',
                            data: []
                        }
                    ]
                }

            }
        },
        components: {
            Schart
        },
        computed: {
            role() {
                return this.name === 'admin' ? '超级管理员' : '普通用户';
            }
        },

        methods: {
            // 跳转工单审核详情
            linkDetail(row) {
                this.$router.push({
                    path: "/orderdetail",
                    query: {
                        type: 1,
                        id:row.id
                    }
                })
            },
            // 跳转发版审核详情
            linkDetail2(row){
                this.$router.push({
                    path: "/editionDeatil",
                    query: {
                        type: 1,
                        id:row.id
                    }
                })
            },

            // 移入样式
            hoverStyle({row, column, rowIndex, columnIndex}) {
                if (columnIndex == 0) {
                    return 'point';
                }
            },

            
            //获取待处理工单列表总数
            getorderCount() {
                this.$axios({
                    url: "/wo/queryWaitingListCount",//请求路径
                    type: "get",//请求方式
                    params: {
                        pageNum: 1,
                        pageSize: 3,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.orderNum = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //待处理发版申请总数
            queryWaitingListCount(){
                this.$axios({
                    url: "/deploy/queryWaitingListCount",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.editionNum = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //查询待处理工单
            queryWaitingList() {
                this.$axios({
                    url: "/wo/queryWaitingList",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.untreated = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //查询操作记录
            getOperation() {
                this.$axios({
                    url: "/oplogs",//请求路径
                    type: "get",//请求方式
                    params: {
                        pageNum: 1,
                        pageSize: 1000,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.operation = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },


            //查询待发布工单
            getUnpublished() {
                this.$axios({
                    url: "/deploy/queryWaitingList",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.unpublished = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },


            //发版统计
            editionsTable(num) {
                this.canvasindex2=num;
                num==3 ?this.editionsCanvas("month"):this.editionsCanvas("week");
            },

            //工单统计
            orderTable(num){
                this.canvasindex=num;
                num==1 ?this.orderCanvas("month"):this.orderCanvas("week");
            },

            //请求工单统计数据
            orderCanvas(type){
                this.$axios({
                    url: "/wo/statistic/"+type,//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.orderWeekkey=[];
                        this.orderWeekvalue=[];
                        for(var i=0;i<res.data.length;i++){
                            this.orderWeekkey.push(res.data[i].item)
                            this.orderWeekvalue.push(res.data[i].value)
                            this.optionbar.xAxis[0].data=this.orderWeekkey;
                            this.optionbar.series[0].data=this.orderWeekvalue;
                            window.chartmainline = this.$echarts.init(document.getElementById("orderCanvas"));    //柱状
                            chartmainline.setOption(this.optionbar);
                        }
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //请求发版统计数据
            editionsCanvas(type){
                this.$axios({
                    url: "/deploy/statistic/"+type,//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.editionsWeekkey=[];
                        this.editionsWeekvalue=[];
                        for(var i=0;i<res.data.length;i++){
                            this.editionsWeekkey.push(res.data[i].item)
                            this.editionsWeekvalue.push(res.data[i].value)
                            this.optionline.xAxis.data=this.editionsWeekkey;
                            this.optionline.series[0].data=this.editionsWeekvalue;
                            //基于准本好的DOM，初始化echarts实例
                            window.chartmainline2 = this.$echarts.init(document.getElementById("editionsCanvas"));    //柱状
                            chartmainline2.setOption(this.optionline);
                        }
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //检查是否登录
            islogin() {
                if (localStorage.getItem('userinfo')) {
                    var userinfo = JSON.parse(localStorage.getItem('userinfo'))
                    // console.log(userinfo,"009")
                    sessionStorage.username = userinfo.realname;
                    sessionStorage.userId = userinfo.userId;
                    this.userType = userinfo.type;   //用户类型 :1 管理员 2 普通用户
                    this.name = sessionStorage.username;
                    this.getorderCount();
                    this.queryWaitingListCount();
                    this.queryWaitingList();
                    this.getOperation();
                    this.getUnpublished();
                    this.orderCanvas("week");
                    this.editionsCanvas("month");
                } else {
                    this.$router.push("/login");
                }
            }
        },
        mounted() {
            this.islogin();
        }
    }

</script>

<style>
    /*滚动条样式*/
    #historyTable::-webkit-scrollbar {/*滚动条整体样式*/
        width: 8px;     /*高宽分别对应横竖滚动条的尺寸*/
        height:8px;
    }
    #historyTable::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
        border-radius: 5px;
        -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
        background: rgba(0,0,0,0.2);
    }
    #historyTable::-webkit-scrollbar-track {/*滚动条里面轨道*/
        -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
        border-radius: 0;
        background: rgba(0,0,0,0.1);
    }

    .point:hover > .cell {
        color: blue;
        text-decoration: underline;
        text-decoration-color: blue;
    }
    canvas{
        width: 100% !important;
    }
    .point {
        cursor: pointer;
    }
    .el-card__body{
        margin-top: -15px;
        /*padding-top: 10px !important;*/
    }
    .el-card{
        margin-bottom: 10px;
    }
    .mgb20{
        margin-bottom: 10px;
    }
</style>
<style scoped>
    #index .el-col-12 {
        border: 1px solid transparent;
        position: relative;

        /*padding-top: 10px;*/
    }

    #index .changeTab {
        position: absolute;
        top: 10px;
        right: 12%;
        color: #666;
    }

    .el-icon-search, .masteColor {
        color: #2d8cf0;
    }

    .changeTab {
        position: relative;

    }

    .masteColor:before {
        background-color: #2d8cf0 !important;
    }

    .changeTab span:before {
        content: " ";
        display: inline-block;
        width: 15px;
        height: 10px;
        border-radius: 3px;
        margin-right: 3px;
        /*border: 1px solid red;*/
        position: relative;
        left: 0;
        top: -1px;
        background-color: #e5e5e5;
    }

    .el-row {
        margin-bottom: 20px;
    }

    .grid-content {
        display: flex;
        align-items: center;
        height: 100px;
    }

    .grid-cont-right {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: #999;
    }

    .grid-num {
        font-size: 30px;
        font-weight: bold;
    }

    .grid-con-icon {
        font-size: 50px;
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        color: #fff;
    }

    .grid-con-1 .grid-con-icon {
        background: rgb(45, 140, 240);
    }

    .grid-con-1 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-2 .grid-con-icon {
        background: rgb(100, 213, 114);
    }

    .grid-con-2 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-3 .grid-con-icon {
        background: rgb(242, 94, 67);
    }

    .grid-con-3 .grid-num {
        color: rgb(242, 94, 67);
    }

    .user-info {
        display: flex;
        align-items: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #ccc;
        margin-bottom: 20px;
    }

    .user-avator {
        width: 120px;
        height: 120px;
        border-radius: 50%;
    }

    .user-info-cont {
        padding-left: 50px;
        flex: 1;
        font-size: 14px;
        color: #999;
    }

    .user-info-cont div:first-child {
        font-size: 26px;
        color: #222;
        margin-bottom: 10px;
    }

    .user-info-list {
        font-size: 14px;
        color: #999;
        line-height: 25px;
    }

    .user-info-list span {
        margin-left: 70px;
    }

    .mgb20 {
        margin-bottom: 20px;
    }

    .todo-item {
        font-size: 14px;
    }

    .todo-item-del {
        text-decoration: line-through;
        color: #999;
    }

    .schart {
        width: 100%;
        height: 300px;
    }

</style>
