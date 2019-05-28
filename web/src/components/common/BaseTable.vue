<template>
    <div class="table">
        <div class="container">
            <!--搜索条件-->
            <div class="handle-box"  v-if="ordertype!=2">
                <el-select v-model="searchForm.status" placeholder="工单状态" class="handle-select mr10"  v-if="ordertype==1">
                    <el-option key="1" label="全部" value="1"></el-option>
                    <el-option key="2" label="新建" value="2"></el-option>
                    <el-option key="3" label="审批中" value="3"></el-option>
                    <el-option key="4" label="已完成" value="4"></el-option>
                    <el-option key="5" label="已拒绝" value="5"></el-option>
                    <el-option key="6" label="已撤销" value="6"></el-option>
                </el-select>
                <el-input v-model="searchForm.name" placeholder="提交人" class="handle-input mr10"  style="width: 12%"></el-input>
                <el-input v-model="searchForm.title" placeholder="标题" class="handle-input mr10"  style="width: 38%"></el-input>
                <div class="block"  style="width:40%">
                    <el-date-picker
                            v-model="searchForm.time"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </div>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="primary" icon="search" @click="edition(0,0)"   v-if="ordertype==1">新增</el-button>
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
                        label="工单编号"
                        sortable
                        min-width="100"
                        cell-class-name="redpkgNum"
                >
                </el-table-column>
                <el-table-column prop="type" label="类型"  min-width="60">
                </el-table-column>
                <el-table-column prop="nouveau" label="提交人" min-width="70">
                </el-table-column>
                <el-table-column prop="status" label="状态"  min-width="70">
                    <template slot-scope="scope">
                        <span v-if="scope.row.status==1?'true':''"><span class="smallRound smallRound2"></span>新建</span>
                        <span v-if="scope.row.status==2?'true':''"><span class="smallRound smallRound5"></span>审批中</span>
                        <span v-if="scope.row.status==3?'true':''"><span class="smallRound smallRound3"></span>已完成</span>
                        <span v-if="scope.row.status==4?'true':''"><span class="smallRound smallRound6"></span>已拒绝</span>
                        <span v-if="scope.row.status==5?'true':''"><span class="smallRound smallRound1"></span>已撤销</span>
                    </template>
                </el-table-column>
                <el-table-column prop="nowPeoson" label="当前处理人"  min-width="90">
                </el-table-column>
                <el-table-column prop="title" label="标题"    :show-overflow-tooltip="true"  min-width="250">
                </el-table-column>
                <el-table-column prop="time" label="提交时间"  min-width="140">
                </el-table-column>
                <el-table-column label="操作" align="center"  min-width="170">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-circle-check-outline" @click="revoke(2, scope.row)"  v-if="ordertype==1">提交</el-button>
                        <el-button type="text" icon="el-icon-edit" @click="edition(1, scope.row)"  v-if="ordertype==1">编辑</el-button>
                        <el-button type="text" icon="el-icon-search" @click="check(0,scope.row)"  v-if="ordertype!=2">查看</el-button>
                        <el-button type="text" icon="el-icon-edit-outline" class="red"  @click="revoke(0, scope.row)"  v-if="ordertype==1">撤销</el-button>
                        <el-button type="text" icon="el-icon-time" @click="check(1, scope.row)"  v-if="ordertype==2">审核</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="revoke(1, scope.row)"  v-if="ordertype==1">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!--分页-->
            <div class="pagination"   v-if="ordertype!=2">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        layout="prev, pager, next"
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
</template>

