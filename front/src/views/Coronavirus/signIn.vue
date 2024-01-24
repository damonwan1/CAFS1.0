<template>
  <div>
    <el-form ref="form" :model="form" :rules="formRules" class="elform_Class">
      <p style="text-align:center;color:#3BA7B4;margin-bottom:35px;margin-top:0px;font-size:26px">{{ $t("login.title") }}
      </p>
      <el-form-item prop="username">
        <el-input v-model="form.username" class="input_Class" prefix-icon="el-icon-user-solid"
          :placeholder="$t('login.user')" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" :type="passwordType" class="input_Class" prefix-icon="el-icon-lock"
          :placeholder="$t('login.password')" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye" />
        </span>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="elbutton_Class" @click.native.prevent="onSubmit">{{ $t("login.login")
        }}</el-button>
        <el-button type="primary" class="elbutton_Class" @click.native.prevent="register">{{ $t("login.register")
        }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { setLoginState, getLoginState } from '../../utils/auth'
import { loginByUsername } from '../../api/login'
import Cookies from 'js-cookie'

export default {
  name: 'Singin',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码不能小于5位'))
      } else {
        callback()
      }
    }
    return {
      form: {
        username: '',
        password: ''
      },
      formRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入账号' }],
        // password: [{ required: true, trigger: 'blur', validator: validatePass }]
      },
      passwordType: 'password'
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // this.$store.dispatch('permission/LoginByUsername', {
          // 线上登录验证用
          //   username: this.form.username,
          //   password: this.form.password
          // }).then(() => {
          //   this.$router.push('/siteMap')
          // }).catch(() => {
          //   this.$message({
          //     type: 'error',
          //     message: '用户名或密码输入错误',
          //     showClose: true
          //   })
          // })
          if (this.form.username === 'admin' && this.form.password === 'nesdc_A0717_Cpredic') {
            // 本地开发用
            setLoginState(1)
            this.$router.push('/siteMap')
          } else {
            this.$message({
              type: 'error',
              message: '账号或密码输入错误'
            })
          }
        } else {
          return false
        }
      })
    },
    register() {
      this.$router.push('/register')
    },
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
  },
  watch: {
    $route: {
      handler(val, oldval) {
        if (val.query.language == 'en') {
          this.handleSetLanguage('en')
          this.formRules = {
            username: [{ required: true, trigger: 'blur', message: "Please enter an account" }]
          }
        } else {
          this.handleSetLanguage('zh')
          this.formRules = {
            username: [{ required: true, trigger: 'blur', message: "请输入账号" }]
          }
        }
      },
      // 深度观察监听
      deep: true,
      immediate: true
    }
  },
}
</script>
<style lang="scss" scoped>
.background {
  background: url("~@/assets/Workflow/loginback.jpg") no-repeat center;
  background-size: 100% 100%;
  opacity: 0.9;
  width: 100%;
  height: 100%;
}

.elform_Class {
  background-color: #313741;
  position: absolute;
  left: 0;
  right: 0;
  width: 520px;
  max-width: 100%;
  padding: 35px 35px 15px 35px;
  margin: 120px auto;
  border-radius: 5px;
  opacity: 0.9;
}

/deep/.input_Class {
  .el-input__inner {
    background-color: #42464E;
    background: rgba(0, 0, 0, .1);
    border: 1px solid #555961;
    height: 50px;
    vertical-align: 30%;
    color: #ffffff
  }
}

.elbutton_Class {
  width: 48%;
  background: #21958B;
  color: #ffffff;
  border: 0px;
  height: 40px;

}

.show-pwd {
  position: absolute;
  right: 10px;
  top: 7px;
  font-size: 16px;
  color: #ffffff;
  vertical-align: middle;
  cursor: pointer;
  user-select: none;
}</style>
