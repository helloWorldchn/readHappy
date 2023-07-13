<template>
    <view>
        <view class="grace-header-cate">
            <scroll-view class="grace-tab-title grace-center" scroll-x="true" id="grace-tab-title">
                <view v-for="(cate, index) in categories" :key="index" :data-cateid="cate.cateId" :data-index="index" :class="[cateCurrentIndex == index ? 'grace-tab-current' : '']" @tap="tabChange">{{cate.name}}</view>
            </scroll-view>
        </view>
        <view style="height:50px;"></view>
        <!-- 文章内容区 -->
        <view class="grace-news-list">
            <block v-for="(item, index) in artList" :key="index">
                <!-- 一张图 -->
                <navigator v-if="item.artContent.length < 3" open-type="navigate" :url="'../info/info?artId='+item.id">
                    <view class="grace-news-list-img-news"> 
                        <view class="grace-news-list-title-main">{{item.artTitle}}</view>
                        <view class="grace-news-list-img-big"><image :src="item.artContent[0]" mode="widthFix"></image></view>
                    </view>
                </navigator>
                <!-- 三张图 -->
               <navigator v-if="item.artContent.length >= 3" :url="'../info/info?artId='+item.id">
                    <view class="grace-news-list-img-news">
                        <view class="grace-news-list-title-main">{{item.artTitle}}</view>
                        <view class="grace-news-list-imgs">
							<view class="imgs"><image :src="item.artContent[0]" mode="widthFix"></image></view>
                            <view class="imgs"><image :src="item.artContent[1]" mode="widthFix"></image></view>
                            <view class="imgs"><image :src="item.artContent[2]" mode="widthFix"></image></view>
                        </view>
                    </view>
                </navigator>
            </block>
        </view>  		
	</view>
</template>
<script>
	var _self, cateTypeId = 0, page = 1;	
	export default {
		data() {
			return {
				categories:[{cateId : 0, name : "全部"}],
				cateCurrentIndex : 0,
				artList:[]
			}
		}, 
		onLoad() {
			_self = this;
			// 加载文章分类
			uni.request({
				url: _self.apiServer + _self.categoryType,
				method: 'GET',
				success: res => {
					//console.log(JSON.stringify(res.data.result));
					if(res.data.success){
						// 把数据格式整理scroll所需样式
						var categories = res.data.result;
						for(var k in categories){
							_self.categories.push({cateId : k, name : categories[k].cateName});
						}
					}
				}
			});
			// 加载全部文章
			this.getNewsList();		
		},
		onReachBottom:function(){
			_self.getNewsList();
		},
		onPullDownRefresh:function(){
			page = 1;
			_self.artList = [];
			_self.getNewsList();
		},
		methods: {
			tabChange :  function(e){
				// var cateId = e.currentTarget.dataset.cateid;
				var cateId=  parseInt(e.currentTarget.dataset.cateid) + 1
				var index = e.currentTarget.dataset.index; 
				console.log(cateId + '::' + index);
				page = 1;
				this.cateCurrentIndex = index;
				cateTypeId = index;
				this.artList = [];
				this.getNewsList();
			},		
			getNewsList : function(){
				uni.showLoading({'title':"加载中..."});
				console.log('getNewsList==>cate:'+ cateTypeId); 
				uni.request({ 
					url: this.apiServer + this.artListByCate,
					method: 'GET',
					data: {
						cate  	: cateTypeId,
						pageNo	: page,
						// pageSize: 2//每页只显示2篇文章，并且滑轮不好使，将2改成5或者直接注释后问题解决
					},
					success: res => {
						// console.log(JSON.stringify(res));
						if(!res.data.success){
							console.log("已经加载全部");
							uni.showToast({
								title:"已经加载全部文章",
								icon: "none"
							});
						}else if(res.data.success){
							//整理新闻信息
							var newsList = res.data.result.records;
							for(var i = 0; i < newsList.length; i++){
								// 把图片分离出来
								var imgs = [];
								var content = newsList[i].artContent;
								content = JSON.parse(content);
								for(var ii = 0; ii < content.length; ii++){
									if(content[ii].type == 'image'){
										// console.log(content[ii].content);
										imgs.push(content[ii].content);
									}
								}
								newsList[i].artContent = imgs;
								// console.log(newsList[i].artContent);
							}
							// console.log(newsList);
							//填充数据
							_self.artList = _self.artList.concat(newsList);
							uni.hideLoading();
							page++;
						}
					},
					complete:function(){
						uni.stopPullDownRefresh();
					}
				});
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
