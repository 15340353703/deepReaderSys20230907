<template>
  <div id="d1">
    <el-row>
      <el-col :span="3">
        -
      </el-col>
      <el-col :span="18">
        <el-card style="text-align: center">
          <h3>讨论主题：{{ this.discussion.topic }}</h3>
          <p>
            <em class="post-meta-item-text">当前评论数：</em>
            <span class="valine-comment-count" :data-xid="urlPath" />
            <br>
            <!-- id 将作为查询条件 -->
            <span :id="urlPath" class="leancloud_visitors" data-flag-title="Your Article Title">
              <em class="post-meta-item-text">当前阅读量： </em>
              <span class="leancloud-visitors-count">1000000</span>
            </span>
          </p>

          <el-divider />
          <H3>讨论内容：</H3>
          <p>{{ this.discussion.content }}</p>
          <el-divider />
          <!--          <p>{{this.discussion.content}}</p>-->
          <h3>本次讨论涉及的文章：</h3>
          <div v-for="(item, id) in this.bookList" :key="id" style="display:inline; margin-right:10px;">
            <el-button style="padding-top: 8px" @click="openDialog(item.id)">《{{ item.name }}》</el-button>
          </div>
          <el-divider />
          <el-link href="#comment" type="primary">回复主题</el-link>
        </el-card>

        <br>
        <br>

        <div id="comment">
          <div id="vcomments" />
        </div>

        <div>
          <el-dialog title="原文" :visible.sync="dialogFormVisible">
            <el-form style="height: 500px; overflow: auto">
              <el-form-item :label-width="formLabelWidth">
                <div>
                  <h3 style="text-align: center">《{{ this.bookList[this.bookIndex].name }}》</h3>
                  <el-divider
                    content-position="right"
                  >作者：{{ this.bookList[this.bookIndex].author }}</el-divider>
                  <div id="dragBox" class="drag-box" style="text-align: center;">
                    <!-- <el-scrollbar style="height:100%;" id="scrollbar">
                      <div class="wrapper" id="pdf-container" >
                        <div
                          v-for="page in pdfPages"
                          :id="`page-${page}`"
                          :key="page"
                          class="pdf-box"
                        >
                          <canvas :key="page" :id="'pdfCanvas' + page" class="bookCanvas"></canvas>
                        </div>
                      </div>
                    </el-scrollbar> -->
                    <iframe :src=" this.bookList[this.bookIndex].url" style="height:600px;width:650px" />
                  </div>
                </div>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button
                type="primary"
                @click="dialogFormVisible = false"
              >关闭</el-button>
            </div>
          </el-dialog>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import Valine from 'valine'
import { mapState, mapActions } from 'vuex'
const PDFJS = require('pdfjs-dist')
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
PDFJS.GlobalWorkerOptions.workerSrc =
  'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.6.347/pdf.worker.js'

