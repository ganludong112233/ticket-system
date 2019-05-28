<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
        <i class="el-icon-menu"></i>
        </div>
        <div class="logo">工单管理系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>
                <!-- 消息中心 -->
                <!--<div class="btn-bell">-->
                <!--<el-tooltip effect="dark" :content="message?`有${message}条未读消息`:`消息中心`" placement="bottom">-->
                <!--<router-link to="/tabs">-->
                <!--<i class="el-icon-bell"></i>-->
                <!--</router-link>-->
                <!--</el-tooltip>-->
                <!--<span class="btn-bell-badge" v-if="message"></span>-->
                <!--</div>-->
                <!-- 用户头像 -->
                <div class="user-avator"><img src="../../assets/photo.jpg"></div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}} <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <!--<a href="http://blog.gdfengshuo.com/about/" target="_blank">-->
                        <!--<el-dropdown-item>关于作者</el-dropdown-item>-->
                        <!--</a>-->
                        <!--<a href="https://github.com/lin-xin/vue-manage-system" target="_blank">-->
                        <!--<el-dropdown-item>项目仓库</el-dropdown-item>-->
                        <!--</a>-->
                        <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                        <el-dropdown-item divided command="resetpass">修改密码</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>


        <!--修改密码框-->
        <div v-if="changepass">
            <el-dialog title="修改密码" :visible.sync="changepass">
                <el-form :model="form"    ref="form" :rules="rules" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="原始密码" prop="oldPwd">
                        <el-input v-model="form.oldPwd" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="新的密码" prop="newPwd">
                        <el-input type="password" v-model="form.newPwd" ></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="password">
                        <el-input type="password" v-model="form.password"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="submission('form')">提交</el-button>
                        <el-button @click="resetForm('form')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </div>

    </div>
</template>
<script>
    import bus from '../common/bus';

    export default {
        data() {
            //检查原始密码不为空，且不为中文
            var checkOldpass = (rule, value, callback) => {
                var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
                if (!value) {
                    return callback(new Error('原始密码不能为空'));
                }else {
                    callback();
                }
                if(reg.test(value)){
                    return callback(new Error('请输入正确的密码'));
                }else{
                    callback();
                }
            };

            //检查新密码 ，不为空
            var validatePass = (rule, value, callback) => {
                if (value === ''||value==undefined) {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.form.password !== '') {
                        this.$refs.form.validateField('password');
                        callback();
                    }else{
                        callback();
                    }
                }
            };

            //确认密码
            var validatePass2 = (rule, value, callback) => {
                if (value === ''||value==undefined) {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.form.newPwd) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                collapse: false,
                fullscreen: false,
                name: 'linxin',
                message: 2,
                changepass: false,
                form: {
                    oldPwd: "",
                    newPwd: "",
                    password: "",
                },
                //标单验证不为空
                rules: {
                    newPwd: [
                        {  required: true, validator: validatePass, trigger: 'blur' }
                    ],
                    password: [
                        { required: true, validator: validatePass2, trigger: 'blur' }
                    ],
                    oldPwd: [
                        { required: true,  validator: checkOldpass, trigger: 'blur' }
                    ]
                }
            }
        },
        computed: {
            username() {
                let username = localStorage.getItem('userinfo');
                let userinfo =JSON.parse(localStorage.getItem('userinfo'))
                return userinfo.realname;
            }
        },
        methods: {
            // 用户名下拉菜单选择事件
            handleCommand(command) {
                if (command == 'loginout') {
                    this.loginout();
                } else if (command == 'resetpass') {
                    this.changepass = true;
                }

            },
            // // 侧边栏折叠
            collapseChage() {
                this.collapse = !this.collapse;
                bus.$emit('collapse', this.collapse);
            },
            // 全屏事件
            handleFullScreen() {
                let element = document.documentElement;
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                this.fullscreen = !this.fullscreen;
            },

            //退出登录
            loginout() {
                this.$axios({
                    url: "/logout",//请求路径
                    type: "POST",//请求方式
                    params: {userId: sessionStorage.userId}, //请求的附加参数，用json对象
                }).then((res) => {
                    if (res.code == 0) {
                        localStorage.removeItem('userinfo');   //本地清除个人信息
                        this.$router.push('/login');
                    }
                })
            },

            //修改密码
            resetpassword() {
                this.$axios({
                    url: "/user/update/pwd",//请求路径
                    type: "POST",//请求方式
                    params: {
                        oldPwd: this.form.oldPwd,    //分组id
                        newPwd: this.form.newPwd,    //分组id
                    },
                }).then((res) => {
                    if (res.code == 0) {
                        this.$message.success("密码修改成功!");
                        this.changepass = false;
                        localStorage.removeItem('userinfo');   //本地清除个人信息
                        this.$router.push("/login");
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
            },

            //提交 修改密码
            submission(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                       this.resetpassword();
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },

            //重置修改密码
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        },
        mounted() {
            if (document.body.clientWidth < 1500) {
                this.collapseChage();
            }
        }
    }
</script>
<style>
    .header .el-dialog {
        width: 35%;
    }
    /*.el-form-item__error{*/
        /*left: 19%;*/
    /*}*/
    .header .el-dialog .el-input {
        width: 70%;
    }
</style>
<style scoped>

    .header {
        position: relative;
        box-sizing: border-box;
        width: 100%;
        height: 70px;
        font-size: 20px;
        /*color: #fff;*/
    }

    .collapse-btn {
        float: left;
        padding: 0 21px;
        padding-right: 15px;
        cursor: pointer;
        line-height: 70px;
    }

    .header .logo {
        float: left;
        width: 250px;
        line-height: 70px;
        /*margin-left: 2%;*/
    }

    .header-right {
        float: right;
        padding-right: 50px;
    }

    .header-user-con {
        display: flex;
        height: 70px;
        align-items: center;
    }

    .btn-fullscreen {
        transform: rotate(45deg);
        margin-right: 5px;
        font-size: 24px;
    }

    .btn-bell, .btn-fullscreen {
        position: relative;
        width: 30px;
        height: 30px;
        text-align: center;
        border-radius: 15px;
        cursor: pointer;
    }

    .btn-bell-badge {
        position: absolute;
        right: 0;
        top: -2px;
        width: 8px;
        height: 8px;
        border-radius: 4px;
        background: #f56c6c;
        color: #fff;
    }

    .btn-bell .el-icon-bell {
        color: #fff;
    }

    .user-name {
        margin-left: 10px;
    }

    .user-avator {
        margin-left: 20px;
    }

    .user-avator img {
        display: block;
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .el-dropdown-link {
        /*color: #fff;*/
        cursor: pointer;
    }

    .el-dropdown-menu__item {
        text-align: center;
    }
</style>
