<template>
    <div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="edition" label-width="80px"  :rules="rules">
                    <el-form-item label="标题"  prop="title"    class="titleHeight">
                        <el-input type="textarea"  resize="none"     rows="1"  v-model="edition.title"  placeholder="50字以内"   minlength="5"   maxlength="50" ></el-input>
                    </el-form-item>

                    <div style="display: flex;justify-content:space-between">
                        <el-form-item label="类型"  prop="type"  style="width: 50%">
                            <el-select v-model="edition.type" placeholder="请选择发版类型">
                                <el-option key="1" label=" 版本迭代 " value="1"></el-option>
                                <el-option key="2" label="bugfix" value="2"></el-option>
                                <el-option key="3" label="代码优化" value="3"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item  label="版本号"  prop="version"  style="width: 50%">
                            <el-input type="text"  v-model="edition.version"></el-input>
                        </el-form-item>
                    </div>

                    <el-form-item label="项目"  prop="projects">
                        <el-checkbox-group v-model="edition.projects">
                            <el-checkbox  :label="item.id"  :value="item.id"  v-for="(item,index)  in  project">{{item.name}}</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                    <el-form-item label="内容"  prop="content">
                        <el-input type="textarea" rows="7" v-model="edition.content"   :autosize="{ minRows: 4}"></el-input>
                    </el-form-item>
                    <el-form-item label="备注"    prop="comment">
                        <el-input
                                type="textarea"
                                resize="none"
                                v-model="edition.comment"
                                placeholder="250字以内"
                                maxlength="250"
                                :autosize="{ minRows: 1}"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                            :label="index==0?'步骤':''"
                            v-for="(item, index) in edition.deploySteps"
                            :prop="'deploySteps.' + index + '.content'"
                            :rules="{
                                       required: true, message: '步骤不能为空', trigger: 'blur'
                             }"
                    >
                        <div  class="stepBox">
                            <span  class="stepNum">{{index+1}}</span>
                            <el-select v-model="item.type" placeholder="类型"  style="width: 20%">
                                <el-option  key="1"  label="SQL " value="1"></el-option>
                                <el-option  key="2"  label="Nginx " value="2"></el-option>
                                <el-option  key="3"  label="Apollo  " value="3"></el-option>
                                <el-option  key="4"  label="deploy  " value="4"></el-option>
                                <el-option  key="5"  label="other  " value="5"></el-option>
                            </el-select>
                            <el-input type="textarea" rows="2"
                                      v-model="item.content"
                                      :autosize="{minRows:2}"
                                      style="width: 75%;margin:0 1%"></el-input>
                            <div @click='adddep()'  class="stepAdd"  v-if="index==0">+</div>
                            <div @click='reducedep(item)'  class="stepAdd"  v-else>-</div>
                        </div>
                    </el-form-item>

                    <el-form-item  style="margin-top: 6%">
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
                flag:true,
                allproject:{},
                project:[],   //全部项目
                edition: {
                    id:"",
                    title:"",    //工单标题，
                    type:"",   //工单类型，
                    content:"",   //内容
                    comment:"",   //补充备注
                    version:"",   //版本
                    projects:[],//项目
                    deploySteps:[       //步骤
                        {
                            type: '',
                            content: ''
                        },
                    ],
                },

                //标单验证不为空
                rules: {
                    type: [
                        { required: true, message: '请选择发版类型', trigger: 'change' }
                    ],
                    title: [
                        { required: true, message: '请输入发版标题', trigger: 'blur' },
                        { min: 5, max: 50, message: '长度5到50字以内', trigger: 'blur' }
                    ],
                    version: [
                        { required: true, message: '请输入版本号', trigger: 'blur' },
                    ],
                    comment:[
                        {max: 250, message: '250字以内', trigger: 'blur' }
                    ],
                    content:[
                        { required: true, message: '请输入内容', trigger: 'blur' },
                    ],
                    projects:[
                        { type: 'array', required: true, message: '请至少选择一个项目', trigger: 'change' }
                    ],
                }
            }
        },
        methods: {
            //查询所有项目
            getProject(callback){
                this.$axios({
                    url: "/project/query?parentId=1",//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    for(var i=0;i<res.length;i++){
                        this.allproject[res[i].id]=res[i];
                    }
                    this.project=res;
                    callback();
                })
            },

            //新增步骤
            adddep(){
                this.edition.deploySteps.push({
                    type: '',
                    content: ''
                })
            },

            //删除步骤
            reducedep(item){
                var index = this.edition.deploySteps.indexOf(item)
                if (index !== -1) {
                    this.edition.deploySteps.splice(index, 1)
                }
            },

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
                    url: "/deploy/editInfo?deployId="+this.edition.deployId,//请求路径
                    type: "get",//请求方式
                }).then((res) => {
                    if(res.code==0){
                        res.data.type=String( res.data.type);
                        for(var i=0;i<res.data.deploySteps.length;i++){
                            res.data.deploySteps[i].type=String(res.data.deploySteps[i].type);
                        }
                        var temp=[]
                        for(var j=0;j<res.data.projects.length;j++){
                            temp.push(res.data.projects[j].id)
                        }
                        res.data.projects=temp;
                        this.edition=res.data;
                        // console.log(this.edition,"this.edition")
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },

            //返回提示
            annuler(){
                this.$confirm('此次发版内容暂未保存, 是否确认返回?', '提示', {
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
                var temp=[];
                for(var i=0;i<this.edition.projects.length;i++){
                    temp.push(this.allproject[this.edition.projects[i]])
                }
                this.$axios({
                    url: "/deploy/create",//请求路径
                    type: "post",//请求方式
                    data: {
                        title: this.edition.title,
                        type: this.edition.type,
                        version: this.edition.version,
                        comment: this.edition.comment,
                        content: this.edition.content,
                        deploySteps:this.edition.deploySteps,
                        projects:temp,
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
                 var temp=[];
                 for(var i=0;i<this.edition.projects.length;i++){
                     temp.push(this.allproject[this.edition.projects[i]])
                 }
                this.$axios({
                    url: "/deploy/edit",//请求路径
                    type: "post",//请求方式
                    data: {
                        id: this.edition.id,
                        title: this.edition.title,
                        type: this.edition.type,
                        version: this.edition.version,
                        comment: this.edition.comment,
                        content: this.edition.content,
                        deploySteps:this.edition.deploySteps,
                        projects:temp,
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
            var self=this;
            this.type=this.$route.query.type;    //0为新增，1为修改；
            this.edition.deployId=this.$route.query.id;    //0为新增，1为修改；
            this.getProject(function(){
                if(self.type==1){
                    self.getoldData();     //获取修改原始数据
                }
            });    //获取全部数量


        }
    }
</script>

<style>
    .stepNum{
        width: 20px;
        height: 19px;
        background-color: #67c23a;
        border-radius: 20px;
        line-height: 20px;
        color: white;
        text-align: center;
        margin: 5px  5px;
        font-size: 12px;
    }
    .stepBox{
        display: flex;
        justify-content: space-between;
        /*margin-bottom: 10px;*/
    }
    .stepAdd{
        width: 20px;
        height: 20px;
        /* background: #999; */
        border: 2px solid #B4B4B4;
        border-radius: 50%;
        line-height: 20px;
        text-align: center;
        margin: auto;
        /*margin-left: 10px;*/
        font-size: 24px;
        color: #B4B4B4;
        cursor: pointer;
    }
    .el-form-item__content   .textHeight   input{
        height: 50px !important;
    }
    .el-form-item__content   .titleHeight   input{
        height: 50px !important;
    }
</style>