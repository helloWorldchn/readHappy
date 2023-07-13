import Vue from 'vue'
import App from './App'

Vue.config.productionTip = false
//添加全局检查登录函数
Vue.prototype.checkLogin = function(backpage, backtype){
	var SUID  = uni.getStorageSync('SUID');    //用户id
	var SNAME = uni.getStorageSync('SNAME');  //用户昵称
	var SFACE = uni.getStorageSync('SFACE');  //用户头像
	if(SUID == '' || SFACE == ''){  	
		uni.redirectTo({url:'../login/login?backpage='+ backpage+'&backtype='+backtype}); //没有登录，转向登录界面
		return false;
	}
	return [SUID, SNAME, SFACE];
}

var baseServer = 'http://192.168.153.1:8080/jeecg-boot/';
// Vue.prototype.staticServer = baseServer;
// Vue.prototype.staticServer = 'https://readhappy.oss-cn-beijing.aliyuncs.com/';
Vue.prototype.staticServer = '';
Vue.prototype.apiServer = baseServer + 'readHappy/';
Vue.prototype.memberLogin = 'member/login';
Vue.prototype.codeToSession = 'member/codeToSession';
Vue.prototype.accessToken = 'token/getAccessToken';

Vue.prototype.categoryType = 'categories/cateType';
Vue.prototype.upload = baseServer + 'sys/common/upload';
Vue.prototype.addArt = 'article/addArt';
Vue.prototype.info = 'member/info';
Vue.prototype.artList = 'article/artList';
Vue.prototype.removeArticle = 'article/removeArt';
Vue.prototype.artListByCate = 'article/artListByCate';
Vue.prototype.artInfo = 'article/artInfo';
Vue.prototype.editArticle = 'article/editArt';

App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
