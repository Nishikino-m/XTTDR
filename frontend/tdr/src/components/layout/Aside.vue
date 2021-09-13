<template>
  <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
    <el-menu :default-openeds="['1']" style="min-height: 100%"
             :default-active="path"
             router
    >
        <div style="text-align: center;margin-top: 50px">
          <el-upload
              class="avatar-uploader"
              action=""
              :http-request="httpRequest"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-avatar v-if="imgUrl" :src="imgUrl" class="avatar" :size="150" shape="circle"/>
            <el-avatar v-else :size="100" :src="circleUrl"></el-avatar>
          </el-upload>
<!--        显示个人信息的div-->
        <div class="aside-information">
        {{"name:"+user.id}}
        </div>
      </div>


      <el-sub-menu index="1">
        <template #title><i class="el-icon-message"></i>课程管理</template>
        <el-menu-item index="/course">所有课程</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title><i class="el-icon-menu"></i>考试管理</template>
        <el-menu-item index="/exam">所有考试</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title><i class="el-icon-setting"></i>学习情况</template>
        <el-menu-item-group>
          <template #title>分组一</template>
          <el-menu-item index="3-1">选项1</el-menu-item>
          <el-menu-item index="3-2">选项2</el-menu-item>
        </el-menu-item-group>

      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Aside",
  props: ['user'],
  data() {
    return {

      path: this.$route.path,   // 设置默认高亮的菜单
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      imgUrl:this.user.user.avatar,
      filesUploadUrl: "http://localhost:9090/courseMaterial/add"
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.imgUrl = URL.createObjectURL(file.raw)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    httpRequest(param) {
      console.log(param)
      let fileObj = param.file // 相当于input里取得的files
      let fd = new FormData()// FormData 对象
      fd.append('file', fileObj)// 文件对象
      fd.append('id',this.user.id)
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      request.post('/files/avatar', fd, config).then(res=>{
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "上传成功"
          })
          this.fileList=[]
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.refreshUser()
      })
    },
    refreshUser() {
      let userId = JSON.parse(sessionStorage.getItem("user")).id
      request.get("/account/" + userId).then(res => {
        this.$store.commit('setUser',res.data)
        sessionStorage.setItem("user",JSON.stringify(res.data))
        this.circleUrl = res.data.user.avatar
      })
    }
  },
}
</script>


<style scoped>

  .el-aside {
    color: var(--el-text-color-primary);
  }
  /*用户上传头像的测试*/
      .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        align: center;
        background: #409eff;
      }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  .aside-information {
    font-size: smaller;
    color: rgba(74, 56, 155, 0.83);
    font-family: Consolas;
  }


</style>
