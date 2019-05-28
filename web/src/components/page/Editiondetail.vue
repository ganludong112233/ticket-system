<template>
    <div  id="detile">
        <div class="container">
            <!--<el-steps :active="2" align-center  style="margin-bottom: 20px"  finish-status="success">-->
            <!--<el-step title="新建" description="( 张三 )"></el-step>-->
            <!--<el-step title="提交审核" description="( 张三 )"></el-step>-->
            <!--<el-step title="项目负责人" description="( 王振东 )"></el-step>-->
            <!--<el-step title="运维" description="( 刘智 )"></el-step>-->
            <!--<el-step title="完成" description="( 刘毅 )"></el-step>-->
            <!--</el-steps>-->


            <div class="form-box">
                <el-form ref="form" :model="detail" label-width="80px">
                    <el-form-item label="类型：" >
                        <p class="textnote"  v-if="detail.type==1">版本迭代 </p>
                        <p class="textnote"  v-if="detail.type==2">bugfix</p>
                        <p class="textnote"  v-if="detail.type==3">代码优化</p>
                    </el-form-item>
                    <el-form-item label="版本：" >
                        <p class="textnote">{{detail.version}}</p>
                    </el-form-item>
                    <el-form-item label="项目："  class="textnote" >
                        <span class=""  v-for="item in detail.projects">【{{item.name}}】</span>
                    </el-form-item>
                    <el-form-item label="标题：">
                        <p class="textnote">{{detail.title}}</p>
                    </el-form-item>

                    <el-form-item label="内容：">
                        <!--<el-input type="textarea" rows="7"  v-model="detail.content"   readonly></el-input>-->
                        <pre class="contentDes">{{detail.content}}</pre>
                    </el-form-item>
                    <el-form-item label="备注：" >
                        <p class="textnote">{{detail.comment||"暂无"}}</p>
                    </el-form-item>
                    <el-form-item label="步骤：" >
                        <p  v-for="(item,index) in detail.deploySteps"  class="textnote">
                            {{index+1}}：
                            <span class=""   v-if="item.type==1">类型：【SQL】 ------</span>
                            <span class=""   v-if="item.type==2">类型：【Nginx】 ------</span>
                            <span class=""   v-if="item.type==3">类型：【Apollo】------</span>
                            <span class=""   v-if="item.type==4">类型：【deploy】 ------</span>
                            <span class=""   v-if="item.type==5">类型：【other】 ------</span>
                            <span class="" >内容： {{item.content}}</span>
                        </p>

                    </el-form-item>


                </el-form>
            </div>

            <!--历史记录-->
            <div class="history">
                <p>历史操作记录：</p>
                <el-table
                        :data="historyList"
                        border
                        :header-cell-style="{background:'#ecf5ff'}"
                        style="width: 60%">
                    <el-table-column
                            prop="processor"
                            label="操作人"
                            align="center"
                            min-width="80">
                    </el-table-column>
                    <el-table-column
                            prop="op"
                            label="操作"
                            min-width="80"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="comment"
                            label="操作内容"
                            min-width="180"
                            >
                    </el-table-column>

                    <el-table-column
                            prop="processTime"
                            label="处理时间"
                            :formatter="formatTime"
                            min-width="100"
                    >
                    </el-table-column>
                </el-table>
            </div>

            <!--查看-->
            <div class="backbtn"  v-if="type==0">
                <el-button type="primary" @click="back"  size="medium ">返回</el-button>
            </div>

            <!--撤销-->
            <div class="backbtn"   v-if="type==1">
                <el-button type="primary" @click="adopt">同意</el-button>
                <el-button  @click="refuse">拒绝</el-button>
            </div>
        </div>

        <!--同意或者拒绝模态框-->
        <div  v-if="handle">
            <el-dialog
                    :title="handleType==0?'备注':'拒绝理由'"
                    :visible.sync="handle"
            >
                <!--拒绝理由 必填-->
                <el-form  ref="cancle"  :model="handleForm"   :rules="rules"  v-if="handleType==1">
                    <el-form-item label=""   prop="note">
                        <el-input type="textarea"  v-model="handleForm.note"  rows="4"   placeholder="必填"></el-input>
                    </el-form-item>
                </el-form>
                <el-input type="textarea"  v-model="handleForm.note"  rows="4"  v-else   placeholder="非必填"></el-input>

                <div slot="footer" class="dialog-footer">
                    <el-button @click="handle = false">取 消</el-button>
                    <el-button type="primary" @click="completed('cancle')">确 定</el-button>
                </div>
            </el-dialog>
        </div>

    </div>
