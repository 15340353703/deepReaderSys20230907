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
      {{userInfo.yourname}}同学你好，关于《{{book.name}}》,你目前已经提出了{{total}}个问题，提问可以加强阅读内容理解，提升批判性思维能力。系统对您产生的问题进行了以下四个方面的评价，包括提问积极性、问题复杂性、问题重要性以及问题流畅性。
    </el-card>
    <el-divider content-position="center"><span style="color:limegreen">问题质量雷达图</span></el-divider>
    <el-card>
      <div style="height: 300px">
      <RadarChart></RadarChart>
      </div>
      <div ref="questionfeedback">
      <p style="text-align: left"><span style="font-weight: bold">提问积极性</span><br>
        <span v-if="total<=3 && total>0" style="color:#B22222">提问意识比较薄弱，主动性不够高哦，爱因斯坦说过“提出一个问题往往比解决一个问题更重要。”多多提问既可以帮助你更好的理解阅读主要内容，也可以关联以前学到的知识，请根据文章主题句，先提出简单事实类问题，再提深层次问题。简单题根据单句内容，它可以帮助学生回顾和总结阅读内容。</span>
        <span v-else-if="total>3 && total<5" style="color:#B8860B">提问意识不太强烈，多多提问可以增加身边的良师益友来解答的机会，帮助大家共同成长哦! 请根据文章主题句，提出概念性问题、事实性问题、程序性问题等问题，它可以帮助学生总结和理解阅读内容。</span>
        <span v-else style="color:#228B22">提问意识比较强烈，看来你的求知欲和探究欲很强，能发现问题并进行思考，真棒！请继续保持。</span></p>

      <p style="text-align: left"><span style="font-weight: bold">问题难度系数</span><br>
        <span v-if="complexityscore>=2.5" style="color:#228B22">你提出了{{complexityscore['height']}}个深度问题，有自己的思考，进行了深度阅读，请继续努力提出类似深层次问题！</span>
        <span v-else-if="complexityscore>=2" style="color:#B8860B">你提出的深度问题比较少，有价值的问题不能是学生仅凭回忆所学的基础知识，有价值的问题是潜在的、需要挖掘思考的。可以采用以下方法：1.通过比较、归纳、总结、关联不同段落和文章的主要概念进行提问，2．对文章中作者主要观点和论据、研究局限等进行提问，请继续努力!</span>
        <span v-else-if="complexityscore<2" style="color:#B22222">你提出的问题过于简单，对同学的吸引力不够哦，建议使用自己的语言概括一下，或者尝试换个角度思考解决方法：1、用自己语言概括（针对直接照抄文章问题）2、换个角度思考，多角度提问。</span>
        <span v-else>系统还未进行评价，请稍等~</span>
      </p>

      <p style="text-align: left"><span style="font-weight: bold">问题重要性</span><br>
        <span v-if="importancescore<=1.6 && importancescore>0" style="color:#B22222">你提出的问题还不错，但有的不属于文章重点内容哦，注意关注核心内容，比如文章回答的科学问题、主要结论、证据的有效性等。</span>
        <span v-else-if="importancescore>1.6" style="color:#228B22">你提出的问题属于文章核心内容，重点把握不错，再接再厉！</span>
        <span v-else>系统还未进行评价，请稍等~</span>
      </p>

      <p style="text-align: left"><span style="font-weight: bold">问题流畅性</span><br>
        <span v-if="fluencyscore<=2 && fluencyscore>0" style="color:#B22222">你提出的问题结构基本完整并能理解，表达基本清晰，语句基本通顺，可以使用更简洁的语言将问题明确地表达出来，便于他人理解哦！</span>
        <span v-else-if="fluencyscore>2" style="color:#228B22">你提出的问题结构完整并易理解，表达清晰，语句通顺、逻辑可理解，继续加油！</span>
        <span v-else>系统还未进行评价，请稍等~</span>
      </p>

      <p style="text-align: left"><span style="font-weight: bold;">为了提出更深层次的问题，我们首先可以先对文章/段落进行总结，然后根据总结句提出问题。</span></p>
      </div>
    </el-card>
