<template>
  <div :bookid="bookid" :class="className" :style="{height:height,width:width}"></div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { mapActions } from 'vuex'
import service from '@/utils/request'
import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'
const animationDuration = 3000

export default {
  mixins: [resize],
  props: {
    bookid: {
      type: IntegerObject
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
      default: '300px'
    }
  },
  data() {
    return {
      tableData:null,
      classquestions:null,
      chart: null,
      fluencyscore:0,
      impotancescore:0,
      complexityscore:0,

      classfluencyscore:0,
      classimpotancescore:0,
      classcomplexityscore:0,
      questionnum: 0
    }
  },
  watch: {
    bookid(newVal,oldVal){
      this.getQuestions()
    }
  },
  created() {

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
  methods: {
    getQuestions() {
      service.get('/dq/getQuestionsBybook', {params: {bookId: this.bookid}}).then( res => {
        this.classquestions = res.data
        this.questionnum = this.classquestions.length
        this.$emit('func',this.questionnum)
        if (this.classquestions.length !== 0){
          let num = 0
          for(let i=0;i<this.classquestions.length;i++){
            if(this.classquestions[i].fluency!==0){
              num += 1
              this.classfluencyscore+=this.classquestions[i].fluency
              this.classimpotancescore += this.classquestions[i].importance
              this.classcomplexityscore += this.classquestions[i].complexity
            }
          }
          this.classfluencyscore = this.classfluencyscore/num
          this.classimpotancescore = this.classimpotancescore/num
          this.classcomplexityscore = this.classcomplexityscore/num

          this.$nextTick(() => {
            this.initChart()
          })
        }
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        title: {
          text: '班级提问情况'
        },
        legend: {
          left: 'center',
          data: ['班级平均']
        },
        radar: {
          // shape: 'circle',
          indicator: [
            { name: '流畅性', max: 3 },
            { name: '重要性', max: 2 },
            { name: '复杂性', max: 3 },
          ]
        },
        series: [
          {
            name: 'Budget vs spending',
            type: 'radar',
            data: [
              // {
              //   value: [this.fluencyscore.toFixed(2), this.impotancescore.toFixed(2), this.complexityscore.toFixed(2)],
              //   name: '个人',
              //   label: {
              //     show: true,
              //     formatter: function (params) {
              //       return params.value;
              //     }
              //   }
              // },
              {
                value: [this.classfluencyscore.toFixed(2), this.classimpotancescore.toFixed(2), this.classcomplexityscore.toFixed(2)],
                // name: '班级平均',
                label: {
                  show: true,
                  formatter: function (params) {
                    return params.value;
                  }
                }
              }
            ]
          }
        ]
      })
    }
  }
}
</script>
