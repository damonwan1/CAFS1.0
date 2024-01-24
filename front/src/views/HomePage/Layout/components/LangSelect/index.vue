<template>
	<el-dropdown trigger="click" class="international" @command="handleSetLanguage">
		<a class="title">
			{{ title }}
		</a>
		<el-dropdown-menu slot="dropdown">
			<el-dropdown-item :disabled="language === 'zh'" command="zh">中文</el-dropdown-item>
			<el-dropdown-item :disabled="language === 'en'" command="en">English</el-dropdown-item>
		</el-dropdown-menu>
	</el-dropdown>
</template>
 
<script>
import Cookies from 'js-cookie'
export default {
	name: "LangSelect",
	computed: {
		language() {
			return this.$store.getters.language
		}
	},
	data() {
		return {
			title: '中文'
		}
	},
	created() {
		if (Cookies.get('language') == 'zh' || Cookies.get('language') == '' || Cookies.get('language') == undefined) {
			this.title = '中文'
		} else {
			this.title = 'English'
		}
	},
	watch: {
		$route: {
			handler(val, oldval) {
				if (val.query.language == 'en') {
					this.handleSetLanguage('en')
				}
			},
			// 深度观察监听
			deep: true,
			immediate: true
		}
	},
	methods: {
		handleSetLanguage(lang) {
			this.$i18n.locale = lang;
			this.$store.dispatch('setLanguage', lang)
			// this.$message.success('switch language success')
			if (Cookies.get('language') == 'zh') {
				this.title = '中文'
			} else {
				this.title = 'English'
			}
		}
	}
}
</script>
 
<style>
.title {
	margin-left: 20px;
	font-weight: 400;
	font-size: 18px;
	color: rgba(0, 0, 0, 1);
	/* vertical-align: 10%; */
	line-height: 60px;
	font-family: Microsoft YaHei;
	color: rgba(255, 255, 255, .75)
}
</style>