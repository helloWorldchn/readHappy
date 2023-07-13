<script>
	export default {
		onLaunch: function() {
			console.log('App Launch');
			console.log(uni.getStorageSync('SUID'));
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	@import 'graceUI/graceUI.css';
</style>
