<template>
  <div class="login" > <!--  加背景图片-->
    <div class="login_card">
      <div style="font-size: 30px; text-align: center; ">
        <img :src="titleUrl" style="width:90%;height: 90%;margin-right: 10%" />
<!--        <p>欢迎登录</p>-->
      </div>
      <el-form ref="form" :model="form" size="normal" :rules="rules" style="padding: 0 5%  3%  5% ">
        <el-form-item prop="id" >
          <el-input  prefix-icon="el-icon-user-solid" v-model="form.id" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="pwd">
          <el-input prefix-icon="el-icon-lock" v-model="form.pwd" show-password placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
         <div style="display: flex;justify-content: center;">
           <el-input prefix-icon="el-icon-key" v-model="form.validCode" style="width: 50%; margin-right: 5%" placeholder="请输入验证码"></el-input>
           <div class="validCode">
             <ValidCode @input="createValidCode" />
           </div>

         </div>
        </el-form-item>



        <div style="text-align: center;margin-bottom: 20px">
          <el-radio-group v-model="form.userType" fill="#7858C0">
            <el-radio-button label="student">学生</el-radio-button>
            <el-radio-button label="teacher">教师</el-radio-button>
            <el-radio-button label="admin">管理员</el-radio-button>
          </el-radio-group>
        </div>


        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="login">登 录</el-button>
        </el-form-item>
        <el-form-item><el-button type="text" @click="$router.push('/register')">前往注册 >> </el-button></el-form-item>
      </el-form>

    </div>
    <div class="footer">
      <p>Copyright© 2021.&nbsp;&nbsp;&nbsp; 学通天地人.&nbsp;&nbsp;All rights reserved.</p>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";

export default {
  name: "Login",
  components: {
    ValidCode,
  },
  data() {
    return {
      form: {userType: 'student'},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
      validCode: '',
      titleUrl: require("../assets/title.png"),
    }
  },
  created() {
    sessionStorage.removeItem("user")
  },
  methods: {
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    login() {
      console.log("login now")
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.validCode) {
            this.$message.error("请填写验证码")
            return
          }
          if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            return
          }
          console.log("to request")
          request.post("/login", this.form).then(res => {
            if (res.code === '0') {
              console.log("code")
              this.$message({
                type: "success",
                message: "登录成功"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  // 缓存用户信息
              this.$router.push("/")  //登录成功之后进行页面的跳转，跳转到主页
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  },
}
</script>

<style scoped>
@import '../assets/css/style.css';
div >>> .el-input > input{
  height: 50px;
  font-family: "ok";
  font-size: 25px;
}

</style>