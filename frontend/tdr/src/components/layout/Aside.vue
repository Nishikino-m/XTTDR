<template>
  <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
    <el-menu :default-openeds="['1']" style="min-height: 100vh"
             :default-active="path"
             router
    >
<!--      更换头像的前后端交互没写-->
      <div style="text-align: center;margin-top: 50px;margin-bottom: 30px">
        <el-upload
            class="avatar-uploader"
            action=""
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
        >
          <img v-if="imgUrl" :src="imgUrl" class="avatar" />
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
        <el-menu-item-group>
          <template #title>分组一</template>
          <el-menu-item index="2-1">选项1</el-menu-item>
          <el-menu-item index="2-2">选项2</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="分组2">
          <el-menu-item index="2-3">选项3</el-menu-item>
        </el-menu-item-group>
        <el-sub-menu index="2-4">
          <template #title>选项4</template>
          <el-menu-item index="2-4-1">选项4-1</el-menu-item>
        </el-sub-menu>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title><i class="el-icon-setting"></i>学习情况</template>
        <el-menu-item-group>
          <template #title>分组一</template>
          <el-menu-item index="3-1">选项1</el-menu-item>
          <el-menu-item index="3-2">选项2</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="分组2">
          <el-menu-item index="3-3">选项3</el-menu-item>
        </el-menu-item-group>
        <el-sub-menu index="3-4">
          <template #title>选项4</template>
          <el-menu-item index="3-4-1">选项4-1</el-menu-item>
        </el-sub-menu>

      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script>

export default {
  name: "Aside",
  props: ['user'],
  data() {
    return {

      path: this.$route.path,   // 设置默认高亮的菜单
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      imgUrl:"",
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

  }
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
