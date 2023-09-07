<template>
  <div :courseid="courseid" :booklist="bookList" :class="className" :style="{height:height,width:width}" />
</template>

<script>
// Vue.prototype.$echarts = echarts
import echarts from 'echarts'
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
      type: Array
    },
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
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
      users: [],
      booknameList: [],
      loginCount: [],
      readCount: [],
      highlight: []
    }
  },
  watch: {
    bookList(newVal,oldVal) {
      this.bookList = newVal
      this.users = []
      this.booknameList = []
      this.read = []
      this.ask = []
      this.answer = []
      this.highlight = []
      service.get('/stu/getUsersByCourseId',{params:{courseid: this.courseid}}).then(res => {
        if(res.data !== null) {
          for (let i = 0; i < res.data.length; i++) {
            this.users.push(res.data[i].yourname)
          }
        }
        service.get('/dq/getLoginCountByUser',{params:{courseid: this.courseid}}).then(res => {
          this.loginCount = res.data
          service.get('/stu/getUserArticles',{params:{courseid: this.courseid}}).then(res => {
            this.readCount = res.data
            service.get('/dq/getHighlightsByUser',{params:{courseid: this.courseid}}).then(res => {
              this.highlight= res.data
              this.$nextTick(() => {
                this.initCharts()
              })
          })
        })
      })

      // for (let i=0;i<this.bookList.length;i++) {
      //   this.booknameList.unshift(this.bookList[i].name)
      //   let bookId = this.bookList[i].id
      //   this.getQuestionsBybook(bookId).then(res => {
      //     this.ask.push(res.length)
      //     service.get('/kwl/getKWLBybookId',{ params:{bookId:bookId}}).then(res=>{
      //       this.read.push(res.data.length)
      //       service.get('/dq/getAnswersBybook',{params:{bookId: bookId}}).then(res=>{
      //         this.answer.push(res.data.length)
      //         if(i===this.bookList.length-1) {
      //           this.$nextTick(() => {
      //             this.initCharts()
      //           })
      //         }

              // service.get('/dq/getHighlightsBybook',{params:{bookId:bookId}}).then(res=>{
              //   let allhighlights = 0
              //   for(let j=0;j<res.data.length;j++){
              //     if(JSON.parse(res.data[j].readfeedback)[0].feedback[0]){
              //       allhighlights += JSON.parse(res.data[j].readfeedback)[0].feedback.length
              //     }
              //     if(j===res.data.length-1){
              //       this.highlight.push(allhighlights)
              //       console.log('bookname:'+this.bookList[i].name)
              //       console.log('hllength:'+res.data.length)
              //       console.log('highlightlength:'+allhighlights)
              //     }
              //     if(i===this.bookList.length-1){
              //       console.log('high:'+this.highlight)
              //       this.$nextTick(() => {
              //         this.initCharts()
              //       })
              //     }
              //   }
              // })
        })
      }
  },
  mounted() {

  },
  created() {
  },
  methods: {
    getAllInfo(){

    },
    initCharts() {
      this.chart = echarts.init(this.$el)
      this.setOptions()
    },
    ...mapActions('books',['getQuestionsBybook']),
    setOptions() {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
          }
        },
        legend: {},
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: this.users
        },
        series: [
          {
            name: '登录次数',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: this.loginCount
          },
          {
            name: '阅读文章数量',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: this.readCount
          },
          {
            name: '批注数量',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: this.highlight
          },
          // {
          //   name: '文章批注数量',
          //   type: 'bar',
          //   stack: 'total',
          //   label: {
          //     show: true
          //   },
          //   emphasis: {
          //     focus: 'series'
          //   },
          //   data: this.highlight
          // }
        ]
      })
    }
  }
}
</script>