<!--    <el-divider content-position="center"><span style="color:orange">自我总结</span></el-divider>-->
<!--    <el-card class="box-card" shadow="never">-->
<!--      <div slot="header" class="clearfix">-->
<!--        <span>总结句</span>-->
<!--      </div>-->
<!--      <div v-for="o in 4" :key="o" class="text item">-->
<!--        {{'总结 ' + o }}-->
<!--      </div>-->
<!--    </el-card>-->
    <el-divider content-position="center"><span style="color:royalblue">感想</span></el-divider>
    <el-card>
      <el-form :model="questionfeedbackForm">
        <el-form-item label="当读到此反馈报告，请谈谈您的内心感受如何？">
          <el-input v-model="questionfeedbackForm.feeling" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="你觉得此反馈报告对阅读有用吗？1-5分.为什么？">
          <el-rate v-model="questionfeedbackForm.usefulrate" @change="changeRate" style="float: left;margin-top: 7px" />
          <el-input v-model="questionfeedbackForm.usefulcause" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="next(book.id)">下一步</el-button>
        </el-form-item>
      </el-form>
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
      fluencyscore: null,
      importancescore: null,
      complexityscore: null
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  created() {
    const id = this.$route.query.bookid
    // this.showAnnotations()
    this.getuserInfo()
    this.getQuestions(this.currentPage,this.pagesize, id)
  },
  methods: {
    ...mapActions('user',['getUserQuestions']),
    getQuestions(currentPage, pageSize, bookId) {
      this.getUserQuestions({currentPage, pageSize, bookId}).then(res => {
        this.tableData = res.list
        this.total = res.total
        this.currentPage = res.pageNum
        let fluencynum = 0

        for(let i=0;i<this.tableData.length;i++){
          if(this.tableData[i].fluency!==0){
            fluencynum += 1
            console.log(this.tableData[i])
            this.fluencyscore+=this.tableData[i].fluency
            this.importancescore += this.tableData[i].importance
            this.complexityscore += this.tableData[i].complexity
          }
        }
        this.fluencyscore = this.fluencyscore/fluencynum
        this.impotancescore = this.impotancescore/fluencynum
        this.complexityscore = this.complexityscore/fluencynum
      })
    },
    changeRate() {
      this.$forceUpdate();
    },
    ...mapActions('user', ['getInfo']),
    getuserInfo() {
      this.getInfo().then(res => {
        this.userInfo = res
        // this.getUserAnswers(this.userInfo.id, this.book.bookId)
      })
    },
    handleNodeClick(data) {
      console.log(data)
    },
    // showAnnotations() {
    //   Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
    //     group: '4kMmkaAL'
    //     }}).then(res => {
    //     let rowlist = res.data.rows
    //     for(let i=0;i<rowlist.length;i++){
    //       if(rowlist[i]['permissions']['admin'][0].includes(this.userInfo.hypothesisname)){
    //         let comments = []
    //         for(let j=0;j<rowlist.length;j++){
    //           if(rowlist[j]['references']){
    //             if(rowlist[j]['references'][0] === rowlist[i]['id']){
    //               comments.push(rowlist[j]['text'])
    //             }
    //
    //           }
    //         }
    //         this.feedback.push({ 'annotation': rowlist[i]['target'][0]['selector'][2]['exact'], 'text': rowlist[i]['text'], 'comments': comments })
    //       }
    //     }
    //   })
    // },
    next(id) {
      this.$store.state.questionfeedback = this.$refs.questionfeedback.innerText
      this.$store.state.questionfeedbackusefulrate = this.questionfeedbackForm.usefulrate
      this.$store.state.questionfeedbackfeeling = this.questionfeedbackForm.feeling
      this.$store.state.questionfeedbackusefulcause = this.questionfeedbackForm.usefulcause
      this.$router.push({path:'/studyEvaluation/index',query:{bookid:this.book.id}})
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
