/**
 *Created by 吕珊 on 2019/3/8.
 */
export default {

    /*搜索时间
    * val:所需要转化的时间
    * type:1,是搜索时间， 2，是列表展示时间
     */
    newTime(val,type) {
        Date.prototype.toLocaleString= function () {
            var mouth = this.getMonth() + 1;
            var sdate = this.getDate();
            var getHours = this.getHours();
            var getMinutes = this.getMinutes();
            var getSeconds = this.getDate();
            mouth=mouth<10? "0" + mouth:mouth;
            sdate=sdate<10? "0" + sdate:sdate;
            getHours=getHours<10? "0" + getHours:getHours;
            getMinutes=getMinutes<10? "0" + getMinutes:getMinutes;
            getSeconds=getSeconds<10? "0" + getSeconds:getSeconds;
            if(type==1){
                return this.getFullYear() + "-" + mouth + "-" + sdate+" 00:00:00";
            }else{
                return this.getFullYear() + "-" +mouth + "-" +sdate+ "  " + getHours+ ":" + getMinutes+ ":" + getSeconds ;
            }
        };
        var unixTimestamp = new Date(val);
        var commonTime = unixTimestamp.toLocaleString();
        return commonTime;
    },

}