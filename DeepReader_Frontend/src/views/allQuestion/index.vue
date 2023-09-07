<template>
  <div class="app-container">
    <div>
      <el-row :gutter="20">
        <el-col :span="contentSpan" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab2">
              <el-tab-pane label="章节目录" name="chapter">
                <chapter :book="book" />
              </el-tab-pane>
              <el-tab-pane label="文章内容" name="myContent">
                <myContent :book="book" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
        <el-col :span="questionSpan" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="查看习题" name="questionForm">
                <questionForm :book="book" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import QuestionForm from "./components/QuestionForm";
import MyContent from "./components/MyContent";
import Chapter from "./components/Chapter";

export default {
  components: { MyContent, Chapter, QuestionForm },
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
      major: {},
      active: 0,

      contentSpan: 14,
      questionSpan: 10,
      percent: 0,
      user: {},
      activeTab: "questionForm",
      activeTab2: "chapter",
    };
  },
  computed: {
    ...mapState("books", ["title"]),
  },
  created() {
    const id = this.$route.query.bookid; //获取图书编号
    this.getBook(id)
  },
  methods: {
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
        document.title = this.book.name
      });
    },
  }
};
</script>
