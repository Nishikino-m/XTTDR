<template>
  <div class="examEditor_page">
      <el-button type="primary" @click="handleadd2">从题库添加题目</el-button>
      <el-button type="primary" @click="handleadd">从文件导入题目</el-button>

      <div style="padding: 20px; color: #888">
        <div>
          <div style="text-align: center;width: 100%">考试名称：{{this.examId}}</div>
          现有题目:<br>
          <el-card  v-loading="this.loading">
          <div class="problem" style=" padding: 1%" v-for="item in problems">
            <div class="describes">
              {{item.describes}}

             </div>
            <div class="options" style=" padding: 0 5px;">
              <el-row :gutter="20">
                <el-col :span="6"> A.{{item.optionA}}</el-col>
                <el-col :span="6">B.{{item.optionB}}</el-col>
                <el-col :span="6"> C.{{item.optionC}}</el-col>
                <el-col :span="6">D.{{item.optionD}}</el-col>
              </el-row>

            </div>
            <div class="answer">
              答案: {{item.answer}}<br>
              解析：
            </div>
            <div >
              <div style="text-align: right">
                <el-popconfirm title="确定删除吗？" @confirm="delete(item.problemId)">
                  <template #reference>
                    <el-button  size="mini" type="danger">删除题目</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>
          </div>

          </el-card>
          添加新题: <br>
          <div class="newproblem" style=" padding: 1%">
            <div class="describes">
              题目描述<el-input type="textarea" :rows="3" v-model="entity.describes"></el-input>
            </div>
            <div class="options" style=" padding: 0 5px;">
              <el-row :gutter="20">
                <el-col :span="6"> A<el-input type="text" style="width: 80%;margin-left: 10%;" v-model="entity.optionA"></el-input></el-col>
                <el-col :span="6">B<el-input type="text" style="width: 80% ;margin-left: 10%;"  v-model="entity.optionB"></el-input></el-col>
                <el-col :span="6"> C<el-input type="text" style="width: 80% ;margin-left: 10%;"  v-model="entity.optionC"></el-input></el-col>
                <el-col :span="6">D<el-input type="text" style="width: 80% ;margin-left: 10%;"  v-model="entity.optionD"></el-input></el-col>
              </el-row>

            </div>
            <div class="answer">
              <div style=" height: 30px" >
                答案:
              <el-radio style="color: #241e42;" v-model="entity.answer" label="A">A</el-radio>
              <el-radio style="color: #241e42;" v-model="entity.answer" label="B">B</el-radio>
              <el-radio style="color: #241e42;" v-model="entity.answer" label="C">C</el-radio>
              <el-radio style="color: #241e42;" v-model="entity.answer" label="D">D</el-radio>
            </div>
              <br>
              解析：<el-input type="textarea" v-model="entity.prase"></el-input>
            </div>
            <div style="text-align: right; padding: 10px"><el-button type="primary" @click="addProblem">确认添加</el-button></div>
          </div>



          <div style="text-align: right; padding: 10px"><el-button type="primary" @click="save">保存试卷</el-button></div>
        </div>
      </div>





  </div>
  <el-dialog title="从文件导入" v-model="vis" width="40%">

    <el-upload
        class="upload-demo"
        ref="upload"
        multiple
        action=""
        :limit="1"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-change="handleChange"
        :http-request="httpRequest"
        :file-list="fileList"
        :auto-upload="false"
    >
      <template #trigger>
        <el-button  type="primary">选取文件</el-button>
      </template>
      <el-button
          style="margin-left: 10px;"
          type="success"
          @click="submitUpload"
          :disabled="fileList.length == 0 ? true : false"
      >确认导入{{fileList.length}}
      </el-button>
    </el-upload>
  </el-dialog>
  <el-dialog title="从题库导入" v-model="dialogFormVisible" >
    <div class="repo">
    <el-checkbox
        :indeterminate="isIndeterminate"
        v-model="checkAll"
        @change="handleCheckAllChange"
    >全选</el-checkbox
    >
    <el-checkbox-group
        v-model="checkedProblems"
        @change="handleCheckedProblemsChange"
    >
      <el-checkbox class="problem" v-for="item in problemsRepo" :label="item.problemId" :key="item.problemId">{{item.describes}}
      </el-checkbox>
    </el-checkbox-group>
    </div>

