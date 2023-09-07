<template>
  <div>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="13" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读成绩</div>
        <div>
          <TestScoreBarChart v-if="isTestScoreAvailable" :userTestScore = "userTestScore"></TestScoreBarChart>
          <div v-else-if="this.userTestScore.length ===0">目前还没有测试</div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读任务查看情况</div>
        <div class="card-panel-charts"><DailyTaskCount v-if="isTaskCountAvailable" :taskCount="taskCount"></DailyTaskCount></div>
      </div>
<!--      <div style="float: right"><span>明确阅读任务能帮助你更好的制定学习计划哦！</span></div>-->
    </el-col>
    <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读进度</div>
        <div><ReadProgress :courseId="radio"></ReadProgress></div>
      </div>
    </el-col>
  </el-row>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="13" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读速度</div>
        <div><LineChart></LineChart></div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读策略使用情况</div>
        <div><ReadStrtegyCharts></ReadStrtegyCharts></div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">阅读测试得分</div>
        <div><ReadQuizeCharts :courseId="radio"></ReadQuizeCharts></div>
      </div>
    </el-col>
  </el-row>
  <el-row :gutter="20" class="panel-group">
    <el-col :xs="13" :sm="13" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">提问质量</div>
        <div><QuestionSccoreCharts :chartData="this.questionForm"></QuestionSccoreCharts></div>
      </div>
    </el-col>
    <el-col :xs="13" :sm="13" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">仪表盘访问情况</div>
        <div>这里是阅读任务查看情况</div>
      </div>
    </el-col>
    <el-col :xs="13" :sm="13" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-description">学习内容提示/阅读理解水平评估</div>
        <div>这里是阅读任务查看情况</div>
      </div>
    </el-col>
  </el-row>
  </div>
</template>

<script>
  import DailyTaskCount from './DailyTaskCount'
  import ReadProgress from './ReadProgress'
  import ReadStrtegyCharts from './ReadStrtegyCharts'
  import LineChart from './LineChart'
  import ReadQuizeCharts from './ReadQuizeCharts'
  import QuestionSccoreCharts from './QuestionSccoreCharts'
  import TestScoreBarChart from './TestScoreBarChart'
  import { getToken } from '@/utils/auth'
  import service from '@/utils/request'
  import { mapState, mapActions } from 'vuex'
  import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'
export default {
  name: 'DashboardGroup',
  components: { DailyTaskCount,ReadProgress ,ReadStrtegyCharts,LineChart,ReadQuizeCharts,QuestionSccoreCharts,TestScoreBarChart},
  props: {
      value: IntegerObject,
  },
  data() {
    return {
      taskCount: [],
      questionFormList: [{}],
      questionForm: {
        com: 0,
        imp: 0,
        flu: 0
      },
      userTestScore:[],
    }
  },

  created() {
    this.getUserTestScore()
    this.getReadLogList()
    this.getUserQuestionForm(this.radio)


  },
  watch: {
  },
  computed: {
    isTaskCountAvailable() {
      return this.taskCount.length > 0; // 检查 taskCount 是否有数据
    },
    isTestScoreAvailable() {
      return this.userTestScore.length > 0; // 检查 taskCount 是否有数据
    },
    radio: {
      get() {
        return this.value;
      },
      set(newValue) {
        this.$emit('input', newValue); // 触发 v-model 的 input 事件
      },
    },
  },
  methods: {
    getReadLogList() {
      service.get('/dq/getReadlog', { params: { token: getToken() }}).then(res => {
        for(let i = 0; i < res.data.length; i++){
          if(res.data[i].behavior === '查看阅读任务'){
            this.taskCount.push(res.data[i])
          }
        }
        // console.log('hhhh'+this.taskCount.length)
      })
    },
    getUserTestScore() {
      service.get('/user/getUsersTestScore', { params: { token: getToken() }}).then(res => {
       this.userTestScore = res.data

        // console.log('hhhh'+res.data)
      })
    },
    ...mapActions('user', ['getUserCourseQuestionForm', 'getUserCourseAnswerForm']),
    getUserQuestionForm(courseId) {
      this.getUserCourseQuestionForm(courseId).then(res => {
        this.questionFormList = res
        this.dataCount.questionCount = this.questionFormList.length
        let imp = 0
        let flu = 0
        let com = 0
        for (let i = 0; i < this.questionFormList.length; i++) {
          imp += this.questionFormList[i].importance
          flu += this.questionFormList[i].fluency
          com += this.questionFormList[i].complexity
        }
        this.questionForm.imp = (imp / this.questionFormList.length).toFixed(3)
        this.questionForm.flu = (flu / this.questionFormList.length).toFixed(3)
        this.questionForm.com = (com / this.questionFormList.length).toFixed(3)
      })
    },

  }
}
</script>

<style lang="scss" scoped>
  .panel-group {
    margin-top: 6px;

    .card-panel-col {
      margin-bottom: 16px;
    }

    .card-panel {
      height: 330px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      /*overflow: hidden;*/
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
        text-align: center;
        font-weight: bold;
        margin: 16px;
        margin-left: 0px;
        font-size: 16px;
        color: rgba(0, 0, 0, 0.45);

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
