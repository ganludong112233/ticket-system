<template>
    <div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="workorder" label-width="80px"  :rules="rules">
                    <el-form-item label="类型"  prop="type">
                        <el-select v-model="workorder.type" placeholder="请选择工单类型">
                            <el-option key="1" label=" SQL " value="1"></el-option>
                            <el-option key="2" label="Apollo" value="2"></el-option>
                            <el-option key="3" label="Nginx" value="3"></el-option>
                            <el-option key="4" label="其它" value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="标题"  prop="title"    class="titleHeight">
                        <el-input type="textarea"  resize="none"     rows="1"  v-model="workorder.title"  placeholder="50字以内"   minlength="5"   maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="描述"  prop="description">
                        <el-input
                                type="textarea"
                                resize="none"
                                v-model="workorder.description"
                                placeholder="250字以内"
                                minlength="5"
                                maxlength="250"
                                :autosize="{ minRows: 2, maxRows: 4}"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="内容"  prop="content">
                        <el-input type="textarea" rows="7" v-model="workorder.content"   :autosize="{ minRows: 4}"></el-input>
                    </el-form-item>
                    <el-form-item label="备注"    prop="comment">
                        <el-input
                                type="textarea"
                                resize="none"
                                v-model="workorder.comment"
                                placeholder="250字以内"
                                minlength="5"
                                maxlength="250"
                                :autosize="{ minRows: 2, maxRows: 4}"
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit('form')">保存</el-button>
                        <el-button  @click="annuler">取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'edition',
        data(){
            return {
                id:"",       //编辑工单id
                workorder: {
                    title:"",    //工单标题，
                    type:"",   //工单类型，
                    description:"",   //工单说明
                    content:"",   //内容
                    comment:"",   //补充备注
                },

                //标单验证不为空
                rules: {
                    title: [
                        { required: true, message: '请输入工单标题', trigger: 'blur' },
                        { min: 5, max: 50, message: '长度5到50字以内', trigger: 'blur' }
                    ],
                    description: [
                        { required: true, message: '请输入描述', trigger: 'blur' },
                        { min: 5, max: 250, message: '长度5到250字以内', trigger: 'blur' }
                    ],
                    comment:[
                        { min: 5, max: 250, message: '长度5到250字以内', trigger: 'blur' }
                    ],
                    content:[
                        { required: true, message: '请输入内容', trigger: 'blur' },
                    ],
                    type: [
                        { required: true, message: '请选择工单类型', trigger: 'change' }
                    ],
                }
            }
        },
        methods: {

            //提交 新增或者修改
            onSubmit(name) {
                // this.type=this.$route.query.type;    //0为新增，1为修改；
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if(this.type==0){
                            this.addorder();
                        }else{
                            this.editorder();
                        }
                    } else {
                        return false;
                    }
                });
            },

            //修改获取原始数据
            getoldData(){
                this.$axios({
                    url: "/wo/editInfo?workOrderId="+this.workorder.workOrderId,//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if(res.code==0){
                        res.data.type=String( res.data.type);
                        this.workorder=res.data;
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //返回提示
            annuler(){
                this.$confirm('此次工单内容暂未保存, 是否确认返回?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$router.go(-1);
                }).catch(() => {

                });
            },

            //新增工单
            addorder(){
                this.$axios({
                    url: "/wo/create",//请求路径
                    type: "post",//请求方式
                    data: {
                        title: this.workorder.title,
                        type: this.workorder.type,
                        description: this.workorder.description,
                        comment: this.workorder.comment,
                        content: this.workorder.content,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("新增成功！");
                        this.$router.go(-1);
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //修改工单
            editorder(){
                this.$axios({
                    url: "/wo/edit",//请求路径
                    type: "post",//请求方式
                    data: {
                        id:this.workorder.id,
                        title: this.workorder.title,
                        type: this.workorder.type,
                        description: this.workorder.description,
                        comment: this.workorder.comment,
                        content: this.workorder.content,
                    },
                }).then((res) => {
                    if(res.code==0){
                        this.$message.success("修改成功！");
                        this.$router.go(-1);
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            }
        },
        mounted(){
            this.type=this.$route.query.type;    //0为新增，1为修改；
            this.workorder.workOrderId=this.$route.query.id;    //0为新增，1为修改；
            if(this.type==1){
                this.getoldData();
            }
        }
    }
</script>

<style>
    .el-form-item__content   .textHeight   input{
        height: 50px !important;
    }
    .el-form-item__content   .titleHeight   input{
        height: 50px !important;
    }
</style>