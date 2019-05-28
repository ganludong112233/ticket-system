<template>
    <div id="order">
        <div class="container">
            <!--导航切换-->
            <div class="topnav">
                <p @click="changeStatus(1)" :class="ordertype==1?'blue':''">全部发版申请 ({{allnum}})</p>
                <p @click="changeStatus(2)" :class="ordertype==2?'blue':'' ">待处理发版申请 <span v-if="waitnum">({{waitnum}})</span></p>
                <!--<p  @click="changeStatus(3)"    :class="ordertype==3?'blue':'' ">审核记录</p>-->
            </div>

            <!--表格数据-->
            <div class="table">
                <div class="container">
                    <!--搜索条件-->
                    <div class="handle-box" v-if="ordertype!=2">
                        <el-select v-model="searchForm.status"  placeholder="状态" class="handle-select mr10"
                                   v-if="ordertype==1"    style="width: 15%">
                            <el-option key="1" label="全部" value=""></el-option>
                            <el-option key="2" label="新建" value="1"></el-option>
                            <el-option key="3" label="审批中" value="2"></el-option>
                            <el-option key="4" label="审批完成" value="4"></el-option>
                            <el-option key="5" label="已拒绝" value="3"></el-option>
                            <el-option key="6" label="已撤销" value="5"></el-option>
                        </el-select>

                        <el-select v-model="searchForm.projectId"  placeholder="项目" class="handle-select mr10"
                                   v-if="ordertype==1"    style="width: 20%">
                            <el-option  :key="item.id" :label="item.name"  :value="item.id"  v-for="(item,index)  in  project"></el-option>
                        </el-select>


                        <el-input v-model="searchForm.submitUser" placeholder="提交人" class="handle-input mr10"
                                  style="width: 12%"></el-input>
                        <el-input v-model="searchForm.title" placeholder="标题" class="handle-input mr10"
                                  style="width: 38%"></el-input>
                        <div class="block" style="width:40%">
                            <el-date-picker
                                    v-model="time"
                                    type="daterange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </div>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                        <el-button type="primary" icon="search" @click="edition(0,0)" v-if="ordertype==1">新增</el-button>
                        <el-button type="primary" icon="search" @click="empty" plain>重置</el-button>
                    </div>

                    <!--表格数据-->
                    <el-table
                            :data="tableData"
                            border
                            class="table"
                            ref="multipleTable"
                            :cell-class-name="hoverStyle"
                            @cell-click="linkDetail"
                            :header-cell-style="{background:'#ecf5ff'}"
                    >
                        <el-table-column
                                prop="id"
                                label="编号"
                                sortable
                                min-width="60"
                                cell-class-name="redpkgNum"
                                align="center"
                        >
                        </el-table-column>
                        <el-table-column prop="type" label="类型" min-width="80"     align="center">
                            <template slot-scope="scope">
                                <span v-if="scope.row.type==1?true:''"> 版本迭代 </span>
                                <span v-if="scope.row.type==2?true:''">bugfix</span>
                                <span v-if="scope.row.type==3?true:''"> 代码优化</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="version" label="版本号" min-width="70"      align="center">
                        </el-table-column>
                        <el-table-column prop="status" label="状态" min-width="80"  >
                            <template slot-scope="scope">
                                <span v-if="scope.row.status==1?'true':''"><span class="smallRound smallRound2"></span>新建</span>
                                <span v-if="scope.row.status==2?'true':''"><span class="smallRound smallRound5"></span>审批中</span>
                                <span v-if="scope.row.status==4?'true':''"><span class="smallRound smallRound3"></span>审批完成</span>
                                <span v-if="scope.row.status==3?'true':''"><span class="smallRound smallRound6"></span>已拒绝</span>
                                <span v-if="scope.row.status==5?'true':''"><span class="smallRound smallRound1"></span>已撤销</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="currentProcessor" label="当前处理人" :show-overflow-tooltip="true" min-width="100"     align="center">
                        </el-table-column>
                        <el-table-column prop="title" label="标题" :show-overflow-tooltip="true" min-width="150">
                        </el-table-column>
                        <el-table-column
                                prop="createTime"
                                label="创建时间"
                                min-width="120"
                                :formatter="formatTime"
                                align="center"
                        >
                        </el-table-column>
                        <!--<el-table-column prop="comment" label="备注" :show-overflow-tooltip="true" min-width="100">-->
                        <!--</el-table-column>-->
                        <el-table-column label="操作" align="center" min-width="240" v-if="ordertype==1">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-circle-check-outline" @click="revoke(2, scope.row)"
                                           v-if="(scope.row.status==1||scope.row.status==3)&&userid==scope.row.creatorId">提交
                                </el-button>
                                <el-button type="text" icon="el-icon-edit" @click="edition(1, scope.row.id)"
                                           v-if="(scope.row.status==1||scope.row.status==3)&&userid==scope.row.creatorId">编辑
                                </el-button>
                                <el-button type="text" icon="el-icon-search" @click="check(0,scope.row.id)">查看
                                </el-button>
                                <el-button type="text" icon="el-icon-edit-outline" class="red"
                                           @click="revoke(0, scope.row)"
                                           v-if="(scope.row.status==2||scope.row.status==3)&&userid==scope.row.creatorId">撤销
                                </el-button>
                                <el-button type="text" icon="el-icon-delete" class="red" @click="revoke(1, scope.row)"
                                           v-if="scope.row.status==1">删除
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" min-width="170" v-if="ordertype==2">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-time" @click="check(1, scope.row.id)">审核
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" min-width="170" v-if="ordertype==3">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-search" @click="check(0,scope.row.id)">查看
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <!--分页-->
                    <div class="pagination" v-if="ordertype!=2">
                        <el-pagination
                                background
                                @current-change="handleCurrentChange"
                                layout="prev, pager, next"
                                :current-page=pageNum
                                :page-size=pageSize
                                :total="total">
                        </el-pagination>
                    </div>
                </div>
                <!-- 删除提示框 -->
                <el-dialog title="提示" :visible.sync="isdele" width="300px" center>
                    <div class="del-dialog-cnt" v-if="deletype==1">删除无法恢复，是否继续？</div>
                    <div class="del-dialog-cnt" v-else-if="deletype==0">撤销后无法继续执行，是否确认？</div>
                    <div class="del-dialog-cnt" v-else>提交审核后无法删除，是否确认？</div>
                    <span slot="footer" class="dialog-footer">
                <el-button @click="isdele = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
                </el-dialog>
            </div>
        </div>
    </div>
