<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { mapActions } from 'vuex'
import service from '@/utils/request'
const animationDuration = 3000

export default {
  mixins: [resize],
  props: {
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
      default: '400px'
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
      classcomplexityscore:0
    }
  },
  created() {
    const id = this.$route.query.bookid
    this.getQuestions(this.currentPage,this.pagesize, id)
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
    ...mapActions('user',['getUserQuestions']),
    getQuestions(currentPage, pageSize, bookId) {
      this.getUserQuestions({currentPage,pageSize, bookId}).then(res => {
        this.tableData = res.list
        this.total = res.total
        this.currentPage = res.pageNum
        let fluencynum = 0

        for(let i=0;i<this.tableData.length;i++){
          if(this.tableData[i].fluency!==0){
            fluencynum += 1
            this.fluencyscore+=this.tableData[i].fluency
            this.impotancescore += this.tableData[i].importance
            this.complexityscore += this.tableData[i].complexity
          }
        }
        this.fluencyscore = this.fluencyscore/fluencynum
        this.impotancescore = this.impotancescore/fluencynum
        this.complexityscore = this.complexityscore/fluencynum
        service.get('/dq/getQuestionsBybook', { params:{bookId: bookId }}).then(res=>{
          this.classquestions = res.data
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
        })

      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        // title: {
        //   text: 'Basic Radar Chart'
        // },
        legend: {
          left: 'center',
          data: ['个人', '班级平均']
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
              {
                value: [this.fluencyscore.toFixed(2), this.impotancescore.toFixed(2), this.complexityscore.toFixed(2)],
                name: '个人',
                label: {
                  show: true,
                  formatter: function (params) {
                    return params.value;
                  }
                }
              },
              {
                value: [this.classfluencyscore.toFixed(2), this.classimpotancescore.toFixed(2), this.classcomplexityscore.toFixed(2)],
                name: '班级平均',
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
