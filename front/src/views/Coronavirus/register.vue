<template>
  <div>
    <el-row>
      <el-col>
        <el-form ref="form" :model="form" :rules="formRules" class="elform_Class" >
          <p style="font-size:26px;text-align:center;color:#3BA7B4;margin-bottom:35px;margin-top:0px">注 册 同 化 账 号</p>
          <el-form-item prop="username">
            <el-input v-model="form.username" class="input_Class" prefix-icon="el-icon-user-solid" placeholder="请输入注册账号" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" class="input_Class" prefix-icon="el-icon-lock" placeholder="请输入密码" />
          </el-form-item>
          <el-form-item prop="rePassword">
            <el-input v-model="form.rePassword" type="password" class="input_Class" prefix-icon="el-icon-lock" placeholder="请再次输入密码" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="elbutton_Class" @click="onSubmit('form')">注册账号</el-button>
            <el-button type="primary" class="elbutton_Class" @click="goBack('form')">返回登录</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { register } from '@/api/login'
export default {
  name: 'Register',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.form.rePassword !== '') {
          this.$refs.form.validateField('rePassword')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var checkUserName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    return {
      form: {
        username: '',
        password: '',
        rePassword: '',
        email: '1659760418@qq.com'
      },
      formRules: {
        username: [{ validator: checkUserName, trigger: 'blur' },
          { pattern: /[0-9]+[a-zA-Z]+[0-9a-zA-Z]*|[a-zA-Z]+[0-9]+[0-9a-zA-Z]*/, message: '必须输入字母和数字的用户名' }],
        password: [{ validator: validatePass, trigger: 'blur' },
          { pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,30}$/, message: '密码为数字，小写字母，大写字母，特殊符号 至少包含三种，长度为 8 - 30位' }],
        rePassword: [{ validator: validatePass2, trigger: 'blur' }]
      }
    }
  },
  mounted() {
  },
  methods: {
    onSubmit(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          register(this.form.username, this.form.password, this.form.email).then((response) => {
            this.$message({
              showClose: true,
              message: '注册成功',
              type: 'success'
            })
            this.goBack(form)
          })
        } else {
          return false
        }
      })
    },
    goBack(form) {
      this.$refs[form].resetFields()
      this.$router.push('/signIn')
    }
  }
}
</script>

<style lang="scss" scoped>
.elform_Class{
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
/deep/.input_Class{
  .el-input__inner{
background-color:#42464E;
background:rgba(0,0,0,.1);
border:1px solid #555961;
height:50px;
vertical-align: 30%;
color:#ffffff
  }
}
.elbutton_Class{
  width:48%;
  background:#21958B;
  color:#ffffff;
  border:0px;
  height:40px
}
</style>
