<template>
  <div id="container" :bookList="bookList" :courseid="courseid" :class="className" :style="{height:height,width:width}" />
</template>

<script>
// Vue.prototype.$echarts = echarts
import echarts from 'echarts'
import Highcharts from 'highcharts'

require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import service from '@/utils/request'
import { mapActions } from 'vuex'
import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'

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
      default: '1050px'
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
      this.startread = []
      this.startPractice = []
      this.startDeepQuestion = []
      this.lookPractice = []
      this.lookArticle = []
      this.lookPracticeArticle = []
      service.get('/stu/getUsersByCourseId',{params:{courseid: this.courseid}}).then(res => {
        if(res.data !== null) {
          this.allusers = res.data
          for (let i = 0; i < res.data.length; i++) {
            this.users.push(res.data[i].yourname)
            service.get('/dq/getAllReadlog',{params:{userid:res.data[i].id}}).then(res=>{
              for(let j=0;j<res.data.length;j++){
                let nowdate = new Date()
                let datetime = new Date(res.data[j].stamptime)
                var bookname = res.data[j].bookname
                let y = datetime.getFullYear()
                let MM = datetime.getMonth()+1
                MM = MM < 10 ? ('0' + MM) : MM
                let d = datetime.getDate()
                d = d < 10 ? ('0' + d) : d
                let h = datetime.getHours();
                h = h < 10 ? ('0' + h) : h;
                let m = datetime.getMinutes();
                m = m < 10 ? ('0' + m) : m;
                let s = datetime.getSeconds();
                s = s < 10 ? ('0' + s) : s;
                datetime = y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s

                var temp = datetime.split(' ')
                var yy = parseInt(temp[1].substr(0, 2)) * 3600+ parseInt(temp[1].substr(3, 2)) * 60 + parseInt(temp[1].substr(6, 2));
                yy = yy * 1000;
                var arr=temp[0].split('-');
                var dd=Date.UTC(arr[0],arr[1]-1,arr[2]);//把日期转化成毫秒数

                if(nowdate.getFullYear()===2022){
                  if(res.data[j].behavior === '开始阅读'){
                    this.startread.push({ 'x': dd+yy, 'y': i, 'name': this.users[i],'book': bookname })
                  }
                  if(res.data[j].behavior === '查看原文'){
                    this.lookPracticeArticle.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                  if(res.data[j].behavior === '开始练习'){
                    this.startPractice.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                  if(res.data[j].behavior === '开始深度提问'){
                    this.startDeepQuestion.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                  if(res.data[j].behavior === '查看文章'){
                    this.lookArticle.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                  if(res.data[j].behavior === '查看习题'){
                    this.lookPractice.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                  if(res.data[j].behavior === '创建问题'){
                    this.createQuestion.push({ 'x': dd+yy, 'y': i, 'name': this.users[i], 'book': bookname })
                  }
                }
              }
              if(i===this.allusers.length-1){
                this.$nextTick(() => {
                  this.initCharts()
                })
              }
            })
          }
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
      Highcharts.chart('container',{
        chart: {
          type: 'scatter',
          zoomType: 'xy',
          // inverted: true
        },
        title: {
          text: '学生阅读活动数据'
        },
        // subtitle: {
        //   text: '数据来源: Heinz  2003'
        // },
        yAxis: {
          categories: this.users,
          title: {
            enabled: true,
            text: '姓名'
          },
          // startOnTick: true,
          // endOnTick: true,
          // showLastLabel: true,
        },
        xAxis: {
          type: 'datetime',
          title: {
            text: '日期'
          },
          labels: {
            overflow: 'justify'
          }
          // dateTimeLabelFormats: {
          //   millisecond: '%Y:%m:%d'
          // },
          // tickPixelInterval: 1
        },
        legend: {
          // layout: 'vertical',
          // align: 'left',
          // verticalAlign: 'top',
          // x: 100,
          // y: 70,
          // floating: true,
          // backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',
          // borderWidth: 1
        },
        plotOptions: {
          scatter: {
            marker: {
              radius: 5,
              states: {
                hover: {
                  enabled: true,
                  lineColor: 'rgb(100,100,100)'
                }
              }
            },
            states: {
              hover: {
                marker: {
                  enabled: false
                }
              }
            },
            tooltip: {
              headerFormat: '<b>{series.name}</b><br>',
              pointFormat: '{point.x:%e. %b %H:%M}, {point.name}, {point.book}',
              // formatter: function (item) {
              //   return item.y;
              // }

            },
            pointInterval: 36000000, // one hour
            pointStart: Date.UTC(2022, 5, 18, 0, 0, 0),

          }
        },
        series: [
          {
          name: '开始阅读',
          // color: 'rgba(223, 83, 83, .5)',
          data: this.startread
          },
          {
          name: '开始深度提问',
          // color: 'rgba(119, 152, 191, .5)',
          data: this.startDeepQuestion
          },
          {
            name: '开始练习',
            // color: 'rgba(80, 20, 20, .5)',
            data: this.startPractice
          },
          {
            name: '创建问题',
            // color: 'rgba(120, 40, 40, .5)',
            data: this.createQuestion
          },
          {
            name: '查看文章',
            // color: 'rgba(120, 40, 40, .5)',
            data: this.lookArticle
          },
          {
            name: '查看原文',
            // color: 'rgba(120, 40, 40, .5)',
            data: this.lookPracticeArticle
          },
          {
            name: '查看习题',
            // color: 'rgba(120, 40, 40, .5)',
            data: this.lookPractice
          }
        ],
        // keys: ['x','y','book'],
        navigation: {
          menuItemStyle: {
            fontSize: '10px'
          }
        }
      })
    },
  }
}
</script>
