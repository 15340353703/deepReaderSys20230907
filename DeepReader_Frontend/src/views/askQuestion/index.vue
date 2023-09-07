<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success">
      <el-step title="查看问题反馈报告"></el-step>
      <el-step title="文章总结"></el-step>
      <el-step title="深度提问"></el-step>
    </el-steps>
    <el-container>
      <el-main>
        <div style="text-align: center">
          <el-row :gutter="20">
            <div>
              <el-col :span="contentSpan" :xs="24">
                <el-card>
                  <el-tabs v-model="activeTab2">
                    <el-tab-pane label="提问能力反馈报告" name="chapter">
                      <chapter :book="book" />
                    </el-tab-pane>
<!--                    <el-tab-pane label="班级高分高亮文本" name="classhighlight">-->
<!--                      <class-highlight :book="book" />-->
<!--                    </el-tab-pane>-->
                    <el-tab-pane label="文章内容" name="myContent" @tab-click="handleclick">
                      <myContent :book="book" />
                    </el-tab-pane>
                  </el-tabs>
                </el-card>
              </el-col>
            </div>
            <el-col :span="questionSpan" :xs="24">
              <el-card>
                <el-tabs v-model="activeTab">
                  <el-tab-pane label="创建深度问题" name="strategyForm">
                    <strategyForm
                      :user="user"
                      :book="book"
                      @close="closeContent()"
                      @next="nextPage()"
                      @open="openContent()"
                      @back="backPage()"
                      @active="changePercent()"
                    />
                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import StrategyForm from "./components/StrategyForm";
import MyContent from "./components/MyContent";
import Chapter from "./components/Chapter";
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
// import ClassHighlight from "../studyEvaluation/components/ClassHighlight"

export default {
  name: "Study",
  components: { MyContent, StrategyForm, Chapter },
  data() {
    return {
      book: {
        bookId: "",
        name: "",
        author: "",
        pages: "",
        brief_introduction: "",
        url: "",
      },
      contentSpan: 14,
      questionSpan: 10,
      active: 2,
      percent: 0,
      user: {},
      major: {},
      activeTab: "strategyForm",
      activeTab2: "chapter",
    };
  },
  computed: {
    ...mapState("books", ["title"]),
  },
  created() {
    const id = this.$route.query.bookid; //获取图书编号
    this.getBook(id);
  },
  methods: {
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
        document.title = this.book.name
      });
    },
    handleclick(obj){
      const id = this.$route.query.bookid;
      if(obj.name==='myContent') {
        const data = {userid: getToken(),behavior:'查看文章',bookid:id,stamptime:new Date()}
        service.post('/user/readlog',data).then(res=>{
          console.log('记录查看文章')
        })
      }
    },
    changeCard() {
      if (this.activeTab2 == "chapter") {
        this.activeTab2 = "myContent";
      } else {
        this.activeTab2 = "chapter";
      }
    },
    nextPage() {
      this.activeTab2 = "myContent";
      this.active = 1;
    },
    backPage() {
      this.activeTab2 = "chapter";
      this.active = 0;
    },
    changePercent() {
      if (this.percent >= 4) {
        this.percent = 0;
      } else {
        this.percent++;
      }
    },
  },
};
</script>

<style>
</style>
