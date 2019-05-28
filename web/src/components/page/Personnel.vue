<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <!--<el-button type="primary" icon="delete" class="handle-del mr10" @click="delAll">批量删除</el-button>-->
                <el-select v-model="searchForm.groupId" placeholder="所在分组" class="handle-select mr10">
                    <el-option key="1" label="管理员组" value="1"></el-option>
                    <el-option key="2" label="开发1组" value="2"></el-option>
                    <el-option key="3" label="开发2组" value="3"></el-option>
                    <el-option key="4" label="测试组" value="4"></el-option>
                    <el-option key="5" label="产品组" value="5"></el-option>
                    <el-option key="6" label="APP组" value="6"></el-option>
                    <el-option key="7" label="WEB组" value="7"></el-option>
                    <el-option key="8" label="运维组" value="8"></el-option>
                    <el-option key="9" label="审核组" value="9"></el-option>
                    <el-option key="10" label="UI组" value="10"></el-option>
                </el-select>
                <el-input v-model="searchForm.realname" placeholder="姓名" class="handle-input mr10"></el-input>
                <el-input v-model="searchForm.username" placeholder="账号" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="getUserList">搜索</el-button>
                <el-button type="primary" icon="search" @click="edit(0,0)"  v-if="userType==1">新增</el-button>
                <el-button type="primary" icon="search" @click="empty"  plain>重置</el-button>
            </div>
            <el-table
                    :data="person"
                    border class="table"
                    ref="multipleTable"
                    :header-cell-style="{background:'#ecf5ff'}"
                    @selection-change="handleSelectionChange"

            >
                <el-table-column prop="realname" label="姓名"  min-width="80"     align="center" >
                </el-table-column>
                <el-table-column prop="username" label="账号" sortable min-width="150"     align="center">
                </el-table-column>
                <el-table-column prop="group" label="分组"  min-width="80"    align="center">
                </el-table-column>
                <el-table-column prop="mobile" label="电话"  min-width="100"     align="center">
                </el-table-column>
                <el-table-column prop="email" label="邮箱"  min-width="220"     align="center" >
                </el-table-column>
                <el-table-column prop="type" label="角色"   min-width="100"     align="center" >
                    <template slot-scope="scope">
                        <span v-if="scope.row.type==1?'true':''">管理员</span>
                        <span v-if="scope.row.type==2?'true':''">普通用户</span>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态"   min-width="100"      align="center">
                    <template slot-scope="scope">
                        <span v-if="scope.row.status==1?'true':''"><span class="smallRound smallRound3"></span>正常</span>
                        <span v-if="scope.row.status==2?'true':''"><span class="smallRound smallRound1"></span>禁用</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作"   align="center"  min-width="200">
                    <template slot-scope="scope">
                        <!--<el-button-->
                                <!--type="text"-->
                                <!--icon="el-icon-search"-->
                                <!--v-if="scope.row.status!=2"-->
                                <!--@click="edit(2, scope.row)"-->
                                  <!--&gt;查看</el-button>-->
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="edit(1, scope.row)"
                                v-if="scope.row.status==1&&userType==1">编辑</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                v-if="scope.row.status==1&&userType==1"
                                @click="handleDelete(0, scope.row)"
                        >禁用</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-refresh"
                                @click="handleDelete(1, scope.row)"
                                v-if="scope.row.status==1&&userType==1">重置密码</el-button>
                        <el-button type="text"    v-if="scope.row.status==2"  disabled="">已禁用</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        @current-change="pageChange"
                        layout="prev, pager, next"
                        :current-page=pageNum
                        :page-size=pageSize
                        :total="totalCount"
                >
                </el-pagination>
            </div>
        </div>

        <!-- 编辑新增弹出框 -->
        <div  v-if="ishandle">
            <el-dialog  :title="handleType==0?'新增':'编辑'" :visible.sync="ishandle" width="30%"   >
                <el-form ref="handle" :model="handleData" label-width="90px"    :rules="rules">
                    <el-form-item label="姓名"  prop="realname">
                        <el-input v-model="handleData.realname" > </el-input>
                    </el-form-item>
                    <el-form-item label="账号"   prop="username">
                        <el-input v-model="handleData.username"  disabled  v-if="handleType==1"> </el-input>
                        <el-input v-model="handleData.username"    v-else> </el-input>
                    </el-form-item>
                    <el-form-item label="分组"  prop="groupId">
                        <el-select v-model="handleData.groupId"   class="handle-select mr10"  style="width: 40%">
                            <el-option key="1" label="管理员组" value="1"></el-option>
                            <el-option key="2" label="开发1组" value="2"></el-option>
                            <el-option key="3" label="开发2组" value="3"></el-option>
                            <el-option key="4" label="测试组" value="4"></el-option>
                            <el-option key="5" label="产品组" value="5"></el-option>
                            <el-option key="6" label="APP组" value="6"></el-option>
                            <el-option key="7" label="WEB组" value="7"></el-option>
                            <el-option key="8" label="运维组" value="8"></el-option>
                            <el-option key="9" label="审核组" value="9"></el-option>
                            <el-option key="10" label="UI组" value="10"></el-option>
                        </el-select>

                        <el-checkbox  v-model="handleData.isGroupLeader"  style="width: 40%">是否设为负责人？</el-checkbox>
                    </el-form-item>
                    <el-form-item label="角色"  prop="type">
                        <el-select v-model="handleData.type"  class="handle-select mr10"  style="width: 100%">
                            <el-option key="1" label="管理员" value="1"></el-option>
                            <el-option key="2" label="普通用户" value="2"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="电话"  prop="mobile">
                        <el-input v-model="handleData.mobile"  > </el-input>
                    </el-form-item>
                    <el-form-item label="邮箱"  prop="email">
                        <el-input v-model="handleData.email"  > </el-input>
                    </el-form-item>

                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="cancleHandle('handle')">取 消</el-button>
                <el-button type="primary" @click="saveEdit('handle')">确 定</el-button>
            </span>
            </el-dialog>
        </div>


        <!-- 禁用和重置密码提示框 -->
        <el-dialog title="提示" :visible.sync="isdele" width="300px" center>
            <div class="del-dialog-cnt"  v-if="setType==0">是否确定禁用  < {{delename}} >？</div>
            <div class="del-dialog-cnt"  v-else>是否确定重置  < {{delename}} >的密码？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="isdele = false">取 消</el-button>
                <el-button type="primary" @click="deleteType">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import Vue from 'vue'
    export default {
        name: 'personnel',
        data() {


            //验证账号重复性
            var removal = (rule, value, callback) => {
                var ishave=this.getUserByName(value);
                if(ishave){
                    callback(new Error('账号已存在'));
                }else{
                    callback();
                }
            };

            //正则验证电话号码
            var checkPhone = (rule, value, callback) => {
                 var  phoneReg =/^1[34578]\d{9}$/;
                if(!phoneReg .test(value)){
                    callback(new Error('请输入正确的手机号'));
                }else{
                    callback();
                }
            };

            //正则验证邮箱
            var checkEmail = (rule, value, callback) => {
                var mailReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                if(!mailReg.test(value)){
                    callback(new Error('请输入正确的邮箱'));
                }else{
                    callback();
                }
            };
            return {
                userType:"",
                delename:"",               //重置密码和禁用人名字
                setType:"",    //重置或者是禁用
                totalCount:0,        //总条数
                pageSize:10,          //每页多少条
                pageNum:1,       //当前第几页
                ishandle:false,    //编辑新增模态框
                isdele:false,   //删除模态框
                setingid:"",     //正在操作的数据id
                searchForm:{       //搜索条件
                    groupId: "",    //分组id
                    realname:"",    //真是姓名
                    username:"",      //用户名
                },
                handleType:"",     //判断是新增还是编辑   0，新增，1编辑 , 2查看
                handleData:{               //新增编辑数据
                    realname:"",
                    username:"",
                    groupId:"",
                    type:"",
                    mobile:".",
                    email:"",
                    isGroupLeader:false,
                },     //新增编辑数据
                person:[ ],     //列表数据

                //标单验证不为空
                rules: {
                    realname:[
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                     ],
                    username:[
                        { required: true,validator: removal, trigger: 'blur'},
                    ],
                    groupId: [
                        { required: true, message: '请选择分组', trigger: 'change' }
                    ],
                    type: [
                        { required: true, message: '请选择角色', trigger: 'change' }
                    ],
                    mobile:[
                        { required: true,validator: checkPhone, trigger: 'blur'},
                    ],
                    email:[
                        { required: true,validator: checkEmail, trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {

            //查询用户列表，或搜索
            getUserList() {
                this.$axios({
                    url: "/user/list",//请求路径
                    type: "POST",//请求方式
                    params: {
                        groupId: this.searchForm.groupId,    //分组id
                        realName:this.searchForm.realname,    //真是姓名
                        userName:this.searchForm.username,      //用户名
                        pageNum:this.pageNum,
                        pageSize:this.pageSize,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.person=res.data.items;
                        this.totalCount=res.data.totalCount;
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //根据用户名查询用户
            getUserByName(name){
                this.$axios({
                    url: "/user/getUserByName",//请求路径
                    type: "get",//请求方式
                    params: {
                        username:name,
                    },
                }).then((res) => {
                    if(res.code==0){
                       return  true;
                    }else{
                        return  false;
                    }
                })
            },


            //重置查询条件
            empty(){
                   this.searchForm.groupId="";    //分组id
                    this.searchForm.realname="";  //真是姓名
                    this.searchForm.username="";     //用户名
            },

            // 分页导航,当前页改变
            pageChange(val) {
                this.pageNum = val;
                this.getUserList();
            },

            //新增或者编辑   type: 0, 新增  1，编辑,  2 查看
            edit(type,row){
                this.handleType=type;
                if(type==1){
                    this.setingid=row.id;
                    this.handleData=row;
                    this.getUserinfo(this.setingid);
                }else{
                    this.handleData={ };
                    this.handleData.isGroupLeader=false;
                }
                this.ishandle=true;

            },

            //编辑，查询原始信息
             getUserinfo(id){
                 this.$axios({
                     url: "/user/editInfo",//请求路径
                     type: "get",//请求方式
                     params: {
                         userId:id,
                     },
                 }).then((res) => {
                     if(res.code==0){
                         res.data.type=String(res.data.type);
                         res.data.groupId=String(res.data.groupId);
                         this.handleData=res.data;
                     }else{
                         this.$message.warning(res.msg);
                     }
                 })
             },

            //编辑用户
            editUser(){
                this.$axios({
                    url: "/user/edit",//请求路径
                    type: "post",//请求方式
                    data: {
                        id:this.setingid,
                        username:this.handleData.username,
                        realname:this.handleData.realname,
                        mobile:this.handleData.mobile,
                        groupId:this.handleData.groupId,
                        email:this.handleData.email,
                        type:this.handleData.type,
                        isGroupLeader:this.handleData.isGroupLeader,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("编辑成功！");
                        this.ishandle = false;
                        this.getUserList();
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
             },

            //新增用户
            addUser(){
                this.$axios({
                    url: "/user/create",//请求路径
                    type: "post",//请求方式
                    data: {
                        username:this.handleData.username,
                        realname:this.handleData.realname,
                        mobile:this.handleData.mobile,
                        groupId:this.handleData.groupId,
                        email:this.handleData.email,
                        type:this.handleData.type,
                        isGroupLeader:this.handleData.isGroupLeader,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("新增成功！");
                        this.ishandle = false;
                        this.getUserList();
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //取消
            cancleHandle(el){
                this.ishandle = false;
            },

            // 保存编辑或者新增
            saveEdit(el) {
                this.$refs[el].validate((valid) => {
                    if (valid) {
                        if(this.handleType==1){
                            this.editUser();
                        }else{
                            this.addUser();
                        }
                    } else {
                        return false;
                    }
                });
                console.log( this.handleData)
            },

            //禁用或者重置密码   0,禁用， 1重置
            handleDelete(index, row) {
                this.setType=index;
                this.isdele = true;            //弹框显示
                this.delename=row.realname;    //禁用人名字
                this.setingid=row.id;            //当前操作人id
            },

            // 确定禁用或者重置密码类型
            deleteType(){
                if(this.setType==0){
                    this.disabling();
                }else{
                    this.resetPass();
                }
            },

            //禁用
            disabling(){
                this.$axios({
                    url: "/user/disable",//请求路径
                    type: "POST",//请求方式
                    params: {
                        userId:this.setingid,
                    },
                }).then((res) => {
                    this.isdele = false;
                    if(res.code==0){
                        this.getUserList();
                        this.$message.success('已成功禁用该用户！');
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //重置密码
            resetPass(){
                this.$axios({
                    url: "/user/reset/pwd",//请求路径
                    type: "POST",//请求方式
                    params: {
                        userId:this.setingid,
                    },
                }).then((res) => {
                    this.isdele = false;
                    if(res.code==0){
                        this.$message.success('密码重置成功！');
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },


            handleSelectionChange(val) {
                this.multipleSelection = val;
            },

        //检查是否登录
            islogin(){
                if(localStorage.getItem('userinfo')) {
                    var userinfo =JSON.parse(localStorage.getItem('userinfo'))
                    console.log(userinfo)
                    sessionStorage.username=userinfo.username;
                    sessionStorage.userId=userinfo.userId;
                    this.userType=userinfo.type;   //用户类型 :1 管理员 2 普通用户
                    this.getUserList();
                }else{
                    this.$router.push("/login");
                }
            }
        },
        mounted(){
            this.islogin();
            // this.getUserByName();
        }
    }

</script>

<style scoped>
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
    .smallRound3{
        background-color: rgba(80,197,26,1);
    }
    .handle-box {
        margin-bottom: 20px;
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
    .red{
        color: #ff0000;
    }
    .mr10{
        margin-right: 10px;
    }
</style>
