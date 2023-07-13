<template>
	<view>
		<!-- #ifdef MP-WEIXIN -->
			<button type="primary" open-type="getUserInfo" @getuserinfo="getUserInfo">使用微信登录</button>
		<!-- #endif -->
	</view>
</template>

<script>
	var sign = require('../../commons/sign.js');
	var _self,session_key,openid,pageOption;
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			// 微信小程序登录按钮触发
			getUserInfo : function(info){
				console.log(info);
				console.log('code换取的openid='+openid);
				var sign = uni.getStorageSync('sign');
				uni.request({
					url: _self.apiServer + _self.memberLogin,
					method: 'POST',
					header:{'content-type' : "application/x-www-form-urlencoded"},
					data: {
						openid : openid,
						name   : info.mp.detail.userInfo.nickName,
						face   : info.mp.detail.userInfo.avatarUrl,
						sign   : sign
					},
					success: res => {
						console.log('server return => '+JSON.stringify(res));
						//将远程返回的信息存储到本地
						if(res.data.success){
							uni.showToast({title:'登录成功'});
							uni.setStorageSync('SUID',res.data.result.id);
							uni.setStorageSync('SNAME',res.data.result.uname);
							uni.setStorageSync('SFACE',res.data.result.uface);
						}
						//跳转到调用前的地址
						if(pageOption.backtype == 1){
							uni.redirectTo({url:pageOption.backpage});
						}else{
							uni.switchTab({url:pageOption.backpage});
						}
					},
					fail: () => {}
				});
				
				
			}
		},	
		onLoad:function(options){
			console.log(options);
			_self = this;
			pageOption = options;
			sign.sign(_self.apiServer + _self.accessToken);
			// 页面加载时用code换取openid
			// #ifdef MP-WEIXIN
			uni.login({
				success: res => {
					console.log('code=>'+JSON.stringify(res));
					//调用服务器接口方法,实现code换取openid
					uni.request({
						url: _self.apiServer + _self.codeToSession,
						method: 'POST',
						header:{'content-type' : "application/x-www-form-urlencoded"},
						data: {
							code : res.code
						},
						success: sessions => {
							console.log('sessions=>'+JSON.stringify(sessions.data.result.openid));
							session_key = sessions.data.result.session_key;
							openid = sessions.data.result.openid;
						},
						fail: () => {}
					});
				},
				fail: (e) => {
					console.log(e);
				}
			});
			
			// #endif
			
			// app----手机端已经内置实现了用code换以openid
			//#ifdef APP-PLUS
			uni.login({
				provider:'weixin',
				success:res =>{
					uni.getUserInfo({
						success:(info) => {
							console.log(JSON.stringify(info));
							uni.request({
								uni: _self.apiServer + _self.memberLogin,
								method: 'POST',
								header: {'content-type' : "application/x-www-form-urlencoded"},
								data: {
									openid : info.userInfo.openid,
									name   : info.userInfo.nickName,
									face   : info.userInfo.avatarUrl
								},
								sucess:res =>{
									console.log(JSON.stringify(res));
									
									//将远程返回的信息存储到本地
									if(res.data.success){
										uni.showToast({title:'登录成功'});
										uni.setStorageSync('SUID',res.data.result.id);
										uni.setStorageSync('SNAME',res.data.result.uname);
										uni.setStorageSync('SFACE',res.data.result.uface);
									}
									//跳转到调用前的地址
									if(pageOption.backtype == 1){
										uni.redirectTo({url:pageOption.backpage});
									}else{
										uni.switchTab({url:pageOption.backpage});
									}
								},
								fail: (e) => {
									console.log(e);
								}
							});
						}
					})
				},
				fail: (e) => {
					console.log(e);
				}
				
			});
			//#endif
			
			//#ifdef MP-WEIXIN
			//#endif
		}
	}
</script>

<style>

</style>