</template>

<script>
    import basetable from '../common/BaseTable';

    export default {
        name: 'workorder',
        data() {
            return {
                allnum:0,   //全部发版数目
                waitnum:null,  //代发版数目
                project:[],   //全部项目
                userType:"",   //登录人状态
                userid:"",   //登录人id
                ordertype: 1,
                setingid: "",     //正在操作的数据id
                tableData: [],  //表格数据
                time: "",    //时间转换
                searchForm: {       //搜索条件
                    status: "",    //工单状态
                    submitUser: "",    //提交人
                    title: "",  //工单标题
                    startTime: "",    //开始时间
                    endTime: "",    //结束时间
                    projectId:"",
                },
                soumettre: '',    //提交时间
                isdele: false,     //删除提示框展示
                deletype: "",     //撤销或者删除   0,撤销  1删除
                allorder: [],     //全部工单工单
                pending: [],      //待处理工单
                history: [],       //历史工单
                pageNum: 1,
                pageSize: 10,
                total: 0,
            }
        },
        components: {
            basetable
        },
        methods: {
            //表格时间转换
            formatTime(time) {
                return this.$m.newTime(time.createTime, 2)
            },

            //切换导航样式
            changeStatus(num) {
                this.ordertype = num;
                this.tableData=[ ];
                switch (num){
                    case 1:
                        this.search();
                        break;
                    case 2:
                        this.queryWaitingList();
                        break;
                }
            },
            //搜索和查询全部工单
            search() {
                // console.log(this.searchForm, "搜索条件");
                if (this.time != "") {
                    this.searchForm.startTime = this.$m.newTime(this.time[0], 1);
                    this.searchForm.endTime = this.$m.newTime(this.time[1], 1);
                }
                this.$axios({
                    url: "/deploy/queryList",//请求路径
                    type: "get",//请求方式
                    params: {
                        status: this.searchForm.status,
                        submitor: this.searchForm.submitUser,
                        projectId:this.searchForm.projectId,
                        title: this.searchForm.title,
                        startTime: this.searchForm.startTime,
                        endTime: this.searchForm.endTime,
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.tableData = res.data.items;
                        this.total = res.data.totalCount;
                        this.allnum = res.data.totalCount;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //查询所有项目
            getProject(){
                this.$axios({
                    url: "/project/query?parentId=1",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.length > 0) {
                            this.project=res;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //待处理的发版请求列表
            queryWaitingList(){
                this.$axios({
                    url: "/deploy/queryWaitingList",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if (res.code == 0) {
                        this.tableData = res.data;
                        this.total = res.data.totalCount;
                        this.waitnum = res.data.length;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //重置搜索条件
            empty() {
                this.searchForm.status = ""
                this.searchForm.submitUser = "";
                this.searchForm.title = "";
                this.time = "";
                this.searchForm.startTime=""
                this.searchForm.endTime=""

            },

            //查看详情,type   0,查看  1，审核，
            check(type, id) {
                this.$router.push({
                    path: "/editionDeatil",
                    query: {
                        type: type,
                        id: id
                    }
                })
            },

            //新增或者修改
            edition(type, id) {
                this.$router.push({
                    path: "/editionoperation",
                    query: {
                        type: type,
                        id: id
                    }
                });
            },

            //撤销或者删除，弹出模态框，0，撤销，1，删除， 2，提交
            revoke(type, row) {
                this.deletype = type;
                this.setingid = row.id;
                this.isdele = true;
            },

            // 确定删除或者撤销或者提交
            deleteRow() {
                if (this.deletype == 0) {    //撤销或者删除   0,撤销  1删除   2，提交
                    this.revokeorder();
                } else if (this.deletype == 1) {
                    this.deleorder();
                } else {
                    this.submitorder();
                }
                this.isdele = false;
            },

            //删除工单
            deleorder() {
                this.$axios({
                    url: "/deploy/delete",//请求路径
                    type: "post",//请求方式
                    params: {
                        deployId: this.setingid,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.$message.success("删除成功！");
                        this.search();
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //撤销工单
            revokeorder() {
                this.$axios({
                    url: "/deploy/revoke",//请求路径
                    type: "post",//请求方式
                    params: {
                        deployId: this.setingid,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.$message.success("撤销成功！");
                        this.search();
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //提交工单
            submitorder() {
                this.$axios({
                    url: "/deploy/submit",//请求路径
                    type: "post",//请求方式
                    params: {
                        deployId: this.setingid,
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.$message.success("提交成功！");
                        this.search();
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            // 分页导航,当前页改变
            handleCurrentChange(val) {
                this.pageNum = val;
                this.search();
            },

            // 移入样式
            hoverStyle({row, column, rowIndex, columnIndex}) {
                if (columnIndex == 0) {
                    return 'point';
                }
            },

            // 跳转详情
            linkDetail(row, column, cell, event) {
                // console.log(column, "linkDetail");
                if(column.label =="编号"){
                    this.$router.push({
                        path: "/editionDeatil",
                        query: {
                            type: 0,
                            id: row.id
                        }
                    })
                }

            },

            //检查是否登录
            islogin(){
                if(localStorage.getItem('userinfo')) {
                    var userinfo =JSON.parse(localStorage.getItem('userinfo'))
                    sessionStorage.username=userinfo.username;
                    sessionStorage.userId=userinfo.userId;
                    this.userType=userinfo.type;   //用户类型 :1 管理员 2 普通用户
                    this.userid=userinfo.userId;   //用户类型 :1 管理员 2 普通用户
                    this.search();
                    this.getProject();
                }else{
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

    #order .topnav {
        border-bottom: 2px solid #e5e5e5;
        height: 30px;
        margin-bottom: 30px;
    }

    #order .topnav p {
        /*width:10%;*/
        display: inline-block;
        margin-right: 3%;
        font-size: 14px;
        color: #303133;
        height: 100%;
    }

    #order .blue {
        color: #409eff !important;
        border-bottom: 2px solid #409eff;
    }

    .block .el-input__inner {
        width: 260px !important;
    }

    td {
        height: 20px !important;
        font-size: 12px !important;
    }

    .point:hover > .cell {
        color: blue;
        text-decoration: underline;
        text-decoration-color: blue;
    }

    .point {
        cursor: pointer;
    }

    /* 表格状态的圆点 */
    .smallRound {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        margin-right: 8px;
    }

    .smallRound1 {
        background-color: rgba(217, 217, 217, 1);
    }

    .smallRound2 {
        background-color: rgb(26, 191, 197);
    }

    .smallRound3 {
        background-color: rgba(80, 197, 26, 1);
    }

    .smallRound4 {
        background-color: rgb(197, 80, 26);
    }

    .smallRound5 {
        background-color: rgba(245, 166, 35, 1);
    }

    .smallRound6 {
        background-color: #F5222D;
    }
</style>
<style scoped>
    .red {
        color: red;
    }

    .handle-box {
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .mr10 {
        margin-right: 10px;
    }

</style>

