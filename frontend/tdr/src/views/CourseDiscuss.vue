<template>
  <div style="margin-top: 10px; margin-bottom: 80px">
    <el-card v-loading="this.loading">
      <div style="padding: 20px; color: #888">
        <div>
          发表评论<el-input type="textarea" :rows="3" v-model="entity.content"></el-input>
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
          <div style="background-color: #eee; padding: 10px" v-if="item.parentComment">{{ item.parentComment.id }}：“{{ item.parentComment.content }}”</div>

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
</template>

<script>
import request from "@/utils/request";

export default {
  name: "CourseDiscuss",
  data() {
    return {
      loading: true,
      user: {},
      comments: [],
      dialogFormVisible: false,
      isCollapse: false,
      entity: {},
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.loadComment();
  },
  computed: {
    courseId(){
      return this.$store.state.curCourseId
    }
  },
  methods: {
    loadComment() {
      request.get("/comment",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          courseId: this.courseId
        }}).then(res => {
        this.comments = res.data.records;
        this.total = res.data.total
        this.loading = false
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    cancel() {
      this.dialogFormVisible = false;
      this.entity = {};
    },
    reReply(id) {
      this.dialogFormVisible = true;
      this.entity.parentId = id;
    },
    reply() {
      this.entity.content = this.entity.reply;
      this.save();
    },
    save() {
      this.entity.courseId = this.courseId
      if (!this.user.id) {
        this.$message({
          message: "请登录",
          type: "warning"
        });
        return;
      }
      if (!this.entity.content) {
        this.$message({
          type: "warning",
          message: "请填写内容"
        });
        return;
      }
      request.post("/comment/add", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "评论成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.entity = {}
        this.loadComment();
        this.dialogFormVisible = false;
      })
    },
    del(id) {
      request.post("/comment/delete/" + id).then(res => {
        this.$message({
          message: "删除成功",
          type: "success"
        });
        this.loadComment()
      })
    },
    dateFormat(createdTime){
      var t=new Date(createdTime);//row 表示一行数据, updateTime 表示要格式化的字段名称
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
