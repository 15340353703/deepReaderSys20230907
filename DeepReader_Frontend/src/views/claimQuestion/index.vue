<template>
  <div class="app-container">
    <el-container>
      <el-main>
        <div style="text-align: center">
          <el-row :gutter="20">
            <div>
              <el-col :span="contentSpan" :xs="24">
                <el-card>
                  <el-tabs v-model="activeTab2">
                    <el-tab-pane label="文章内容" name="myContent">
                      <myContent :book="book" />
                    </el-tab-pane>
                  </el-tabs>
                </el-card>
              </el-col>
            </div>
            <el-col :span="questionSpan" :xs="24">
              <el-card>
                <el-tabs v-model="activeTab">
                  <el-tab-pane label="创建问题" name="strategyForm">
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

export default {
  name: "考考同伴",
  components: { MyContent, StrategyForm },
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
      contentSpan: 12,
      questionSpan: 12,
      active: 2,
      percent: 0,
      user: {},
      major: {},
      activeTab: "strategyForm",
      activeTab2: "myContent",
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
