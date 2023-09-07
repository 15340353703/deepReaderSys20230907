<template>
  <div id="nav" style="height: 720px">
    <el-scrollbar style="height: 100%">
      <div v-if="unanswered.length!==0">
        <el-row>
          <el-col :span="20">
            <el-progress
              :text-inside="true"
              :stroke-width="20"
              :percentage="this.percentageNum"
              :color="customColors"
            />
          </el-col>
          <el-col :span="4">
            <div style="text-align: center">
              <p>已完成题目：{{ questionIndex.length }}/{{ questionForm.length }}</p>
            </div>
          </el-col>
        </el-row>
        <el-divider />
        <el-card class="box-card">
          <div class="text item">
            接下来请您根据文章内容对以下问题进行回答，您可以随时点击提交答案
          </div>
        </el-card>

        <el-divider><i class="el-icon-mobile-phone" /></el-divider>

        <div style="text-align: center">
          <el-form
            ref="questionForm"
            class="demo-dynamic"
          >
            <div
              v-for="(question, index) in questionForm"
              :key="question.key"
            >
              <div v-if="index===unanswered[0]">
                <el-form-item
                  :label="'问题' + ' : ' + question.finalQuestion"
                  :rules="{
                    required: true,
                    message: '提问不能为空',
                  }"
                >
                  <el-input
                    v-model="question.yourAnswer"
                    type="textarea"
                    :rows="2"
                    placeholder="请作答"
                  />
                </el-form-item>
                <el-form-item label="问题流畅性：" :label-width="formLabelWidth" style="text-align: left">
                  <el-rate
                    v-model="question.fluency"
                    :texts="texts1"
                    show-text="true"
                    :max="3"
                    @change="changeRate"
                  />
                </el-form-item>
                <!--                <el-form-item label="前期知识关联性：" :label-width="formLabelWidth" style="text-align: left" >-->
                <!--                  <el-rate-->
                <!--                   @change="changeRate"-->
                <!--                   v-model="questionList.relevance" :texts="texts2" show-text="true" :max=2 />-->
                <!--                </el-form-item>-->
                <el-form-item label="问题复杂性：" :label-width="formLabelWidth" style="text-align: left">
                  <el-rate
                    v-model="question.complexity"
                    :texts="texts3"
                    show-text="true"
                    :max="3"
                    @change="changeRate"
                  />
                </el-form-item>
                <el-form-item label="问题重要性：" :label-width="formLabelWidth" style="text-align: left">
                  <el-rate
                    v-model="question.importance"
                    :texts="texts4"
                    show-text="true"
                    :max="2"
                    @change="changeRate"
                  />
                </el-form-item>
                <el-form-item label="请对该问题质量优缺点做整体评价以及修改意见">
                  <el-input v-model="question.opinion" placeholder="请输入内容" />
                </el-form-item>
              </div>
            </div>
          </el-form>
        </div>

        <div style="text-align: center">
          <el-button
            size="medium"
            type="primary"
            @click="next"
          >下一题</el-button>
          <el-button
            size="medium"
            type="success"
            @click="end"
          >提交答案</el-button>
          <el-button
            size="medium"
            type="primary"
            @click="openDialog"
          >查看原文</el-button>
        </div>
      </div>

      <div>
        <el-dialog title="原文" :visible.sync="dialogFormVisible">
          <div>
            <div v-if="active1" style="height: 650px; overflow: auto">
              <p v-html="book.content" />
            </div>
            <div v-else-if="active2" class="block" style="height: 650px; overflow: auto">
              <!-- <h3 style="text-align: center">《{{ book.name }}》</h3>
                  <el-divider content-position="right">作者：{{ book.author }}</el-divider> -->

              <div style="text-align: center">
                <!-- <el-button @click="scalBig" type="success">放大</el-button>
                    <el-button @click="scalSmall" type="primary">缩小</el-button>  -->
                <el-button type="primary" style="margin-bottom:10px" @click="newPage(book.url)">文本标注</el-button>
              </div>
              <div id="dragBox" class="drag-box" style="text-align: center">
                <iframe :src="book.url" height="600" width="100%" />
              </div>
            </div>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button
              type="primary"
              @click="dialogFormVisible = false"
            >关闭</el-button>
          </div>
        </el-dialog>
      </div>

      <div>
        <el-dialog title="问题答案" :visible.sync="answerFormVisible">
          <el-form style="height: 500px; overflow: auto">
            <el-form-item
              v-for="(question, index) in questionList"
              :key="question.key"
              :label-width="formLabelWidth"
              :prop="'question.' + index + '.value'"
            >
              <el-card>
