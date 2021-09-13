<template>
  <div class="examEditor_page">


    <el-card v-loading="this.loading">
      <el-button type="primary" @click="handleadd">从题库添加题目</el-button>
      <el-button type="primary" @click="handleadd">从文件导入题目</el-button>
      <div style="padding: 20px; color: #888">
        <div>
          <div style="text-align: center;width: 100%">考试名称：{{this.examId}}</div>
          这里输出现有试卷题目
          <el-input type="textarea" :rows="3" v-model="value1"></el-input>
          <div style="text-align: right; padding: 10px"><el-button type="primary" @click="save">发表</el-button></div>
        </div>
      </div>

      <div style="display: flex; padding: 20px" v-for="item in comments">
        <div style="text-align: center; flex: 1">
          <el-avatar  :src="item.avatar" class="avatar" :size="80" shape="circle"/>
        </div>
        <div style="padding: 0 5px; flex: 5">
          <div><b style="font-size: 14px">{{ item.id }}</b></div>
          <div style="padding: 10px 0; color: #888">
            {{ item.content }}
            <el-button type="text" size="mini" @click="del(item.commentId)" v-if="item.id === user.id">删除</el-button>
          </div>
          <div style="background-color: #eee; padding: 10px" v-if="item.parentComment">{{ item.id }}：“{{item.parentComment.content }}”</div>
          <div style="color: #888; font-size: 12px" >
            <span>{{ dateFormat(item.createdTime)  }}</span>
            <el-button type="text" style="margin-left: 20px" @click="reReply(item.commentId)">回复</el-button>
          </div>
        </div>
      </div>

      <el-dialog title="回复信息" v-model="dialogFormVisible" width="30%">
        <el-form :model="entity" label-width="80px">
          <el-form-item label="内容">
            <el-input v-model="entity.reply" autocomplete="off" type="textarea" :rows="3"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="reply">确 定</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "ExamEditor",

  data(){
    return{
      value1:""
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user")
    this.user = JSON.parse(userStr)
  },
  computed: {
    examId() {
      return this.$store.state.curExamId
    }
  },
}
</script>

<style scoped>

</style>