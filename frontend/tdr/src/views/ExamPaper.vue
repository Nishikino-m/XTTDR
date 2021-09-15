<template>
  <div class="exam">
  <div style="padding: 20px; color: #888">
    <div>
      <div style="text-align: center;width: 100%">考试名称：{{this.examId}}</div>
      剩余时间：{{this.currentTime}}
      <el-card  v-loading="this.loading">
        <div class="problem" style=" padding: 1%"  v-for="(item,index) in problems" >
          <div class="describes">
            {{item.describes}}

          </div>
          <div class="options" style=" padding: 0 5px;">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-radio style="color: #241e42; " @change="changeInput()" v-model="this.chooseAnswer[index]" label="A">A.{{item.optionA}}</el-radio>
              </el-col>
              <el-col :span="6"><el-radio style="color: #241e42;" @change="changeInput()" v-model="this.chooseAnswer[index]" label="B">B.{{item.optionB}}</el-radio></el-col>
              <el-col :span="6"><el-radio style="color: #241e42;"  @change="changeInput()" v-model="this.chooseAnswer[index]" label="C"> C.{{item.optionC}}</el-radio></el-col>
              <el-col :span="6" ><el-radio style="color: #241e42;"  @change="changeInput()" v-model="this.chooseAnswer[index]" label="D">D.{{item.optionD}}</el-radio></el-col>
            </el-row>

          </div>
          <div class="answer" v-if="answerVis">
            答案: {{item.answer}}<br>
            解析：{{item.prase}}
          </div>

        </div>

      </el-card>

      <div style="text-align: right; padding: 10px"><el-button :disabled="answerVis" type="primary" @click="save">提交答卷</el-button></div>

      <div v-if="answerVis">最终得分：{{score}}</div>
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
      timer: "",//定义一个定时器的变量
      currentTime: new Date(), // 获取当前时间
      lastTime: "",
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
          problemId:"4",
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
      chooseAnswer:[],
      answerVis:false,
      finishCount: 0,
      score:0,
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")
    this.user = JSON.parse(userStr)
     //this.loadProblems();
    for (var i=0;i<this.problems.length;i++)
    {
      this.chooseAnswer.push("E")
    }

    var _this = this; //声明一个变量指向Vue实例this，保证作用域一致

    this.timer = setInterval(function() {
      var t=new Date();
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

      _this.currentTime = newTime;
    }, 1000);
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
    save(){
      if(this.finishCount<this.problems.length){
        this.$message({
          type: "error",
          message: "您还有题目尚未作答"
        })
        return
      }
      this.score=0
      for (var i=0;i<this.problems.length;i++)
      {
        console.log("i="+i)
        console.log(this.problems[i])
        if(this.chooseAnswer[i]==this.problems[i].answer){
          this.score++
        }

      }
      this.answerVis=true
      this.$message({
        type: "success",
        message: "提交成功"
      })

      //这里需要和后端搞一下让数据传到数据库
      request.post("/exam/doExams/judgeScore"+this.examId)
    },
    changeInput(){
      this.finishCount=0
      for (var i=0;i<this.problems.length;i++)
      {
        if(this.chooseAnswer[i]!='E')
        this.finishCount++
      }
      console.log("已作答："+this.finishCount)
    },
    currentTime() {
      setInterval(this.formatDate, 500);
    },
    formatDate() {
      let date = new Date();
      let year = date.getFullYear(); // 年
      let month = date.getMonth() + 1; // 月
      let day = date.getDate(); // 日
      let week = date.getDay(); // 星期
      let weekArr = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];
      let hour = date.getHours(); // 时
      hour = hour < 10 ? "0" + hour : hour; // 如果只有一位，则前面补零
      let minute = date.getMinutes(); // 分
      minute = minute < 10 ? "0" + minute : minute; // 如果只有一位，则前面补零
      let second = date.getSeconds(); // 秒
      second = second < 10 ? "0" + second : second; // 如果只有一位，则前面补零
      this.nowDate = `${year}/${month}/${day} ${hour}:${minute}:${second} ${weekArr[week]}`;
    },
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  }





}

</script>

<style scoped>
@import '../assets/css/style.css';
</style>