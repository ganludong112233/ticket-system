<template>
    <div id="app">
        <div
                v-loading="isLOADING.LOADING"
               element-loading-text="拼命加载中"
               element-loading-spinner="el-icon-loading"
               element-loading-background="rgba(0, 0, 0, 0.8)"
               style="width: 100%;position: fixed;height:100%;top:0;left:0">
        </div>
        <router-view></router-view>
    </div>
</template>
<style>
    @import "./assets/css/main.css";
    @import "./assets/css/color-dark.css";     /*深色主题*/
    /*@import "./assets/css/theme-green/color-green.css";   浅绿色主题*/
</style>
<script>
    import {mapState}  from 'vuex'
    export default {
        name: 'app',
        data() {
            return {

            }
        },
        computed: {
            ...mapState({isLOADING:state=>state}),
        },
    }
</script>