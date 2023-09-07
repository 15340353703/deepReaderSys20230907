<template>
  <div>
    <el-row>
      <el-col>
        <ReadInfo :book="book" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ReadInfo from './ReadInfo'
import { mapState, mapActions } from 'vuex'
export default {
  name: 'Index',
  components: {
    ReadInfo
  },
  data() {
    return {
      book: {
        bookId: '',
        name: '',
        author: '',
        pages: '',
        brief_introduction: '',
        url: ''
      }
    }
  },
  computed: {
    ...mapState('books', ['title'])
  },
  created() {
    this.book.bookId = this.$route.query.bookId // 获取图书编号
    this.getBook(this.book.bookId)
    console.log('这是图书编号' + this.book.bookId)
  },
  methods: {
    ...mapActions('books', ['getBookInfo']),
    getBook(bookId) {
      this.getBookInfo(bookId).then(response => {
        this.book = response
        this.book.bookId = this.$route.query.bookId
      })
    }
  }
}
</script>

<style scoped>

</style>
