<template>
  <el-row :bookList="bookList" :allUser="allUser" :gutter="40" class="panel-group">
    <el-col :lg="6" class="card-panel-col">
      <el-card class="card-panel" style="height:200px" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            在线登录次数 <count-to :start-val="0" :end-val="loginCounts / (allUser.length)" :duration="2600" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            阅读文章数 <count-to :start-val="0" :end-val="readCount / (allUser.length)" :duration="3600" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            未完成文章数 <count-to :start-val="0" :end-val="0" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <el-card class="card-panel" style="height:200px" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="eye-open" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            提问问题数量： <count-to :start-val="0" :end-val="questionCount / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            提问问题复杂度： <count-to :start-val="0" :end-val="complexityscore" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            提问问题流畅性： <count-to :start-val="0" :end-val="fluencyscore" :duration="3000" class="card-panel-num" />
          </div>

          <div class="card-panel-text">
            提问问题重要性： <count-to :start-val="0" :end-val="importancescore" :duration="3000" class="card-panel-num" />
          </div>

        </div>
      </el-card>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <el-card class="card-panel" style="height:200px" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="message" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            已知知识数量： <count-to :start-val="0" :end-val="preKnowCount / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            精读提问和回答数量： <count-to :start-val="0" :end-val="QAcount / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            阅读时长： <count-to :start-val="0" :end-val="readtime / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            文章总结字数： <count-to :start-val="0" :end-val="summarywords / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>

        </div>
      </el-card>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <el-card class="card-panel" style="height:200px" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="eye" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            回答问题数量： <count-to :start-val="0" :end-val="answerCount / (allUser.length*bookList.length)" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            回答问题正确率： <count-to :start-val="0" :end-val="rightnum / answerCount" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            文章批注数量： <count-to :start-val="0" :end-val="highlight" :duration="3000" class="card-panel-num" />
          </div>
          <div class="card-panel-text">
            文章批注质量： 0
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import CountTo from 'vue-count-to'
import service from '@/utils/request'
var echarts = require('echarts/lib/echarts')

export default {
  data() {
    return {
      questionCount: 0,
      answerCount: 0,
      opinionCount: 0,
      avgReadTime: 0,
      readCount: 0,
      unReadCount: 0,
      avgQuestionRate: 0.0,
      beAnsweredCount: 0,
      questionList:null,
      loginCounts:0,
      preKnowCount: 0,
      QAcount: 0,
      readtime: 0,
      summarywords: 0,
      fluencyscore: 0,
      importancescore: 0,
      complexityscore: 0,
      accuracy: 0,
      rightnum: 0,
      highlight:0
    }
  },
  props:['bookList','allUser'],
  watch: {
    bookList(newVal,oldVal){
      this.bookList = newVal
      this.questionCount = 0
      this.answerCount = 0
      this.readCount = 0
      this.preKnowCount = 0
      this.QAcount = 0
      this.readtime = 0
      this.fluencyscore = 0
      this.importancescore = 0
      this.complexityscore = 0
      this.summarywords = 0
      this.accuracy = 0
      this.rightnum = 0
      this.highlight = 0
      this.bookList.forEach((book,index) =>{
        service.get('/dq/getQuestionsBybook',{params:{bookId:book.id}}).then(res=>{
          this.questionCount += res.data.length
          let num = 0

          for(let i=0;i<this.questionCount;i++){
            if(res.data[i]){
              if(res.data[i].fluency!==0){
                num += 1
                this.fluencyscore+=res.data[i].fluency
                this.importancescore += res.data[i].importance
                this.complexityscore += res.data[i].complexity
              }
            }
            if(index===this.bookList.length-1 && num!==0){
              this.fluencyscore = parseFloat(this.fluencyscore/num)
              this.importancescore = this.importancescore/num
              this.complexityscore = this.complexityscore/num
            }
          }
          service.get('/kwl/getKWLBybookId',{params:{bookId:book.id}}).then(res=>{
            this.readCount += res.data.length
            for(let i=0;i<res.data.length;i++){
              this.preKnowCount += JSON.parse(res.data[i].preKnown).length
              this.QAcount += JSON.parse(res.data[i].qa).length
              this.readtime += res.data[i].read_time
              this.summarywords += res.data[i].summary.length
            }
            service.get('/dq/getAnswersBybook',{params:{bookId:book.id}}).then(res=>{
              this.answerCount += res.data.length
              for(let i=0;i<this.answerCount;i++){
                if(res.data[i]){
                  if(res.data[i].judge === '正确'){
                    this.rightnum += 1
                  }
                }
              }
              service.get('/dq/getHighlightsBybook',{params:{bookId:book.id}}).then(res=>{
                for(let j=0;j<res.data.length;j++){
                  if(JSON.parse(res.data[j].readfeedback)[0].feedback[0]){
                    this.highlight += JSON.parse(res.data[j].readfeedback)[0].feedback.length
                  }
                }
              })
            })
          })
        })
      })
    }
  },
  components: {
    CountTo
  },
  created(){
    this.loginCount()
    this.getDataCount()
  },
  methods: {
    ...mapActions('user', ['userDataCount']),
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    getQuestions(){

    },
    loginCount(){
      service.get('/dq/getLoginCount').then(res=>{
        this.loginCounts = res.data
      })
    },
    getDataCount(){
      this.userDataCount().then(response => {
        this.dataCount = response
        this.dataCount.avgQuestionRate=parseFloat(this.dataCount.avgQuestionRate).toFixed(1)
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 130px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
