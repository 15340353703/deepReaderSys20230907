<template>
  <div v-if="active" style="height: 650px; overflow: auto">
    <p v-html="book.content" />
  </div>
  <div v-else-if="active1" class="block" style="height: 650px; overflow: auto">
    <!-- <h3 style="text-align: center">《{{ book.name }}》</h3>
    <el-divider content-position="right">作者：{{ book.author }}</el-divider> -->

    <div style="text-align: center">
      <!-- <el-button @click="scalBig" type="success">放大</el-button>
      <el-button @click="scalSmall" type="primary">缩小</el-button>  --><br/><br/>
      <el-button type="primary" style="margin-bottom:10px" @click="newPage(book.url)">文本标注</el-button>
      <el-button @click="readEnd">结束阅读</el-button>
      <div style="text-align: center">
        <h4>阅读时间：{{ this.timeData }}</h4>
      </div>

    </div>
    <div id="dragBox" class="drag-box" style="text-align: center">
      <iframe :src="book.url" height="600" width="100%" />
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
const PDFJS = require('pdfjs-dist')
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
PDFJS.GlobalWorkerOptions.workerSrc =
  'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.6.347/pdf.worker.js'
export default {
  name: 'Index',
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      content: '',
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      pdfPages: [], // 页数
      pdfWidth: '', // 宽度
      pdfSrc: '', // 地址
      pdfDoc: '', // 文档内容
      pdfScale: 1.4, // 放大倍数,
      active: null,
      active1: null,
      book: null,
      timeData: '',
      readTime: 0,
      timer: 0,
      timeDataEnd: false,
      dynamicValidateForm: {
        bookId: '',
        time: 0,
        knows: [
          {
            value: ''
          }
        ],
        doubts: [
          {
            value: '',
            result: ''
          }
        ],
        summary: {
          content: '',
          rate: 0
        }
      }
    }
  },
  created() {
    const id = this.$route.query.bookid
    this.getBook(id)
    this.timeCount()
    // this.getPdfUrl();
  },
  methods: {
    ...mapActions('books', ['getBookInfo']),
    ...mapActions('user', ['updateUserKWLForm']),
    readEnd() {
      this.timeDataEnd = true
      this.dynamicValidateForm.time = this.readTime
      this.dynamicValidateForm.bookId = this.book.id
      this.updateUserKWLForm(this.dynamicValidateForm)
      const data = { userid: getToken(), behavior: '创建问题', bookid: this.book.id, stamptime: new Date() }
      service.post('/user/readlog', data).then(res => {
        console.log('记录创建问题')
      })
      this.$router.push({ path: '/claimQuestion/index', query: { bookid: this.book.id }})
    },
    timeCount() {
      // 会议计时
      let tempTime = 0
      let second = 0
      let minute = 0
      this.timeData = ''
      this.timer = setInterval(() => {
        tempTime += 1
        minute = parseInt(tempTime % 3600 / 60) // 转分钟
        second = parseInt(tempTime % 3600 % 60) // 转秒
        // 格式化
        minute = (minute > 9 ? minute : '0' + minute) + ''
        second = (second > 9 ? second : '0' + second) + ''

        this.timeData = minute + ':' + second
        this.readTime = parseInt(minute) * 60 + parseInt(second)
      }, 1000)
      if (this.timeDataEnd) {
        window.clearInterval(this.timer)
      }
    },
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response
        if (this.book.content) {
          this.active = 1
          this.active1 = 0
        } else {
          this.active = 0
          this.active1 = 1
        }
      })
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
      this.getPdfUrl()
      var o = document.getElementById('pdfCanvas1')
      var w = o.offsetWidth
      document.getElementById('scrollbar').style.width = w + 'px'
      // alert(w+" : "+document.getElementById("scrollbar").offsetWidth)
    },
    // 缩小
    scalSmall() {
      //     if (this.pdfScale > 1.4) {
      //       this.pdfScale = this.pdfScale - 0.1;
      //       this.getPdfUrl()
      //       var o=document.getElementById("pdfCanvas1");
      // 　　  var w=o.offsetWidth;
      //       document.getElementById("scrollbar").style.width=w+"px"
      //     }else{
      //       this.$message({
      //         type: 'info',
      //         message: `action: 无法继续缩小`
      //       })
      //     }
      this.pdfScale = this.pdfScale - 0.1
      this.getPdfUrl()
      var o = document.getElementById('pdfCanvas1')
      var w = o.offsetWidth
      document.getElementById('scrollbar').style.width = w + 'px'
    },
    newPage(url) {
      window.open(url)
    }
  }
}
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