<!--                <div>{{ question.question }}</div>-->
                <div>{{ question.finalQuestion }}</div>
                <el-divider />
                <div>参考答案：{{ question.answer }}</div>
                <div v-if="question.yourAnswer==null || question.yourAnswer== ''">未作答</div>
                <div v-if="question.yourAnswer!=null && question.yourAnswer!= ''"> 你的答案：{{ question.yourAnswer }}</div>
              </el-card>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button
              type="primary"
              @click="answerFormVisible = false"
            >关闭</el-button>
          </div>
        </el-dialog>
      </div>

      <div v-if="unanswered.length===0">
        <el-card class="box-card" style="text-align: center">
          <el-button type="success" icon="el-icon-check" circle />
          <div class="text item">
            <br>
            提交成功
          </div>
        </el-card>
        <br>
        <div style="text-align: center">
          <el-button
            size="medium"
            type="success"
            @click="showAnswer"
          >查看答案</el-button>
          <el-button
            size="medium"
            type="primary"
            @click="backExamSelection"
          >返回选择界面</el-button>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
const PDFJS = require('pdfjs-dist')
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
PDFJS.GlobalWorkerOptions.workerSrc =
  'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.6.347/pdf.worker.js'
import Axios from 'axios'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
export default {
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      questionList: [],
      texts1: ['既不流畅，表达也不清晰', '流畅或表达清晰', '流畅且表达清晰'],
      texts2: ['没有利用前期知识产生问题', '有利用前期知识和本文内容产生问题'],
      texts3: ['根据单句内容，直接从阅读材料粘贴复制产生的问题', '根据对单句的理解，灵活使用自己的语言描述问题', '根据对多个句子、段落或者篇章的理解，灵活使用自己的语言描述问题'],
      texts4: ['没有涉及文中重要的主题或观点', '有涉及文中重要的主题或观点'],
      questionIndex: [],
      unanswered: [],
      questionRate: 5,
      answered: 0,
      questionNum: 0,
      content: '',
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      dialogFormVisible: false,
      answerFormVisible: false,
      percentageNum: 0,
      choiceData: [],
      active: 0,
      question: '',
      judge: '',
      questionForm: [
        {
          id: '',
          question: '',
          answer: '',
          yourAnswer: '',
          type: '',
          clues: '',
          robotQuestion: '',
          rate: '',
          modifyQuestion: '',
          userId: '',
          bookId: '',
          opinion: '',
          fluency: '',
          importance: '',
          complexity: '',
          finalQuestion: ''
        }
      ],
      transferProps: {
        key: 'key',
        label: 'value'
      },
      customColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#6f7ad3', percentage: 100 }
      ],

      pdfPages: [], // 页数
      pdfWidth: '', // 宽度
      pdfSrc: '', // 地址
      pdfDoc: '', // 文档内容
      pdfScale: 1.4, // 放大倍数
      active1: null,
      active2: null,
      bookid: null
    }
  },

  mounted() {
    // this.getPdfUrl();
  },
  computed: {
    ...mapState('user', ['name'])
  },
  created() {
    // console.log("0:"+this.book.bookId) viewBookAtExam
    this.bookid = this.$route.query.bookid // 获取图书编号
    this.getBook(this.bookid)
  },
  methods: {
    ...mapActions('books', ['getBookInfo']),
    getBook(bookId) {
      this.getBookInfo(bookId).then(response => {
        this.book = response
        if (this.book.content) {
          this.active1 = 1
          this.active2 = 0
        } else {
          this.active1 = 0
          this.active2 = 1
        }
        this.getQuestionForm(this.book.id)
      })
    },
    ...mapActions('user', ['getQuestionFormByBookId', 'submitAnswer', 'viewBookAtExam']),
    getQuestionForm(bookId) {
      console.log('bookId:' + bookId)
      this.getQuestionFormByBookId(bookId).then(response => {
        this.questionForm = response
        this.questionList = []
        for (var i = 0; i < this.questionForm.length; i++) {
          console.log('所有问题：' + this.questionForm[i].question)
          if (this.questionForm[i].youranswer !== null) {
            console.log('已经回答的问题：' + this.questionForm[i].question)
            this.questionIndex.push(i)
            this.answered++
            this.percentageNum = Math.round((this.questionIndex.length / this.questionForm.length) * 100)
            this.questionList.push(this.questionForm[i])
          } else {
            if (this.questionForm[i].modifyQuestion !== null && this.questionForm[i].modifyQuestion !== ''){
              this.questionForm[i].finalQuestion = this.questionForm[i].modifyQuestion
            } else {
              this.questionForm[i].finalQuestion = this.questionForm[i].question
            }
            this.unanswered.push(i)
          }
        }
      })
    },
    changeRate() {
      this.$forceUpdate()
    },
    backExamSelection() {
      this.$router.push('/examSelection/index')
    },
    failNextQuestion() {
      this.$alert('回答和评价不可以为空，请认真回答问题！', '失败', {
        confirmButtonText: '确定'
      })
    },
    next() {
      if (this.questionIndex.length < this.questionForm.length) {
        if (this.questionForm[this.unanswered[0]].yourAnswer == null || this.questionForm[this.unanswered[0]].yourAnswer === '' ||
          this.questionForm[this.unanswered[0]].fluency === 0 || this.questionForm[this.unanswered[0]].importance === 0 || this.questionForm[this.unanswered[0]].complexity === 0) {
          this.failNextQuestion()
        } else {
          this.nextQuestion()
        }
      } else {
        if (this.questionForm[this.questionIndex].yourAnswer == null || this.questionForm[this.questionIndex].yourAnswer === '' ||
          this.questionForm[this.questionIndex].fluency === 0 || this.questionForm[this.questionIndex].importance === 0 || this.questionForm[this.questionIndex].complexity === 0) {
          this.failNextQuestion()
        } else {
          this.percentageNum = 100
          this.answered = this.questionForm.length
          this.noQuestion()
        }
      }
      this.questionRate = 5
    },
    noQuestion() {
      this.$confirm('已经没有其他问题了，是否结束答题并提交当前答案?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const questionid = this.unanswered.shift()
          var form = this.questionForm[questionid]
          const data = { answer: form.answer, a: form.yourAnswer }
          Axios.post('http://222.198.118.211:9091/similarity', data).then((res) => {
            console.log('res=>', res.data.score)
            if (parseFloat(res.data.score) > 0.8) {
              this.judge = '正确'
            } else {
              this.judge = '错误'
            }
            var answerForm = {
              'bookId': form.bookId,
              'questionId': form.id,
              'answer': form.yourAnswer,
              'fluency': form.fluency,
              // 'relevance':form.relevance,
              'complexity': form.complexity,
              'importance': form.importance,
              'opinion': form.opinion,
              'judge': this.judge
            }
            this.submitAnswer(answerForm)
            this.questionList.push(this.questionForm[questionid])
            this.active = 3
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消提交'
          })
        })
    },
    nextQuestion() {
      this.$confirm('是否提交该题答案并继续回答下一题?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const questionid = this.unanswered.shift()
          var form = this.questionForm[questionid]
          const data = { answer: form.answer, a: form.yourAnswer }

          Axios.post('http://222.198.118.211:9091/similarity', data).then((res) => {
            this.loading = false
            console.log('res=>', res.data.score)
            if (parseFloat(res.data.score) > 0.8) {
              this.judge = '正确'
            } else {
              this.judge = '错误'
            }
            var answerForm = {
              'bookId': form.bookId,
              'questionId': form.id,
              'answer': form.yourAnswer,
              'fluency': form.fluency,
              // 'relevance':form.relevance,
              'complexity': form.complexity,
              'importance': form.importance,
              'opinion': form.opinion,
              'judge': this.judge
            }
            this.submitAnswer(answerForm)
            this.questionList.push(this.questionForm[questionid])
            this.questionIndex.push(questionid)
            this.percentageNum = Math.round((this.questionIndex.length / this.questionForm.length) * 100)
            this.answered++
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消提交'
          })
        })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    end() {
      if (this.questionForm[this.unanswered[0]].yourAnswer == null || this.questionForm[this.unanswered[0]].yourAnswer === '' ||
        this.questionForm[this.unanswered[0]].fluency === 0 || this.questionForm[this.unanswered[0]].importance === 0 || this.questionForm[this.unanswered[0]].complexity === 0) {
        this.failNextQuestion()
      } else {
        this.$confirm('是否结束答题并提交当前答案?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const questionid = this.unanswered.shift()
            var form = this.questionForm[questionid]
            const data = { answer: form.answer, a: form.yourAnswer }
            Axios.post('http://222.198.118.211:9091/similarity', data).then((res) => {
              this.loading = false
              console.log('res=>', res.data.score)
              if (parseFloat(res.data.score) > 0.8) {
                this.judge = '正确'
              } else {
                this.judge = '错误'
              }
              var answerForm = {
                'bookId': form.bookId,
                'questionId': form.id,
                'answer': form.yourAnswer,
                'fluency': form.fluency,
                // 'relevance':form.relevance,
                'complexity': form.complexity,
                'importance': form.importance,
                'opinion': form.opinion,
                'judge': this.judge
              }
              this.submitAnswer(answerForm)
              this.questionList.push(form)
              this.active = 3
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '取消提交'
            })
          })
      }
    },
    handleChange(val) {
      console.log(val)
    },
    openDialog() {
      const data = { userid: getToken(), behavior: '查看原文', bookid: this.book.id, stamptime: new Date() }
      service.post('/user/readlog', data).then(res => {
        console.log('查看原文')
      })
      this.dialogFormVisible = true
      // this.getPdfUrl()
      this.viewBookAtExam(this.book.id)
    },
    showAnswer() {
      console.log('questionList:' + this.questionList)
      const data = { userid: getToken(), behavior: '查看答案', bookid: this.book.id, stamptime: new Date() }
      service.post('/user/readlog', data).then(res => {
        console.log('查看答案')
      })
      this.answerFormVisible = true
    },
    newPage(url) {
      window.open(url)
    }
  }
}
</script>
<style lang="scss">
.el-scrollbar__wrap {
  overflow-x: hidden;
}

.preview-pdf {
  h1 {
    margin: 30px auto;
    text-align: center;
    font-family: "宋体";
    letter-spacing: 2px;
  }
}

</style>

