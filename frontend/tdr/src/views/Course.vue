<template>
  <div class="course-page">

    <el-row :gutter="0">
      <el-col :span="4"><el-button type="primary" @click="add">加入课程</el-button>
        <el-button type="primary" v-if="user.userType!=='student'" @click="this.vis = true">新建课程</el-button></el-col>

      <el-col :span="20">
        <div style="margin: 10px 0">
          <el-input v-model="search" placeholder="请输入关键字" style="width: 30%" clearable></el-input>
          <el-button type="primary" style="margin-left: 5px" @click="load">查询课程</el-button>
        </div>
      </el-col>
    </el-row>
    <div class="course-card" v-loading="loading" style="height: 60vh">
      <el-card  class="box-card" v-for="it in tableData">
        <div class="course-back">
          <img src="@/assets/img/course.svg" alt=""/>
        </div>
        <div class="course-bottom">
          <span style="position: absolute;left: 0">{{it.name}} : {{it.teacherId}}</span>
          <el-button type="primary" class="enter" @click="enterCourse(it.courseId)">进入课程</el-button>
        </div>
      </el-card>

    </div>
    <el-pagination style="margin-top: 50px"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="currentPage"
                   :page-sizes="[3, 6, 9]"
                   :page-size="pageSize"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total" >
    </el-pagination>
  </div>

  <el-dialog title="加入课程" v-model="dialogVisible" width="30%">
    <el-form label-width="120px">
      <el-form-item label="课程编号">
        <el-input v-model="addStudyId" style="width: 80%"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog title="添加课程" v-model="this.vis" width="30%">
    <el-form label-width="120px">
      <el-form-item label="课程名称">
        <el-input v-model="courseName" style="width: 80%"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="this.vis = false">取 消</el-button>
        <el-button type="primary" @click="saveCourse">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Course",
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      vis: false,
      search: '',
      currentPage: 1,
      pageSize: 6,
      total: 0,
      tableData: [],
      addStudyId: '',
      // filesUploadUrl: "http://" + window.server.filesUploadUrl + ":9090/files/upload",
      ids: [],
     courseName: '',
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)

    this.load()
  },
  methods: {
    load() {
      this.loading = true
      request.get("/course", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true
    },
    save(){
      request.post("/course/addStudy", null,{params: {
          addStudyId: this.addStudyId
        }
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "新增成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
        this.dialogVisible = false  // 关闭弹窗
      })
    },
    saveCourse(){
      request.post("/course/add", null,{params: {
          name: this.courseName
        }
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "新建课堂成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
        this.vis = false  // 关闭弹窗
      })
    },
    enterCourse(courseId){
      this.$store.commit('setCourseId',courseId)
      this.$router.push('/coursePage')
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
@import '../assets/css/style.css';
div >>> {
  font-family: "ok";

}

.course-page{
  text-align: center;
}
.course-card{
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-top: 20px;
}
.course-back{
  background-color: deepskyblue;
  margin: 0;
  width: 100%;
}
.course-bottom{
  position: relative;
  margin-top: 5px;
}
.box-card {
  width: 350px;
  height: 200px;
  margin: 20px;
}
.enter{
  position: absolute;
  right: 0;
}
.el-form-item{
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
</style>
