<template>
  <div class="app-container">
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="8">
            <div style="text-align: left">-</div>
          </el-col>
          <el-col :span="8">
            <el-steps :active="percent" finish-status="success">
              <el-step title="泛读提问" />
              <el-step title="精读回答" />
              <el-step title="读后反思" />
              <el-step title="考考同伴" />
            </el-steps>
          </el-col>
          <el-col :span="8">
            <div style="text-align: right">-</div>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <div>
          <el-row :gutter="20">
            <div>
              <el-col v-if="showContent" :span="contentSpan" :xs="24">
                <el-card>
                  <el-tabs v-model="activeTab2">
                    <el-tab-pane v-if="active == 0" name="chapter">
                      <chapter :book="book" />
                    </el-tab-pane>
                    <el-tab-pane v-if="active == 1" name="myContent">
                      <myContent :book="book" />
                    </el-tab-pane>
                  </el-tabs>
                </el-card>
              </el-col>
            </div>

            <el-col :span="questionSpan" :xs="24">
              <el-card>
                <el-tabs v-model="activeTab">
                  <el-tab-pane name="strategyForm">
                    <strategyForm
                      :user="user"
                      :book="book"
                      @close="closeContent()"
                      @next="nextPage()"
                      @open="openContent()"
                      @back="backPage()"
                      @active="changePercent()"
                      @requestion="reQuestion()"
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
import MyContent from "./components/MyContent";
import StrategyForm from "./components/StrategyForm";
import Chapter from "./components/Chapter";
import StartRead from './components'
import Axios from "axios";
import qs from 'qs';

export default {
  name: "Study",
  components: { MyContent, StrategyForm, Chapter,StartRead },
  data() {
    return {
      kwlread: 0,
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
      showContent: true,
      active: 0,
      percent: 0,
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
    // const kwlrea = this.$route.query.kwlread;
    // document.title = this.book.name
    this.getBook(id)
    // this.selectReadStrategy(kwlrea)
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
    closeContent() {
      this.showContent = false;
      this.questionSpan = 24;
    },
    openContent() {
      this.showContent = true;
      this.questionSpan = 12;
    },
    changePercent() {
      if (this.percent >= 4) {
        this.percent = 0;
      } else {
        this.percent++;
      }
    },
    reQuestion() {
      this.percent = 3;
    },
    showScore() {
      let data = {
        answer: "毛泽东故乡是湖南。",
        a: "湖南。",
      };
      Axios.post("http://222.198.118.211:9090/similarity",data).then((res) => {
        console.log("res=>", res);
      });
    },
    showQuestion(){
      let data = {
        answer: "毛泽东故乡是湖南。",
        clue: "湖南。",
      };
      Axios.post("http://222.198.118.211:8888/deepreader",data).then((res) => {
        console.log("res=>", res);
        alert("问题生成成功！")
      });
    }
  },
};
</script>

<style>
</style>
