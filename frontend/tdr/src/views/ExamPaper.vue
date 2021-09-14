<template>
  <div class="exam">
  <div style="padding: 20px; color: #888">
    <div>
      <div style="text-align: center;width: 100%">考试名称：{{this.examId}}</div>
      剩余时间：
      <el-card  v-loading="this.loading">
        <div class="problem" style=" padding: 1%" v-for="item in problems">
          <div class="describes">
            {{item.describes}}

          </div>
          <div class="options" style=" padding: 0 5px;">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-radio style="color: #241e42;" v-model="item.chooseAnswer" label="A">A.{{item.optionA}}</el-radio>
              </el-col>
              <el-col :span="6"><el-radio style="color: #241e42;" v-model="item.chooseAnswer" label="B">B.{{item.optionB}}</el-radio></el-col>
              <el-col :span="6"><el-radio style="color: #241e42;" v-model="item.chooseAnswer" label="C"> C.{{item.optionC}}</el-radio></el-col>
              <el-col :span="6" ><el-radio style="color: #241e42;" v-model="item.chooseAnswer" label="D">D.{{item.optionD}}</el-radio></el-col>
            </el-row>

          </div>
          <div class="answer" v-if="answerVis">
            答案: {{item.answer}}<br>
            解析：
          </div>

        </div>

      </el-card>

      <div style="text-align: right; padding: 10px"><el-button type="primary" @click="save">提交答卷</el-button></div>
    </div>
  </div>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "ExamPaper",
  data(){
    return{
      problems:[
        {
          problemId:"1",
          describes:"第一个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A",
          chooseAnswer:"",
          prase:"因为......"
        },
        {
          problemId:"2",
          describes:"第2个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A", chooseAnswer:"",
          prase:"因为......"
        },
        {
          problemId:"3",
          describes:"第3个题目描述",
          optionA:"11111",
          optionB:"11111",
          optionC:"11111",
          optionD:"11111",
          answer:"A", chooseAnswer:"",
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
      answerVis:false,
      finishCount: 0,

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
    //还需要写 计算剩余时间的,时间到了自动胶卷
    //交卷的， 题目未作答完全弹个提示
    //显示答案的条件：当前时间考试已经结束 or 直接写一个新的学生查看试卷界面
  }
}

</script>

<style scoped>
@import '../assets/css/style.css';
</style>