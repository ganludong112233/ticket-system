<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">工单管理系统</div>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input
                            v-model="ruleForm.username"
                            placeholder="username"
                            @keyup.enter.native="submitForm('ruleForm')"
                            @keyup.tab.native="submitForm('ruleForm')"
                            ref="username"
                            autofocus="autofocus"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-people"       tabindex="-1"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                            type="password"
                            placeholder="password"
                            v-model="ruleForm.password"
                            @keyup.enter.native="submitForm('ruleForm')"
                            @keyup.tab.native="submitForm('ruleForm')"
                            autocomplete="new-password"
                            ref="password"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"       tabindex="-1"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm('ruleForm')"       tabindex="-1">LOGIN    IN</el-button>
                </div>
                <p class="login-tips">Tips : 自己的密码打死不能告诉别人噢。</p>
            </el-form>
        </div>
    </div>
</template>

<script>

    export default {
        data: function(){
            return {
                ruleForm: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            //点击enter和tab获取光标，并验证空值
            submitForm(formName) {
                if(this.ruleForm.username==""){
                      this.$refs.username.focus();
                 }else if(this.ruleForm.password==""){
                    this.$refs.password.focus();
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.login();
                    } else {
                        return false;
                    }
                });
            },

            //登录
            login(){
                this.$axios({
                    url: "/login",//请求路径
                    type: "POST",//请求方式
                    params: {
                        username:this.ruleForm.username,
                        password:this.ruleForm.password
                    }, //请求的附加参数，用json对象
                }).then((res) => {
                    if (res.code == 0) {
                        var data=JSON.stringify(res.data);
                        localStorage.setItem('userinfo',data);    //本地存信息
                        this.$router.push('/dashboard');
                    }else{
                        this.$message.warning(res.msg);
                    }
                })
            },
        },

    }
</script>

<style scoped>
    .login-wrap{
        position: relative;
        width:100%;
        height:100%;
        background-image: url(../../assets/img/login-bg.jpg);
        background-size: 100%;
    }
    .ms-title{
        width:100%;
        line-height: 50px;
        text-align: center;
        font-size:20px;
        color: #409eff;
        border-bottom: 1px solid #ddd;
    }
    .ms-login{
        position: absolute;
        left:50%;
        top:50%;
        width:350px;
        margin:-190px 0 0 -175px;
        border-radius: 5px;
        background: rgba(255,255,255, 0.3);
        overflow: hidden;
    }
    .ms-content{
        padding: 30px 30px;
    }
    .login-btn{
        text-align: center;
    }
    .login-btn button{
        width:100%;
        height:36px;
        margin-bottom: 10px;
    }
    .login-tips{
        font-size:12px;
        line-height:30px;
        color:#fff;
    }
</style>