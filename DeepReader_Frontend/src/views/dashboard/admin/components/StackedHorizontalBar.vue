<template>
  <div :bookid="bookid" :courseid="courseid" :class="className" :style="{height:height,width:width}" />
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

    },
    bookid: {
      type: IntegerObject,
      default: 2283115
    },
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '1550px'
    },
    height: {
      type: String,
      default: '550px'
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
      allusers: []
    }
  },
  watch: {
    bookid(newVal,oldVal){
      this.bookid = newVal
      this.booknameList = []
      this.read = []
      this.ask = []
      this.answer = []
      this.highlight = []
      this.allusers = []
      this.users = []
      this.right = []
      this.wrong = []

      service.get('/stu/getUsersByCourseId',{params:{courseid: this.courseid}}).then(res => {
        if(res.data !== null) {
          this.allusers = res.data
          for (let k = 0; k < res.data.length; k++) {
            this.users.push(res.data[k].yourname)
          }
          for(let i=0;i<this.allusers.length;i++){
            service.get('/dq/getAnswerFormByUserId', { params: { bookId: this.bookid, userId: this.allusers[i].id }}).then(res=>{
              let rightCount = 0
              let wrongCount = 0
              if(res.data!==null){
                this.answer.push(res.data.length)
                for(let j=0;j<res.data.length;j++){
                  if(res.data[j].judge==='正确'){
                    rightCount += 1
                  } else {
                    wrongCount += 1
                  }
                }
              } else {
                this.answer.push(0)
              }
              this.right.push(rightCount)
              this.wrong.push(wrongCount)
              if(i===this.allusers.length-1){
                this.$nextTick(() => {
                  this.initCharts()
                })
              }
            })
          }
        }
      })

      // for(let i=0;i<this.bookList.length;i++){
      //   this.booknameList.unshift(this.bookList[i].name)
      //   let bookId = this.bookList[i].id
      //   this.getQuestionsBybook(bookId).then(res=>{
      //     this.ask.push(res.length)
      //     service.get('/kwl/getKWLBybookId',{params:{bookId:bookId}}).then(res=>{
      //       this.read.push(res.data.length)
      //       service.get('/dq/getAnswersBybook',{params:{bookId:bookId}}).then(res=>{
      //         this.answer.push(res.data.length)
      //         if(i===this.bookList.length-1){
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
            // })
          // })
        // })
      // }
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
    setOptions() {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
          }
        },
        legend: {
          data: ['回答问题个数', '回答正确数量', '回答错误数量']
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            magicType: { show: true, type: ['line', 'bar', 'stack'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: this.users
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '回答问题个数',
            type: 'bar',
            barGap: 0,
            label: 'labelOption',
            emphasis: {
              focus: 'series'
            },
            data: this.answer
          },
          {
            name: '回答正确数量',
            type: 'bar',
            label: 'labelOption',
            emphasis: {
              focus: 'series'
            },
            data: this.right
          },
          {
            name: '回答错误数量',
            type: 'bar',
            label: 'labelOption',
            emphasis: {
              focus: 'series'
            },
            data: this.wrong
          },
        ]
      })
    }
  }
}
</script>