<template #footer>
  <span>选中{{this.checkCount}}条</span>

    <el-button @click="dialogFormVisible = false">取 消</el-button>
    <el-button type="primary" @click="submit" >确 定</el-button>
</template>
  </el-dialog>
</template>

<script>
import E from "wangeditor";
import request from "../utils/request";

export default {
  name: "ExamEditor",

  data(){
    return{
      value1:"",
      problems:[
        {
          problemId:"1",
          describes:"第一个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          prase:"因为......"
        },
        {
          problemId:"2",
          describes:"第2个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          prase:"因为......"
        },
        {
          problemId:"3",
          describes:"第3个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          prase:"因为......"
        },
      ],
      entity:{},
      options: [
        {
          value: 'A',
          label: 'A',
        },
        {
          value: 'B',
          label: 'B',
        },
        {
          value: 'C',
          label: 'C',
        },
        {
          value: 'D',
          label: 'D',
        },
      ],
      dialogFormVisible:false,
      vis:false,
      fileList: [],
      uploadFileList: [],
      problemsRepo: [
        {
          problemId:"2",
          describes:"题库第一个题目描很长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          prase:"因为......"
        },
        {
          problemId:"3",
          describes:"题库第2个题目描述",
          optionA:"122221",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          prase:"因为......"
        },
      ],
      checkAll: false,
      checkedProblems: [],
      isIndeterminate: true,
      checkCount: 0
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")
    this.user = JSON.parse(userStr)
   // this.loadProblems();
  },
  computed: {
    examId() {
      return this.$store.state.curExamId
    }
  },
  methods: {
    loadProblems() {
      request.get("/exam/problems",{//后端相关
        params: {
          examId: this.examId
        }}).then(res => {
        this.problems = res.data.records;
        this.total = res.data.total
        this.loading = false
      })
    },
    addProblem() {
      this.entity.examId = this.examId
      if (!this.user.id) {
        this.$message({
          message: "请登录",
          type: "warning"
        });
        return;
      }
      if (!this.entity.describes|| !this.entity.optionA|| !this.entity.optionB
          || !this.entity.optionC|| !this.entity.optionD || !this.entity.answer) {
        this.$message({
          type: "warning",
          message: "请填写完整"
        });
        return;
      }
      request.post("/exam/addProblem", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "添加成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.entity = {}
        this.loadProblems();
      })
    },
    delete(id) {
      request.post("/exam/problems/delete/" + id).then(res => {
        this.$message({
          message: "删除成功",
          type: "success"
        });
        this.loadProblems()
      })
    },
    handleadd(){
      this.vis = true
    },
    handleadd2(){
      this.dialogFormVisible = true
    },
    httpRequest(param) {
      console.log(param)
      let fileObj = param.file // 相当于input里取得的files
      let fd = new FormData()// FormData 对象
      fd.append('file', fileObj)// 文件对象
      fd.append('homeworkId', this.detail.homeworkId)
      fd.append('studentId',this.user.id)
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
            message: "提交成功"
          })
          this.fileList=[]
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    //从文件添加
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
    handleCheckAllChange(val) {
      this.checkedProblems = val ? problemOptions : []
      this.isIndeterminate = false
    },
    handleCheckedProblemsChange(value) {
      let checkedCount = value.length
      this.checkCount = checkedCount
      this.checkAll = checkedCount === this.problemsRepo.length
      this.isIndeterminate =
          checkedCount > 0 && checkedCount < this.problemsRepo.length
    },
    //从题库添加
    submit(){
        //this.checkedProblems存的是选中的题号
      request.post('/exam/'+this.examId,this.checkedProblems).then( res => {
        if(res.code === '0'){
          this.$message({
            type: "success",
            message: "添加成功"})
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })

      this.loadProblems()
    }
  }
}
</script>

<style scoped>
@import '../assets/css/style.css';
.button {
  padding: 0;
  min-height: auto;
}
.repo >>>.el-checkbox__label {
  display: inline-grid;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow: hidden;
  line-height: 30px;
}
.repo .problem {
  width: 100%;
  height: auto;
}
</style>