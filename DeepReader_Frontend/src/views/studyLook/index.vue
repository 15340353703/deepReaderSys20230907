<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab2" @tab-click="handleclick">
        <el-tab-pane label="章节目录" name="chapter">
          <chapter :book="book"/>
        </el-tab-pane>
        <el-tab-pane label="文章内容" name="myContent">
          <myContent :book="book"/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import MyContent from './components/MyContent'
import Chapter from './components/Chapter'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'

export default {
  name: 'Study',
  components: { MyContent, Chapter },
  data() {
    return {
      book: {
        bookId: '',
        name: '',
        author: '',
        pages: '',
        brief_introduction: '',
        url: ''
      },
      contentSpan: 24,
      questionSpan: 0,
      showContent: true,
      active: 0,
      percent: 0,
      major: {},
      activeTab2: 'myContent'
    }
  },
  computed: {
    ...mapState('books', ['title'])
  },
  created() {
    const id = this.$route.query.bookid
    this.getBook(id)
  },
  methods: {
    ...mapActions('books', ['getBookInfo']),
    getBook(bookId){
      this.getBookInfo(bookId).then(response => {
        this.book = response
        document.title = this.book.name
      })
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
      if (this.activeTab2 == 'chapter') {
        this.activeTab2 = 'myContent'
      } else {
        this.activeTab2 = 'chapter'
      }
    },
    nextPage() {
      this.activeTab2 = 'myContent'
      this.active = 1
    },
    backPage() {
      this.activeTab2 = 'chapter'
      this.active = 0
    },
    closeContent() {
      this.showContent = false
      this.questionSpan = 24
    },
    openContent() {
      this.showContent = true
      this.questionSpan = 10
    },
    changePercent() {
      if (this.percent >= 4) {
        this.percent = 0
      } else {
        this.percent++
      }
    }
  }
}
</script>

<style>
</style>
