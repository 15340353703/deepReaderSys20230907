<template>
  <div :bookList="bookList" :courseid="courseid" :class="className" :style="{height:height,width:width}" />
</template>

<script>
// Vue.prototype.$echarts = echarts
import echarts from 'echarts'


require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import service from '@/utils/request'
import { mapActions } from 'vuex'
import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'
// prettier-ignore

export default {
  mixins: [resize],
  props: {
    courseid: {
      default: 31
    },
    bookList: {
      type: IntegerObject,
      default: 2283115
    },
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      // default: '100%'
    },
    height: {
      type: String,
      default: '850px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    heatList: {
      type: Array,
    }
  },
  data() {
    return {
      chart: null,
      booknameList:[],
      read:[],
      answer:[],
      ask:[],
      highlight:[],
      right: [],
      wrong: [],
      users: [],
      allusers: [],
      hours: [],
      startread: [],
      startPractice: [],
      startDeepQuestion: [],
      lookPractice: [],
      lookArticle: [],
      lookPracticeArticle: [],
      createQuestion: []
    }
  },
  watch: {
    bookList(newVal,oldVal){
      this.bookList = newVal
      this.booknameList = []
      this.read = []
      this.ask = []
      this.answer = []
      this.highlight = []
      this.allusers = []
      this.users = []
      this.hours = []
      for(let i=0;i<32;i++){
        this.hours.push(i)
      }
      service.get('/stu/getUsersByCourseId',{params:{courseid: this.courseid}}).then(res => {
        this.allusers = res.data
        for (let i = 0; i < res.data.length; i++) {
          this.users.push(res.data[i].yourname)
          service.get('/dq/getAllreadlog',{params:{userid:res.data[i].id}}).then(res=>{
            let max = 0
            for(let j=0;j<res.data.length;j++){
              let nowdate = new Date()
              let datetime = new Date(res.data[j].stamptime)
              if(nowdate.getFullYear()===datetime.getFullYear() && nowdate.getMonth()===datetime.getMonth()+1){
                console.log('this.behavior:'+res.data[j].behavior)
                if(res.data[j].behavior === '开始阅读'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.startread.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '查看原文'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.lookPracticeArticle.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '开始练习'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.startPractice.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '开始深度提问'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.startDeepQuestion.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '查看文章'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.lookArticle.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '查看习题'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.lookPractice.splice(0,0,userreadlog)
                }
                if(res.data[j].behavior === '创建问题'){
                  let userreadlog = []
                  userreadlog.push(i)
                  userreadlog.push(datetime.getDate())
                  this.createQuestion.splice(0,0,userreadlog)
                }
              }
            }
            if(i===this.allusers.length-1){
              console.log('this.lookp:'+this.lookPractice)
              this.$nextTick(() => {
                this.initCharts()
              })
            }
          })
        }
      })
    }
  },
  mounted() {

  },
  created() {

  },
  methods: {
    initCharts() {
      this.chart = echarts.init(this.$el)
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        title: {
          text: 'Punch Card of Github'
        },
        legend: {
          data: ['开始阅读','开始练习','开始深度提问','查看原文','查看文章','查看习题','创建问题'],
          left: 'right'
        },
        tooltip: {
          position: 'top',
          formatter: function (params) {
            return (
              this.hours[params.value[0]] +
              ' of ' +
              this.users[params.value[1]]
            );
          }
        },
        grid: {
          left: 2,
          bottom: 10,
          right: 10,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.hours,
          boundaryGap: false,
          splitLine: {
            show: true
          },
          axisLine: {
            show: false
          }
        },
        yAxis: {
          type: 'category',
          data: this.users,
          axisLine: {
            show: false
          }
        },
        series: [
          {
            name: '开始阅读',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.startread
          },
          {
            name: '开始练习',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.startPractice
          },
          {
            name: '开始深度提问',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.startDeepQuestion
          },
          {
            name: '查看文章',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.lookArticle
          },
          {
            name: '查看原文',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.lookPracticeArticle
          },
          {
            name: '查看习题',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.lookPractice
          },
          {
            name: '创建问题',
            type: 'scatter',
            symbolSize: function (val) {
              return 10;
            },
            data: this.createQuestion
          },
        ]
      })
    }
  }
}
</script>
