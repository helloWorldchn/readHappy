var md5 = require('./md5.js');
module.exports = {
	sign: function(apiAddress) {
		// 环境判断非uni环境不支持
		console.log('uni==>' + JSON.stringify(uni));
		if (!uni) {
			return '...';
		}
		console.log('开始签名');
		// 连接服务器获取一个临时的accessToken
		uni.request({
			url: apiAddress,
			method: 'GET',
			success: res => {
				res = res.data;
				console.log(res);
				console.log('token_time=>' + res.result.token + res.result.time);
				if (!res.success) {
					return;
				}
				// 对 accessToken 进行md5加密
				var accessToken = md5.hex_md5(res.result.token + res.result.time);
				// 签名 = md5(accessToekn + time) + '-' + 'accessToekn';
				var sign = accessToken + '-' + res.result.token;
				console.log(sign);
				// 记录在本地
				uni.setStorage({
					key: "sign",
					data: sign
				});
				console.log('签名完成');
			},
			fail: function(e) {
				console.log(JSON.stringify(e));
			}
		});
	}
}