export default {

  props: ['setEditorValue', 'remark', 'path'],
  data() {
    return {
      discussionId: 0,
      bookIndex: 0,
      discussion: {
        id: 0,
        userId: 0,
        classifyId: 0,
        createTime: '',
        topic: '',
        content: '',
        userName: '',
        classifyName: ''
      },
      urlPath: this.$route.path,
      valine: null,
      dialogFormVisible: false,
      list: [
        { id: 1, name: 'zs1' },
        { id: 2, name: 'zs2' },
        { id: 3, name: 'zs3' },
        { id: 4, name: 'zs4' },
        { id: 5, name: 'zs5' },
        { id: 6, name: 'zs6' }
      ],
      num1: 0,
      uptype: false,
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      content: '',

      pdfPages: [], // 页数
      pdfWidth: '', // 宽度
      pdfSrc: '', // 地址
      pdfDoc: '', // 文档内容
      pdfScale: 1.4, // 放大倍数

      bookList: [{
        id: 0,
        name: '',
        author: '',
        pages: 0,
        url: ''
      }]
    }
  },
  watch: {
    $route(to, from) {
      if (from.path != to.path) {
        this.valine && this.valine.setPath(to.path)
      }
    }
  },
  created() {
    // alert(this.$route.params.id)
    this.discussionId = this.$route.params.id
    this.getDiscussion(this.discussionId)
    this.viewDiscussion('1000517')

  },
  mounted() {
    this.getPdfUrl(this.bookIndex)
    const vm = this
    vm.$nextTick(() => {
      vm.valine = new Valine({
        el: '#vcomments',
        appId: '8D9uq6yke3Go0sx6r6fJAWf9-gzGzoHsz',
        appKey: 'GiJ44VtSe4pbRreoqF452IwL',
        path: vm.$route.path,
        avatar: 'mp',
        visitor: true, // 阅读量统计
        // meta:['姓名', '学号', 'link']

      })
    })
  },
  methods: {
    ...mapActions('discussion', ['getDiscussionById']),
    ...mapActions('books', ['getBookInfo', 'getBookByClassifyId']),
    getBook(bookId) {
      this.getBookInfo(bookId).then(response => {
        this.book = response
        this.book.bookId = this.$route.query.bookId
      })
    },
    getBookList(classifyId) {
      this.getBookByClassifyId(classifyId).then(response => {
        console.log(response)
        this.bookList = response
        console.log(this.bookList)
      })
    },
    getDiscussion(id) {
      this.getDiscussionById(id).then(response => {
        // console.log(response)
        this.discussion = response
        this.getBookList(this.discussion.classifyId)
      })
    },
    openDialog(id) {
      const data = { userid: getToken(), behavior: '查看文章', bookid: id, stamptime: new Date() }
      service.post('/user/readlog', data).then(res => {
        console.log('记录查看文章')
      })
      for (var i = 0; i < this.bookList.length; i++) {
        if (this.bookList[i].id == id) {
          this.bookIndex = i
          break
        }
      }
      this.dialogFormVisible = true
      this.getPdfUrl(this.bookIndex)
    },
    viewDiscussion(id){
      const data = {userid: getToken(),behavior:'查看讨论',bookid:id,stamptime:new Date()}
      service.post('/user/readlog',data).then(res=>{
        console.log('记录查看讨论')
      })
    },

    getPdfUrl(index) {
      // todo 请求后台，获取pdf的url，这里用的是线上的地址
      this.pdfSrc = this.bookList[index].url
      console.log('this.bookList[index].url:' + this.bookList[index].url)
      this.loadFile(this.pdfSrc)
    },
    loadFile(url) {
      const loadingTask = PDFJS.getDocument(url)
      loadingTask.promise.then((pdf) => {
        this.pdfDoc = pdf
        this.pdfPages = pdf.numPages
        this.$nextTick(() => {
          this.renderPage(1)
        })
      })
    },
    renderPage(num) {
      const that = this
      this.pdfDoc.getPage(num).then((page) => {
        const pageDiv = document.getElementById(`page-${num}`)
        const canvas = document.getElementById('pdfCanvas' + num)
        const ctx = canvas.getContext('2d')
        const dpr = window.devicePixelRatio || 1
        const bsr =
          ctx.webkitBackingStorePixelRatio ||
          ctx.mozBackingStorePixelRatio ||
          ctx.msBackingStorePixelRatio ||
          ctx.oBackingStorePixelRatio ||
          ctx.backingStorePixelRatio ||
          1
        const ratio = dpr / bsr
        const viewport = page.getViewport({ scale: this.pdfScale })
        canvas.width = viewport.width * ratio
        canvas.height = viewport.height * ratio

        canvas.style.width = viewport.width + 'px'

        that.pdfWidth = viewport.width + 'px'

        canvas.style.height = viewport.height + 'px'

        ctx.setTransform(ratio, 0, 0, ratio, 0, 0)
        // 将 PDF 页面渲染到 canvas 上下文中
        const renderContext = {
          canvasContext: ctx,
          viewport: viewport
        }
        page
          .render(renderContext)
          .promise.then(() => {
            return page.getTextContent()
          })
          .then((textContent) => {
            // 创建文本图层div
            const textLayerDiv = document.createElement('div')
            textLayerDiv.setAttribute('class', 'textLayer')

            textLayerDiv.setAttribute('style', ' overflow: auto;')
            // 将文本图层div添加至每页pdf的div中
            pageDiv.appendChild(textLayerDiv)
            // 创建新的TextLayerBuilder实例
            const textLayer = new TextLayerBuilder({
              textLayerDiv: textLayerDiv,
              pageIndex: page.pageIndex,
              viewport: viewport
            })
            textLayer.setTextContent(textContent)
            textLayer.render()
          })
        if (this.pdfPages > num) {
          this.renderPage(num + 1)
        }
      })
    },
    // 放大
    scalBig() {
      this.pdfScale = this.pdfScale + 0.1
      this.getPdfUrl(this.bookIndex)
      var o = document.getElementById('pdfCanvas1')
      var w = o.offsetWidth
      document.getElementById('scrollbar').style.width = w + 'px'
      // alert(w+" : "+document.getElementById("scrollbar").offsetWidth)
    },
    // 缩小
    scalSmall() {
      if (this.pdfScale > 1.4) {
        this.pdfScale = this.pdfScale - 0.1
        this.getPdfUrl(this.bookIndex)
        var o = document.getElementById('pdfCanvas1')
        var w = o.offsetWidth
        document.getElementById('scrollbar').style.width = w + 'px'
      } else {
        this.$message({
          type: 'info',
          message: `action: 无法继续缩小`
        })
      }
    }
  }

}

</script>

<style>
  .w-e-menu {
    z-index: 2 !important;
  }
  .w-e-text-container {
    z-index: 1 !important;
  }
</style>

