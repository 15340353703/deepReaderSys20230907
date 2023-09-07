<template>
  <div class="dashboard-editor-container">
    <div style="text-align:center">
      <el-card>
        <el-radio :key="0"  v-model="radio" :label="0">全部</el-radio>
        <el-radio v-for="(item, id) in courseList.slice(17,)" :key="id"  v-model="radio" :label="item.id">{{item.courseName}}</el-radio>
      </el-card>
    </div>
    <panel-group :bookList="bookList" :allUser="allUsers" @handleSetLineChartData="handleSetLineChartData" />

    <!-- <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row> -->
    <scatter></scatter>
    <el-card>
      <p style="font-weight: bold">学习参与度</p>
      <participate-bar :courseid="courseid" :bookList="bookList"></participate-bar>
<!--      <stacked-horizontal-bar />-->
<!--      <p style="text-align: center">平均阅读文章数量：</p><box-card :bookList="bookList" :allUser="allUsers"></box-card>-->
    </el-card>
    <el-card>
      <question-detail :courseid="courseid" :bookList="bookList"></question-detail>
    </el-card>
    <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
      <el-tab-pane
        v-for="(book, index) in bookList"
        :key="index"
        :label="book.name"
        :name="book.id"
        eager
      >
        <el-card style="width:870px;float: left">
          <p style="font-weight: bold">KWL能力</p>
          <pie-chart :bookid="bookid" style="width:870px"></pie-chart>
        </el-card>

        <el-card style="width:870px;margin:10px auto;">
          <p style="font-weight: bold">提问能力</p>
          提问数量：{{questionnum}}
          <raddar-chart @func="getQuestionnum" :bookid="bookid" style="width:870px"></raddar-chart>
        </el-card>
        <el-card>
          <span style="font-weight: bold">阅读理解能力</span>
          <stacked-horizontal-bar :bookid="bookid" :courseid="courseid"></stacked-horizontal-bar>
        </el-card>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import TransactionTable from './components/TransactionTable'
import TodoList from './components/TodoList'
import BoxCard from './components/BoxCard'
import StackedHorizontalBar from './components/StackedHorizontalBar'
import { mapActions } from 'vuex'
import service from '@/utils/request'
import ParticipateBar from '@/views/dashboard/admin/components/participateBar'
import QuestionDetail from '@/views/dashboard/admin/components/questionDetail'
import Activitychar from '@/views/dashboard/admin/components/activitychar'
import Scatter from '@/views/dashboard/admin/components/Scatter'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'DashboardAdmin',
  components: {
    Scatter,
    Activitychar,
    QuestionDetail,
    LineChart,
    ParticipateBar,
    StackedHorizontalBar,
    PanelGroup,
    BoxCard,
    RaddarChart,
    PieChart
  },
  data() {
    return {
      activeName: 2283115,
      radio: 3,
      lineChartData: lineChartData.newVisitis,
      courseList:null,
      bookList: [],
      allUsers: null,
      courseid: 31,
      bookid: null,
      questionnum: 0
    }
  },
  created() {
    this.getAllBookList()
    this.getCourse()
  },
  watch: {
    radio: function(newVal,oldVal){
      console.log("newVal: "+newVal+", oldVal: "+oldVal)
      if(newVal==0){
        this.getAllBookList()
        this.getAllUsers()
        this.courseid = 0
      }else{
        this.getBookListByCourse(newVal)
        this.getUsersByCourse(newVal)
      }
    }
  },
  methods: {
    getQuestionnum(data){
      this.questionnum = data
      console.log('questionnum:'+this.questionnum)
    },
    getCourse() {
      service.get('/course/getCourseList').then(res=>{
        this.courseList = res.data
      })
    },
    getAllBookList() {
      service.get('/book/allbooks').then(res=>{
        this.bookList = res.data
      })
    },
    getAllUsers(){
      service.get('/user/getAllUsers').then(res=>{
        this.allUsers = res.data
      })
    },
    getUsersByCourse(courseId){
      service.get('/user/getUsersByCourseId',{ params: { courseId: courseId }}).then(res=>{
        this.allUsers = res.data
        this.courseid = courseId
      })
    },
    getBookListByCourse(courseId) {
      service.get('/book/getBookByCourseId', { params: { courseId: courseId }}).then(res=>{
        if(res.data!==null){
          this.bookList = res.data
          this.courseid = courseId
        }
      })
    },
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    handleClick(tab, event) {
      this.bookid = tab.name
      console.log('boookid:'+this.bookid)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