</template>
<style>
    #detile .form-box{
        width: auto;
    }
    #detile  .contentDes{
        border: 1px solid #e5e5e5;
        box-shadow: 1px 1px 4px  1px rgba(128, 128, 128, 0.28);
        /*box-sizing: content-box;*/
        overflow: visible;
        display: inline-block;
        border-radius: 5px;
        padding: 1%;
        line-height: 15px;

    }
    .textnote{
        font-size: 14px;
        color: #999;
    }
    .backbtn{
        text-align: center;
        padding: 3% 0;
    }
    .history{
        width: 95%;
        margin: 0  auto;
    }
    .history  p{
        color: #606266;
        font-size: 14px;
        margin-bottom: 10px;
    }
</style>

<script>
    export default {
        name: 'orderdetail',
        data: function(){
            return {
                historyList:[],    //历史记录
                handle:false,       //处理模态框  同意或者拒绝
                id:"",
                type:"",   //操作类型， 0查看 1撤销
                handleForm:{
                    note:"",      //备注或者拒绝理由
                },
                rules:{
                    note: [
                        { required: true, message: '请输入拒绝理由', trigger: 'blur' },
                        { min: 1, max: 50, message: '长度1到250字以内', trigger: 'blur' }
                    ],
                },
                handleType:"",     //操作类型

                detail: {},    //详情数据
            }
        },
        methods: {
            //表格时间转换
            formatTime(time) {
                return this.$m.newTime(time.processTime, 2)
            },
            //返回
            back() {
                this.$router.go(-1);
            },

            //通过审核
            adopt(){
                this.handleType=0;
                this.handle=true;
            },

            //拒绝理由
            refuse(){
                this.handleType=1;
                this.handle=true;
            },

            //点击确认
            completed(val){
                if(this.handleType==0){
                    this.completetask();
                    this.handle=false;
                }else{
                    this.$refs[val].validate((valid) => {
                        if (valid) {
                            this.completetask();
                            this.handle=false;
                        } else {
                            return false;
                        }
                    });
                }
            },

            //处理意见
            completetask(){
                var rerult=this.handleType==0?true:false;      //同意：true 拒绝：false
                this.$axios({
                    url: "/deploy/completeTask",//请求路径
                    type: "post",//请求方式
                    params: {
                        deployId:this.detail.id,
                        result:rerult,
                        comment:this.handleForm.note,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("处理成功！");
                        this.handleForm.note="";
                        this.$router.go(-1);
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },


            //查看详情数据
            orderinfo(){
                this.$axios({
                    url: "/deploy/getInfoById",//请求路径
                    type: "get",//请求方式
                    params: {
                        deployId:this.id,
                    },
                }).then((res) => {
                    if(res.code==0){
                        // res.data.type=String(res.data.type);
                        this.detail=res.data;
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //发版申请处理日志接口
            getOpLogs(){
                this.$axios({
                    url: "/deploy/getOpLogs",//请求路径
                    type: "get",//请求方式
                    params: {
                        deployId:this.id,
                    },
                }).then((res) => {
                    if(res.code==0){
                        // res.data.type=String(res.data.type);
                        this.historyList=res.data;
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },



        },
        mounted(){
            this.id=this.$route.query.id;    //详情数据
            this.type=this.$route.query.type;       //查看详情 ,type   0,查看  1，审核，
            this.orderinfo();
            this.getOpLogs();
        }
    }
</script>