<template>
  <div class="block" style="height: 650px; overflow: auto">
    <!--    <h3 style="text-align: center">《{{book.name}}》标注文本</h3>-->
    <!--    <el-divider content-position="right">作者：{{book.author}}</el-divider>-->
    <!--    <div v-for="(fb,index) in this.feedback">-->
    <!--      <h4 style="text-align: left">标注内容：{{fb['text']}}</h4>-->
    <!--      <p style="text-align: left">-->
    <!--        高亮文本：{{fb['annotation']}}-->
    <!--      </p>-->
    <!--      <p style="text-align: left" v-for="(comment, index) in fb['comments']">-->
    <!--        同伴评价：{{comment}}-->
    <!--      </p>-->
    <!--    </div>-->
    <el-card shadow="never" style="font-weight: bold">
      {{userInfo.name}}同学你好，关于{{book.name}},你目前已经提出了{{total}}个问题，<span v-if="total<=3">提问意识比较薄弱，主动性不够高哦，爱因斯坦说过“提出一个问题往往比解决一个问题更重要。”多多提问既可以帮助你更好的理解阅读主要内容，也可以关联以前学到的知识，请根据文章主题句，先提出简单事实类问题，再提深层次问题。简单题根据单句内容，它可以帮助学生回顾和总结阅读内容。</span>
      <span v-else-if="total>3 && total<5">提问意识不太强烈，多多提问可以增加身边的良师益友来解答的机会，帮助大家共同成长哦! 请根据文章主题句，提出概念性问题、事实性问题、程序性问题等问题，它可以帮助学生总结和理解阅读内容。</span>
      <span v-else>提问意识比较强烈，看来你的求知欲和探究欲很强，能发现问题并进行思考，真棒！请继续保持。</span>
    </el-card>
    <el-divider content-position="center"><span style="color:limegreen">问题质量雷达图</span></el-divider>
    <el-card>
      <RadarChart></RadarChart>
      <p style="text-align: left"><span style="font-weight: bold">问题流畅性</span><br>
        <span v-if="fluence>=2">你提出的问题结构完整并易理解，表达清晰，语句通顺、逻辑可理解，继续加油！</span>
        <span v-else-if="fluence>=1 && fluence<2">你提出的问题表达基本清晰，语句基本通顺，可以使用简洁的语言将问题明确地表达出来，便于他人理解哦！</span>
        <span v-else>你提出的问题结构不完整，表达不够清晰，逻辑有误，语句中可能存在语法错误或者错别字，请仔细检查！</span></p>

      <p style="text-align: left"><span style="font-weight: bold">问题重要性</span><br>
        <span v-if="importance>=2">你提出的问题属于文章核心内容，重点把握不错，再接再厉！</span>
        <span v-else-if="importance>=1 && importance<2">你提出的问题还不错，但有的不属于文章重点内容哦，注意关注核心内容，比如从作者观点、研究方法、结论等角度进提问，提高问题质量，帮助同学更好地理解文章。</span>
        <span v-else>你提出的问题不属于文章核心内容，想一想哪些是文章重点内容？可以回顾一下总结句或者把握一下文章大致结构和行文逻辑。</span></p>

      <p style="text-align: left"><span style="font-weight: bold">问题难度系数</span><br>
        <span v-if="complexity['easy']>0 && complexity['medium']===0 && complexity['height'] === 0">你提出了{{complexity['easy']}}个简单问题。你提出的问题过于简单，对同学的吸引力不够哦，建议使用自己的语言进行多角度提问。比如换个其他方式表达。它可以帮助学生回顾和总结阅读内容}</span>
        <span v-else-if="complexity['medium']>0">你提出了{{complexity['medium']}}个中等问题，有价值的问题不能是学生仅凭回忆所学的基础知识。你可以尝试提出更深层次问题：1.通过比较、归纳、总结、关联不同段落和文章的主要概念进行提问；2. 对文章中作者主要观点和论据、研究局限等进行提问，激发学生更深层次批判性思考以及引起深度讨论等</span>
        <span v-else-if="complexity['height']>0">你提出了{{complexity['height']}}个深度问题，有自己的思考，进行了深度阅读，请继续努力提出类似深层次问题！</span></p>

    </el-card>
  </div>
