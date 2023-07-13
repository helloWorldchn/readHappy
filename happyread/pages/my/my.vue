<template>
	<view class="grace-padding">
	    <view class="myface"><image :src="myFace" mode="widthFix"></image></view>
		<view style="text-align:center; margin:10px 0;">
			{{user.uname}}<text style="color: #888888;" @tap="logoff">[注销]</text>
		</view>
		<view class="grace-box-banner" style="margin:30rpx 0;">
			<view class="garce-items">
				<view class="line1">{{user.artCount}}</text></view>
				<view class="line2">文章</view>
			</view>
			<view class="garce-items">
				<view class="line1">{{user.uintegral}}</text></view>
				<view class="line2">积分</view>
			</view>
			<view class="garce-items">
				<view class="line1">{{user.uremainder}}</text></view>
				<view class="line2">余额</view>
			</view>
            <view class="garce-items">
                <view class="line1">0</text></view>
                <view class="line2">消息</view>
            </view>			
		</view>
		<view class="grace-title grace-nowrap grace-space-between">
	        <view class="grace-h5 grace-blod">我的文章</view>
	    </view>
	    <view class="myart-list" v-for="(item, index) in arts" :key="index">
	        <view class="title">{{item.artTitle}}</view>
			<view class="btns">
				<view :data-artid="item.id"  @tap="editArt">编辑</view>
				<view :data-artid="item.id" :data-index="index" @tap="removeArt">删除</view>
			</view>
	    </view>
	    <view class="loadMore" @tap="getArtList">{{loadMore}}</view>	
	</view>
</template>
<!--  -->
<script>
	var loginRes,_self,page=1;
	export default {
		data() {
			return {
				myFace : '',
				arts : [],
				loadMore : "点击加载更多",
				user : []
			};
		},
		methods: {
			logoff : function(){
				uni.removeStorageSync('SUID');
				uni.removeStorageSync('SRAND');
				uni.showToast({
					title:"您已经退出悦读",
					icon: "none"
				});
				setTimeout(function(){
					uni.switchTab({
						url:'../index/index'
					});
				}, 1000);
			},
			getArtList : function(){
				if(this.loadMore != '点击加载更多'){return ;}
				this.loadMore = '加载中...';
				uni.request({
					url: this.apiServer + this.artList,
					method: 'GET',
					header: {'content-type' : "application/x-www-form-urlencoded"},
					data: {
						uid 	: loginRes[0],
						pageNo	: page,
						pageSize: 3
						
					},
					success: res => {
						console.log(page + '::' + JSON.stringify(res.data.result.records));
						var pageSize = res.data.result.pages;
						if(res.data.result.records.length != 0 ){
							this.arts = this.arts.concat(res.data.result.records);
							page++;
							if(page <= pageSize){
								this.loadMore = '点击加载更多';
							}else{
								this.loadMore = '已经加载全部';
							}
						}else{
							this.loadMore = '已经加载全部';
						}
					}
				});
			},
			editArt : function(e){
				var artId = e.currentTarget.dataset.artid;
				uni.navigateTo({
					url:"../editArt/editArt?artId="+artId
				});
			},
			removeArt : function(e){
				var artId = e.currentTarget.dataset.artid;
				var index = e.currentTarget.dataset.index;
				uni.showModal({
					title:"提示",
					content:"确定要删除吗?",
					success:function(e){
						if(e.confirm == true){
							uni.request({
								url: _self.apiServer + _self.removeArticle,
								method: 'POST',
								header: {'content-type' : "application/x-www-form-urlencoded"},
								data: {
									uid : loginRes[0],
									artId : artId,
									sign : loginRes[1]
								},
								success: res => {
									console.log(JSON.stringify(res));
									if(res.data.success){
										uni.showToast({title: "已删除", icon:"none"});
										_self.arts.splice(index, 1);
									}else{
										uni.showToast({title: "删除失败", icon:"none"});
									}
								}
							});
						}
					}
				});
			}								
		},
		onLoad:function(){
			_self = this;
			loginRes = this.checkLogin('../my/my', '2');
			if(!loginRes){return false;}
			_self.myFace = loginRes[2];
		},
		onShow:function(){
			//加载我的文章
			this.arts = [];
			page = 1;
			this.getArtList();	
			// 加载会员信息
			uni.request({
				url: _self.apiServer + _self.info,
				method: 'POST',
				header: {'content-type' : "application/x-www-form-urlencoded"},
				data: {
					uid : loginRes[0],
					random : loginRes[1]
				},
				success: res => {
					console.log(JSON.stringify(res));
					if(res.data.success){
						this.user = res.data.result;
					}
				}
			});
			this.arts = [];
			page = 1;
			this.loadMore = '点击加载更多';
			this.getArtList();
		}
	}
</script>

<style>
.myface{width:88px; height:88px; border:5px solid #F1F2F3; border-radius:100%; margin:15px auto;}
.myface image{width:100%; border-radius:100%;}
.myart-list{width:100%; margin:8px 0; overflow:hidden; border-bottom:1px dashed #D7D8D9;}
.myart-list .title{line-height:2em; width:100%;}
.myart-list .btns{line-height:2em; width:100%;}
.myart-list .btns view{float:right; padding:0 6px; margin:0 5px; color:#00B26A;}
.myart-list .btns view:last-child{color:#F76260;}
.loadMore{width:100%; padding:8px 0; text-align:center; color:#CCCCCC;}
</style>
