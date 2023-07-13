<template>
	<view>
		<!-- 标题 -->
		<view :class="['grace-article-title',graceSkeleton == 'ing' ? 'grace-skeleton' : '']">{{article.artTitle}}</view>
		<!-- 作者信息 -->
		<view class="grace-article-author-line">
			<view :class="['grace-article-author',graceSkeleton == 'ing' ? 'grace-skeleton' : '']">
				<image :src="article.uface" mode="widthFix"></image>
				<view class="author-name">{{article.uname}}</view>
			</view>
			<view>{{article.artCreatetime}}</view>			
		</view>
		<!-- 文章内容-->
		<view class="grace-article-contents">
			<block v-for="(item,index) in artContents" :key="index">
				<view :class="['img-item',graceSkeleton == 'ing' ? 'grace-skeleton' : '']" v-if="item.type == 'image'">	
					<image :src="item.content":data-url="item.content" mode="widthFix" @tap="showImgs"></image>
				</view>
				<view :class="['text-item',graceSkeleton == 'ing' ? 'grace-skeleton' : '']" v-if="item.type == 'text'">{{item.content}}</view>
			</block>
		</view>
	</view>
</template>

<script>
	var artId,_self;
	export default {
		data() {
			return {
				article : [], //文章基础信息
				artContents : [], //文章项目
				graceSkeleton : 'ing'
			}
		},
		methods: {
			//预览图片
			showImgs : function(e){
				var currentUrl = e.currentTarget.dataset.url;
				// 找出所有图片
				var imgsNeedShow = [];
				for(var i = 0;i < this.artContents.length;i++){
					if(this.artContents[i].type == 'image'){
						imgsNeedShow.push(this.artContents[i].content);
					}
				}
				uni.previewImage({
					urls: imgsNeedShow,
					current:currentUrl
				});
			}
		},
		onLoad:function(options){
			_self = this;
			artId = options.artId;
			//console.log(artId);
			uni.showLoading({
				title:""
			});
			uni.request({
				url: _self.apiServer + _self.artInfo ,
				method: 'GET',
				data: {
					artId:artId
				},
				success:res => {
					console.log(JSON.stringify(res));
					var art = res.data.result;
					console.log('art'+art.artContent)
					// 将文章内容转换成数组
					var artContentItems = JSON.parse(art.artContent);
					console.log(artContentItems);
					//首先规划骨架
					_self.artContents = [] ;
					for(var i= 0; i< artContentItems.length;i++){
						_self.artContents.push({'type': artContentItems[i].type});
					}
					//延迟添加数据
					setTimeout(function(){
						_self.article		= art;
						_self.artContents	= artContentItems;
						_self.graceSkeleton = 'end';
						uni.hideLoading();
					},500)
				},
				fail: (res) => { }
			});
		}
	}
</script>

<style>

</style>