</template>

<script>
import Axios from 'axios'
import { mapActions, mapState} from 'vuex'
import RadarChart from './RaddarChart'
export default {
  name: 'Chapter',
  components: {
    RadarChart
  },
  data() {
    return {
      book: null,
      tableData: null,
      currentPage: 1,
      pageSize: 8,
      total: 0,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      feedback: [],
      userInfo: '',
      questionfeedbackForm: {
        questionfeedback: '',
        feeling: this.$store.state.questionfeedbackfeeling,
        usefulrate: this.$store.state.questionfeedbackusefulrate,
        usefulcause: this.$store.state.questionfeedbackusefulcause
      },
      answerlist: null,
      fluence: '',
      importance: '',
      complexity: {}
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  created() {
    this.showAnnotations()
    this.getuserInfo()
    this.getQuestions(this.currentPage,this.pagesize, this.book.bookId)
  },
  methods: {
    ...mapActions('user',['getAnswerForm']),
    getUserAnswers(userId, bookId){
      this.getAnswerForm({userId, bookId}).then(res => {
        this.answerlist = res
        let fluencyscore = 0
        let importancescore = 0
        let complexityscore = {'easy':0, 'medium': 0, 'height':0}
        for(let i=0;i<this.answerlist.length;i++){
          fluencyscore += this.answerlist[i].fluency
          importancescore += this.answerlist[i].importance
          if(this.answerlist[i].complexity === 1){
            complexityscore['easy'] += 1
          } else if(this.answerlist[i].complexity === 2){
            complexityscore['medium'] += 1
          } else if(this.answerlist[i].complexity === 3){
            complexityscore['height'] += 1
          }
        }
        this.fluence = Math.floor(fluencyscore / this.answerlist.length)
        this.importance = Math.floor(importancescore / this.answerlist.length)
        this.complexity = complexityscore
      })
    },
    ...mapActions('user',['getUserQuestions']),
    getQuestions(currentPage, pageSize, bookId) {
      console.log('bookId:'+bookId)
      this.getUserQuestions({currentPage, pageSize, bookId}).then(res => {
        this.tableData = res.list
        this.total = res.total
        this.currentPage = res.pageNum
      })
    },
    changeRate() {
      this.$forceUpdate();
    },
    ...mapActions('user', ['getInfo']),
    getuserInfo() {
      this.getInfo().then(res => {
        this.userInfo = res
        console.log('answerbookId:'+this.book.bookId)
        this.getUserAnswers(this.userInfo.id, this.book.bookId)
      })
    },
    handleNodeClick(data) {
      console.log(data)
    },
    showAnnotations() {
      Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          group: '4kMmkaAL'
        }}).then(res => {
        let rowlist = res.data.rows
        for(let i=0;i<rowlist.length;i++){
          if(rowlist[i]['permissions']['admin'][0].includes(this.userInfo.username)){
            let comments = []
            for(let j=0;j<rowlist.length;j++){
              if(rowlist[j]['references']){
                if(rowlist[j]['references'][0] === rowlist[i]['id']){
                  comments.push(rowlist[j]['text'])
                }

              }
            }
            this.feedback.push({ 'annotation': rowlist[i]['target'][0]['selector'][2]['exact'], 'text': rowlist[i]['text'], 'comments': comments })
          }
        }
      })
    },
    next(id) {
      this.$store.state.questionfeedbackusefulrate = this.questionfeedbackForm.usefulrate
      this.$store.state.questionfeedbackfeeling = this.questionfeedbackForm.feeling
      this.$store.state.questionfeedbackusefulcause = this.questionfeedbackForm.usefulcause
      this.$router.push('/studyEvaluation/index?bookId='+id)
    }
  }
}
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
