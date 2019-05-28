module.exports = {
    devServer: {
        // 设置代理
        proxy: {
            '/api': {
                // target: "http://test3.scsxy.com.cn:81/api", //设置你调用的接口域名和端口号 别忘了加http  http://api.test-hsh.scsxy.com.cn:81/
                target:  "http://10.40.1.55:8067/ticket",
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '' //这里理解成用‘/api'代替target里面的地址，后面组件中我们掉接口时直接用api代替
                    //比如我要调用'http://40.00.100.133:3002/user/login'，直接写‘/api/user/login'即可
                }
            },
        }
    },
    baseUrl: './',
    outputDir: undefined,
    assetsDir: undefined,
    runtimeCompiler: undefined,
    productionSourceMap: undefined,
    parallel: undefined,
};