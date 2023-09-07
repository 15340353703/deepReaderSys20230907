<template>
  <div v-if="active" style="height: 650px; overflow: auto">
    <p v-html="book.content"></p>
  </div>
  <div v-else-if="active1" class="block" style="height: 650px; overflow: auto">
    <!-- <h3 style="text-align: center">《{{ book.name }}》</h3>
    <el-divider content-position="right">作者：{{ book.author }}</el-divider> -->

    <div style="text-align: center">
      <!-- <el-button @click="scalBig" type="success">放大</el-button>
      <el-button @click="scalSmall" type="primary">缩小</el-button>  -->
      <el-button @click="newPage(book.url)" type="primary" style="margin-bottom:10px">文本标注</el-button>
    </div>
    <div class="drag-box" id="dragBox" style="text-align: center">
      <iframe :src="book.url" height="600" width="100%"></iframe>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const PDFJS = require("pdfjs-dist");
import { TextLayerBuilder } from "pdfjs-dist/web/pdf_viewer";
import "pdfjs-dist/web/pdf_viewer.css";
PDFJS.GlobalWorkerOptions.workerSrc =
  "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.6.347/pdf.worker.js";

// console.log("成功：",PDFJS )
export default {
  data() {
    return {
      content: "",
      url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      pdfPages: [], // 页数
      pdfWidth: "", // 宽度
      pdfSrc: "", // 地址
      pdfDoc: "", // 文档内容
      pdfScale: 1.4, // 放大倍数
      active: null,
      active1: null,
      book: null
    };
  },
  mounted() {
    const id = this.$route.query.bookid
    this.getBook(id)
  },
  props: {
    book: {
      type: Object,
      required: true,
    },
  },
  methods: {
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
        if(this.book.content){
          this.active = 1
          this.active1 = 0
        } else {
          this.active = 0
          this.active1 = 1
        }
      });
    },
  },
};
</script>
<style lang="scss">
.preview-pdf {
  h1 {
    margin: 30px auto;
    text-align: center;
    font-family: "宋体";
    letter-spacing: 2px;
  }
}


</style>



