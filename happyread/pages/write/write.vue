<template>
	<view class="wrap">
		<view class="write_title">
			<input type="text" v-model="title" placeholder="请输入标题" />
		</view>
		<!-- 内容展示区 -->
		<view class="artList">
			<block v-for="(item, index) in artList" :key="index">
				<view class="item" v-if="item.type == 'image'">
					<image :src="item.content" :data-index="index" mode="widthFix" @tap="removeImg" />
				</view>
				<view class="item" v-if="item.type == 'text'">
					<textarea name="item.content" placeholder="" v-model="artList[index].content" />
					<view :data-index="index" class="deleteText" @tap="deleteText">删除</view>
				</view>
			</block> 
		</view>
		<!-- 输入区 -->
		<form @submit="submit">
			<view class="inputArea">
				<view class="addImg" @tap="addImg">+图片</view>
				<view class="addText">
					<textarea name="artText" maxlength="-1" v-model="inputContent" placeholder="请输入文本" />
					<button type="primary" form-type="submit">添加</button>
				</view>
			</view>
		</form>			
		<!-- 选择分类 -->
		<view class="art-cate">
			<view>文章分类</view>
			<view class="">
				<picker mode="selector" :range="caties" @change="cateChange">
					<view>{{caties[currentCateIndex]}}</view>
				</picker>
			</view>
		</view>	
		<!-- 提交按钮 -->
        <view class="submitNow" v-if="artList.length > 0" @tap="submitNow">发布文章</view>				
	</view>
</template>

<script>
	var loginRes,_self;
	var signModel = require('../../commons/sign.js');
	export default {
		data() {
			return {
				title : '',
				artList : [],
				inputContent : "",
				needUploadImg : [],
				uploadIndex : 0,
				//分类
				caties : ['点击选择'],
				currentCateIndex : 0,
				catiesFromApi : [],
				// 记录真实选择的api接口数据的分类id
				sedCateIndex  : 0		
			}
		},
		methods: {
			//文章分类选择
			cateChange : function(e){
				var sedIndex          = e.detail.value;
				this.currentCateIndex = sedIndex;
				// 获取选择的分类名称
				if(sedIndex < 1){return ;}
				var cateName = this.caties[sedIndex];
				for(var k in this.catiesFromApi){
					if(cateName == this.catiesFromApi[k].cateName){
						this.sedCateIndex = this.catiesFromApi[k].id;
						console.log(this.sedCateIndex);
						break;
					}
				}
				this.currentCateIndex = sedIndex;
			},
			addImg : function(){
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					success: function(res) {
						_self.artList.push({"type":"image", "content":res.tempFilePaths[0]})
					}
				});
			},
			removeImg : function(e){
				var index = e.currentTarget.dataset.index;
				uni.showModal({
					content:"确定要删除此图片吗",
					title:'提示',
					success(e) {
						if (e.confirm) {
							_self.artList.splice(index, 1);
						}
					}
				});
			},
			submit : function(res){
				var content = res.detail.value.artText;
				if(content.length < 1){uni.showToast({title:"请输入内容",icon:'none'}); return ;}
				this.artList.push({"type":"text", "content" : content});
				this.inputContent = '';
			},
			deleteText : function(e){
				var index = e.currentTarget.dataset.index;
				uni.showModal({
					content:"确定要删除吗",
					title:'提示',
					success(e) {
						if (e.confirm) {
							_self.artList.splice(index, 1);
						}
					}
				});
			},
	        submitNow : function(){
	            // 数据验证
				if(this.title.length < 2){uni.showToast({title:'请输入标题', icon:"none"}); return ;}
	            if(this.artList.length < 1){uni.showToast({title:'请填写文章内容', icon:"none"}); return ;}
	            if(this.sedCateIndex < 1){uni.showToast({title:'请选择分类', icon:"none"}); return ;}

	            // 上传图片 一次一个多次上传 [ 递归函数 ]
	            // 上传完成后整体提交数据
	            // 首先整理一下需要上传的图片
	            // this.needUploadImg = [{tmpurl : 临时地址, index : 数据索引}]
	            this.needUploadImg = [];
	            for(var i = 0; i < this.artList.length; i++){
	                if(this.artList[i].type == 'image'){
	                    this.needUploadImg.push({"tmpurl" : this.artList[i].content , "indexID" : i});
	                }
	            }
	            this.uploadImg();
	        },
			uploadImg : function(){
				// 如果没有图片 或者已经上传完成，则执行文章提交
				if(this.needUploadImg.length < 1 || this.uploadIndex >=  this.needUploadImg.length){
					uni.showLoading({title:"正在提交"});
					// 将信息整合后提交到服务器
					var sign = uni.getStorageSync('sign');
					uni.request({
						url: this.apiServer + this.addArt ,
						method: 'POST',
						header: {'content-type' : "application/x-www-form-urlencoded"},
						data: {
							title   : _self.title,
							content : JSON.stringify(_self.artList),
							uid     : loginRes[0],
							cate    : _self.sedCateIndex,
							sign    : sign
						},
						success: res => {
							console.log(res);
							if(res.data.success){
								uni.showToast({title:"提交成功", icon:"none"});
								_self.artList = [];
								// 清空数据
								signModel.sign(_self.apiServer + _self.accessToken);
								// 防止数据缓存
								_self.currentCateIndex = 0;
								_self.sedCateIndex     = 0;
								_self.needUploadImg    = [];
								_self.title            = '';
								setTimeout(function(){ uni.switchTab({ url:'../my/my' }) }, 1000);
							}else{
								uni.showToast({title:res.data.data, icon:"none"});
							}
						},
						fail: (res) => {
                          
						}
					});
					return ;
				}
				// 上传图片
				uni.showLoading({title:"上传图片"});
				var uploader = uni.uploadFile({
					url      : _self.upload,
					filePath : _self.needUploadImg[_self.uploadIndex].tmpurl,
					name     : 'file',
					success: (uploadFileRes) => {
						uploadFileRes = JSON.parse(uploadFileRes.data);
						console.log('==>'+ JSON.stringify(uploadFileRes));
						if(!uploadFileRes.success){
							uni.showToast({title:"上传图片失败,请重试!", icon:"none"});
							return false;
						}
						// 将已经上传的文件地址赋值给文章数据
						var index = _self.needUploadImg[_self.uploadIndex].indexID;
						_self.artList[index].content = _self.staticServer + uploadFileRes.message;
						console.log("_self.artList=>"+_self.artList[index].content); 
						_self.uploadIndex ++;
						// 递归上传
						setTimeout(function(){_self.uploadImg();}, 1000);
					},
					fail: () => {
						uni.showToast({title:"上传图片失败,请重试!", icon:"none"});
					}
				})
			},
		},
		onLoad:function(){
			_self = this;
			loginRes = this.checkLogin('../write/write',2);
			if(!loginRes){return ;}
			// 获取签名
			signModel.sign(_self.apiServer+_self.accessToken);
			// 动态加载文章分类
			uni.request({
				url: this.apiServer + this.categoryType,
				method: 'GET',
				success: res => {
					console.log('文章分类'+JSON.stringify(res.data.result));
					if(res.data.success){
						// 把数据格式整理为 picker 支持的格式 ['分类名', ...]
						var categories = res.data.result;
						for(var k in categories){
							_self.caties.push(categories[k].cateName);
						}
						// 记录分类信息
						_self.catiesFromApi = categories;
					}
				}
			});
		}
	}
</script>

<style>

</style>