<script>
    export default {
        name: 'basetable',
        data() {
            return {
                setingid:"",     //正在操作的数据id
                tableData: [              {id:"12345",type:"全部工单",nouveau:"吕珊珊",status:1,nowPeoson:"王振东",title:"1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库",time:"2019-09-21 09:12:20"},
                    {id:"12345",type:"sql",nouveau:"全部工单",status:2,nowPeoson:"王振东",title:"修改数据库",time:"2019-09-21"},],  //表格数据
                searchForm:{       //搜索条件
                    status:"",    //工单状态
                    name:"",    //提交人
                    title:"",  //工单标题
                    time:"",    //时间
                },
                soumettre: '',    //提交时间
                cur_page: 1,            //当前页
                isdele:false,     //删除提示框展示
                deletype:"",     //撤销或者删除   0,撤销  1删除
                allorder:[],     //全部工单工单
                pending:[],      //待处理工单
                history:[],       //历史工单
                pageNum:1,
                pageSize:10,
                total:100,

            }
        },
        props:['ordertype'],    //1,全部，2待处理，3处理记录
        computed: {
            // data() {
            //     return this.tableData.filter((d) => {
            //         let is_del = false;
            //         for (let i = 0; i < this.del_list.length; i++) {
            //             if (d.name === this.del_list[i].name) {
            //                 is_del = true;
            //                 break;
            //             }
            //         }
            //         if (!is_del) {
            //             if (d.address.indexOf(this.select_cate) > -1 &&
            //                 (d.name.indexOf(this.select_word) > -1 ||
            //                     d.address.indexOf(this.select_word) > -1)
            //             ) {
            //                 return d;
            //             }
            //         }
            //     })
            // }
        },
        methods: {
            //新增搜索工单
            search() {
               console.log(this.searchForm,"搜索条件");
                this.$axios({
                    url: "/wo/revoke",//请求路径
                    type: "post",//请求方式
                    params: {
                        workOrderId:this.setingid,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.tableData=res.data;
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },
            //查看详情,type   0,查看  1，审核，
            check(type,row) {
                this.$router.push({
                    path:"/orderdetail",
                    query:{
                        type:type,
                        data:row
                    }
                })
            },

            //新增或者修改
            edition(type,val){
                this.$router.push({
                    path:"/edition",
                    query:{
                        type:type,
                        val:val
                    }
                });
            },

            //撤销或者删除，弹出模态框，0，撤销，1，删除， 2，提交
            revoke(type, row) {
                this.deletype=type;
                this.setingid=row.id;
                this.isdele = true;
            },

            // 确定删除或者撤销或者提交
            deleteRow(){
                if( this.deletype==0){    //撤销或者删除   0,撤销  1删除   2，提交
                      this.revokeorder();
                 }else if( this.deletype==1){
                       this.deleorder();
                }else{
                     this.submitorder();
                }
                this.isdele = false;
            },

            //删除工单
            deleorder(){
                this.$axios({
                    url: "/wo/edit",//请求路径
                    type: "post",//请求方式
                    params: {
                        workOrderId:this.setingid,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("删除成功！");
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //撤销工单
            revokeorder(){
                this.$axios({
                    url: "/wo/revoke",//请求路径
                    type: "post",//请求方式
                    params: {
                        workOrderId:this.setingid,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("撤销成功！");
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //提交工单
            submitorder(){
                this.$axios({
                    url: "/wo/submit",//请求路径
                    type: "post",//请求方式
                    params: {
                        workOrderId:this.setingid,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("提交成功！");
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            // 分页导航,当前页改变
            handleCurrentChange(val) {
                this.pageNum = val;
            },

            // 移入样式
            hoverStyle({row, column, rowIndex, columnIndex}){
                if(columnIndex==0){
                    return 'point';
                }
            },

            // 跳转详情
            linkDetail(row,column,cell,event){
                console.log(row,"rowrow")
                if(column.label=='工单编号'){
                    this.$router.push({
                        path: "/orderDetail",
                        query:{
                            type:0,
                            data:row
                        }
                    });
                }
            },


            getStatus(val){
                 switch (val){
                     case "first":
                         // alert(1)
                         // this.getorderlist(0);
                         this.tableData=[              {id:"12345",type:"全部工单",nouveau:"吕珊珊",status:1,nowPeoson:"王振东",title:"1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库",time:"2019-09-21 09:12:20"},
                             {id:"12345",type:"sql",nouveau:"全部工单",status:2,nowPeoson:"王振东",title:"修改数据库",time:"2019-09-21"},]
                       console.log(this.tableData)
                         break;
                     case "second":
                         // alert(2)
                         // this.getorderlist(1);
                         this.tableData=[              {id:"12345",type:"待处理",nouveau:"吕珊珊",status:3,nowPeoson:"王振东",title:"1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库",time:"2019-09-21 09:12:20"},
                             {id:"12345",type:"待处理",nouveau:"珊珊",status:4,nowPeoson:"王振东",title:"修改数据库",time:"2019-09-21"},]
                         console.log(this.tableData)
                         window.a =  this.tableData
                         break;
                     case "third":
                         // this.getorderlist(2);

                         this.tableData=[              {id:"12345",type:"历史处理",nouveau:"吕珊珊",status:5,nowPeoson:"王振东",title:"1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库1修改数据库",time:"2019-09-21 09:12:20"},
                             {id:"12345",type:"历史处理",nouveau:"吕珊珊",status:2,nowPeoson:"王振东",title:"修改数据库",time:"2019-09-21"},]
                         console.log(this.tableData)
                         break;
                     default:{

                     }
                 }
            },

        },
        mounted(){
            // this.getorderlist(0);
            // this.getorderlist(1);
            // this.getorderlist(2);
        },
        updated(){
            console.log(this.tableData)
        }
    }

</script>

<style>
    .block .el-input__inner{
        width: 260px  !important;
    }
    td{
        height: 20px !important;
        font-size: 12px !important;
    }

    .point:hover>.cell{
        color:blue;
        text-decoration: underline;
        text-decoration-color:blue;
    }
    .point{
        cursor: pointer;
    }
    /* 表格状态的圆点 */
    .smallRound{
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        margin-right: 8px;
    }
    .smallRound1{
        background-color: rgba(217,217,217,1);
    }
    .smallRound2{
        background-color: rgb(26, 191, 197);
    }
    .smallRound3{
        background-color: rgba(80,197,26,1);
    }
    .smallRound4{
        background-color: rgb(197, 80, 26);
    }
    .smallRound5{
        background-color:rgba(245,166,35,1);
    }
    .smallRound6{
        background-color:#F5222D;
    }

</style>

<style scoped>
    .red{
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
    .del-dialog-cnt{
        font-size: 16px;
        text-align: center
    }
    .table{
        width: 100%;
        font-size: 14px;
    }

    .mr10{
        margin-right: 10px;
    }

</style>
