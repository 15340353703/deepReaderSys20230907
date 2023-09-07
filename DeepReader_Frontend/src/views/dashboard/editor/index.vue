<template>
  <div class="dashboard-editor-container">
    <div style="text-align:center">
    <el-card>
        <el-radio :key="0"  v-model="radio" :label="0">全部</el-radio>
        <el-radio v-for="(item, id) in this.courseList" :key="id"  v-model="radio" :label="item.id">{{item.courseName}}</el-radio>
    </el-card>
    </div>
<!--    <panel-group @handleSetLineChartData="handleSetLineChartData" />-->
    <DashboardGroup v-model="radio" ></DashboardGroup>
    <el-row style="background:#fff;padding:16px 16px 0;margin:25px;">
      <dailyStepCount v-if="heatList[0].date!==''" :heat-list="heatList" />
    </el-row>
  <div style="background:#fff;padding:16px 16px 16px;margin:25px;text-align:center">截至本周阅读文章进度：<el-progress :text-inside="true" :stroke-width="26" :percentage="70"></el-progress></div>
<!--  <bar-chart style="background:#fff;padding:16px 16px 16px;margin:25px;"></bar-chart>-->
<!--  <line-chart style="background:#fff;padding:16px 16px 16px;margin:25px;"></line-chart>-->
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import RaddarChart from './components/RaddarChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import TransactionTable from './components/TransactionTable'
import TodoList from './components/TodoList'
import BoxCard from './components/BoxCard'
import DailyStepCount from './components/DailyStepCount'
import TagsRaddarChart from '@/views/dashboard/editor/components/TagsRaddarChart'
import DashboardGroup from './components/DashboardGroup'
// import LineChart from '../admin/components/LineChart.vue'

const lineChartData = {
  newVisitis:{

  }
}


export default {
  name: 'DashboardEditor',
  components: {
    GithubCorner,
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    TransactionTable,
    TodoList,
    BoxCard,
    DailyStepCount,
    TagsRaddarChart,
    DashboardGroup
  },

  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      heatList: [{
        date: '',
        heat: 0
      }],
      radio: 0,
      courseList: [{
        id: 0,
        courseNum: '',
        courseName: ''
      }],
      courseId:31
    }
  },
  created() {
    this.getHeat()
    this.getCourse()
  },
   watch: {
    radio: function(newVal,oldVal){
      console.log("newVal: "+newVal+", oldVal: "+oldVal)
      if(newVal==0){
        // this.courseId = 31

      }else{
        // this.courseId = newVal

      }
    }
  },
  methods: {
    ...mapActions('user', ['getUserHeat']),

    getHeat() {
      this.getUserHeat().then(response => {
        this.heatList = response
        if (this.heatList.length == 0) {
          console.log('heatList length ==0')
          // this.heatList[0].date='2019-01-01'
          // this.heatList[0].heat=0
          this.heatList.push('', 0)
          console.log(this.heatList)
        }
      })
    },
    ...mapActions('user', ['getUserCourse']),
    getCourse() {
      this.getUserCourse({}).then((response) => {
        this.courseList = response
        console.log(this.courseList)
      })
    },
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
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
