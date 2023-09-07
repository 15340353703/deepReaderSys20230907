<template>
  <div :bookid="bookid" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import service from '@/utils/request'
import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'

export default {
  mixins: [resize],
  props: {
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
      default: '100%'
    },
    height: {
      type: String,
      default: '320px'
    }
  },
  data() {
    return {
      chart: null,
      preKnowCount: 0,
      QAcount: 0,
      readtime: 0,
      summarywords: 0
    }
  },
  mounted() {

  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  watch: {
    bookid(newVal,oldVal){
      console.log('bookid:'+newVal)
      this.bookid = newVal
      this.preKnowCount = 0
      this.QAcount = 0
      this.readtime = 0
      this.summarywords = 0
      service.get('/kwl/getKWLBybookId',{params:{bookId:this.bookid}}).then(res=> {
        this.readCount += res.data.length
        for (let i = 0; i < res.data.length; i++) {
          this.preKnowCount += JSON.parse(res.data[i].preKnown).length
          this.QAcount += JSON.parse(res.data[i].qa).length
          this.readtime += res.data[i].read_time
          this.summarywords += res.data[i].summary.length
        }
        this.$nextTick(() => {
          this.initChart()
        })
      })
      // this.bookList.forEach((book,index) =>{
      //   service.get('/kwl/getKWLBybookId',{params:{bookId:book.id}}).then(res=> {
      //     this.readCount += res.data.length
      //     for (let i = 0; i < res.data.length; i++) {
      //       this.preKnowCount += JSON.parse(res.data[i].preKnown).length
      //       this.QAcount += JSON.parse(res.data[i].qa).length
      //       this.readtime += res.data[i].read_time
      //       this.summarywords += res.data[i].summary.length
      //     }
      //     if(index === this.bookList.length-1){
      //       this.$nextTick(() => {
      //         this.initChart()
      //       })
      //     }
      //   })
      // })
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['已知知识数量', '提问和回答问题数量', '阅读速度', '文章总结字数']
        },
        series: [
          {
            name: 'WEEKLY WRITE ARTICLES',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: this.preKnowCount, name: '已知知识数量' },
              { value: this.QAcount, name: '提问和回答问题数量' },
              // { value: this.readtime, name: '阅读时长' },
              // { value: this.summarywords, name: '文章总结字数' },
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    }
  }
}
</script>
