<template>
  <div class="exam-page">
    <el-row :gutter="0" >
      <el-col :span="6" style="background-color: #ffffff">
        <el-button v-if="user.userType!=='student'" style="margin: 10px 0;padding: 0 10px;" type="primary" @click="this.dialogVisible = true">新建考试</el-button>

  </el-col>

    <el-col :span="12" style="background-color: #ffffff; " :offset="user.userType!=='student'?6:12">
      <div style="margin: 10px 0;">
        <el-input v-model="search" placeholder="请输入关键字" style="width: 30%; margin-left: 30vh" clearable></el-input>
        <el-button type="primary" style="margin-left: 5px" @click="load">查询课程考试</el-button>
      </div>
    </el-col>
  </el-row>
    <el-divider></el-divider>
  <el-table :data="tableData"  v-loading="loading" stripe style="width: 100%; height: 55vh">
    <el-table-column prop="name" align="center" label="考试名" width="240"> </el-table-column>
    <el-table-column prop="courseId" align="center" label="考试课程" width="240"> </el-table-column>
    <el-table-column prop="createdTime" align="center" label="开考时间" width="240" :formatter="dateFormat"> </el-table-column>
    <el-table-column prop="lastTime" align="center" label="考试时长" width="90" > </el-table-column>
    <el-table-column prop="teacherId" align="center" label="创建者"> </el-table-column>
    <el-table-column align="center" label="操作">
      <template #default="scope">
        <el-button  type="primary" v-if="user.userType == 'student'"  @click="enterExam(scope.row)">进入考试</el-button>
       <el-button v-if="user.userType !== 'student' " @click="editPaper(scope.row.examId)">编辑试卷</el-button>
        <el-button v-if="user.userType !== 'student'" @click="findPaper(scope.row.examId)">查看考试结果</el-button>
        <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.examId)" v-if="user.userType !== 'student'">
          <template #reference>
            <el-button  type="danger">删除考试</el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div style="text-align: center">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
  </div>
  <el-dialog title="新建考试" v-model="dialogVisible" width="30%">
    <el-form :model="form" label-width="120px">
      <el-form-item label="课程编号">
        <el-input v-model="form.courseId" style="width: 80%"></el-input>
      </el-form-item>
        <el-form-item label="开考时间">
          <el-date-picker
              v-model="form.startTime"
              type="datetime"
              placeholder="选择日期时间"
              :default-time="this.defaultTime"
          >
          </el-date-picker>

      </el-form-item>
      <el-form-item label="考试时长">
        <el-select v-model="form.lastTime" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import request from "@/utils/request";
import fileDownload from "js-file-download";

export default {
  name: "exam",
  data() {
    return {
      options: [
        {
          value: '1',
          label: '1h',
        },
        {
          value: '1.5',
          label: '1.5h',
        },
        {
          value: '2',
          label: '2h',
        },
        {
          value: '2.5',
          label: '2.5h',
        },

      ],
      fileList: [],
      uploadFileList: [],
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      filesUploadUrl: "http://localhost:9090/exam/add",//后端内容
      ids: [],
      isSelect: true,
      courseList:[],//这个是打算记录当前用户拥有课程的数组
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")
    this.user = JSON.parse(userStr)
    this.load()

    //希望可以返回该教师所有的课程,然后就可以把课程编号的input改成选择框了
  },
  computed: {
    examId() {
      return this.$store.state.curExamId
    }
  },
  methods: {
    httpRequest(param) {
      console.log(param)
      let fileObj = param.file // 相当于input里取得的files
      let fd = new FormData()// FormData 对象
      fd.append('file', fileObj)// 文件对象
      fd.append('courseId', this.courseId)
      let url = this.filesUploadUrl
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      request.post(url, fd, config).then(res=>{
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "新增成功"
          })
          this.fileList=[]
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() // 刷新表格的数据
      })
    },

    submitUpload() {
      this.$refs.upload.submit()
    },
    handleRemove(file, fileList) {
      this.fileList=fileList
    },
    handlePreview(file) {
      console.log(file)
    },
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    handleDownload(path,fileName){
      request.get('/files/download/'+path+fileName, {responseType: 'blob'}).then(res => {
        fileDownload(res, fileName);
      }).catch((res)=>{
            console.log('download error');
          }
      )
    },
    handleDelete(id){//删除考试
      request.post('/exam/delete/'+id).then(res => {
            if(res.code === '0'){
              this.$message({
                type: "success",
                message: "删除成功"})
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
            this.load() // 刷新表格的数据
          }
      )
    },
    editPaper(examId){
      this.$store.commit('setExamId',examId)

      this.$router.push({

        name: 'examEditor',
        params: {
          examId: examId
        }
      })
    },
    findPaper(examId){
      this.$store.commit('setExamId',examId)

      this.$router.push({

        name: 'paperList',
        params: {
          examId: examId
        }
      })
    },

    enterExam(row){
      this.$store.commit('setExamId',row.examId)
      let start = row.createdTime;
      let timeLength = row.lastTime;
      var endingTime = start +timeLength*3600;
      console.log(this.dateFormat2(start))
      console.log(this.dateFormat2(endingTime))
      this.$router.push({

        name: 'examPaper',
        params: {
          examId: row.examId,
          endingTime:endingTime
        }
      })
    },

    save() {
         let userStr = sessionStorage.getItem("user") || "{}"
         let user = JSON.parse(userStr)
         this.form.teacherId = user.id
         //this.form.courseId = this.courseId
         request.post("/exam/add", this.form).then(res => {//不知道和后端的参数是否对应
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
    load() {//加载考试列表
      this.loading = true
      if(this.user.userType == student){
        request.get("/exam/doExams/all", {
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize,
          }
        }).then(res => {
          this.loading = false
          this.tableData = res.data.records
          this.total = res.data.total
        })
      }
      else{
        request.get("/exam/all", {
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
      }

    },


    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    dateFormat(row,column){
      var t=new Date(row.createdTime);//row 表示一行数据, updateTime 表示要格式化的字段名称
      var year=t.getFullYear(),
          month=t.getMonth()+1,
          day=t.getDate(),
          hour=t.getHours(),
          min=t.getMinutes(),
          sec=t.getSeconds();
      var newTime=year+'-'+
          (month<10?'0'+month:month)+'-'+
          (day<10?'0'+day:day)+' '+
          (hour<10?'0'+hour:hour)+':'+
          (min<10?'0'+min:min)+':'+
          (sec<10?'0'+sec:sec);
      return newTime;
    },
    dateFormat2(curTime){
      var t=new Date(curTime);//row 表示一行数据, updateTime 表示要格式化的字段名称
      var year=t.getFullYear(),
          month=t.getMonth()+1,
          day=t.getDate(),
          hour=t.getHours(),
          min=t.getMinutes(),
          sec=t.getSeconds();
      var newTime=year+'-'+
          (month<10?'0'+month:month)+'-'+
          (day<10?'0'+day:day)+' '+
          (hour<10?'0'+hour:hour)+':'+
          (min<10?'0'+min:min)+':'+
          (sec<10?'0'+sec:sec);
      return newTime;
    },

  }
}
</script>

<style scoped>

</style>
