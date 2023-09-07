<template>
  <div :class="className" :chartData="this.questionForm" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'

  const animationDuration = 3000
  export default {
    name: 'QuestionSccoreCharts',
    mixins: [resize],
    props: {
      chartData: {
        required: true
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
        chart: null,
        chartdata: {
          com: 0,
          flu: 0,
          imp: 0
        }
      }
    },
    watch: {
      chartData: {
        handler(newVal, oldVal) {
          if (newVal != undefined) {
            this.chartdata = newVal
            this.initChart()
          }
        },
        deep: true //  深度监听开启
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.initChart()
      })
    },
    beforeDestroy() {
      if (!this.chart) {
        return
      }
      this.chart.dispose()
      this.chart = null
    },
    methods: {
      initChart() {
        this.chart = echarts.init(this.$el, 'macarons')

        this.chart.setOption({
          tooltip: {
            trigger: 'item'
          },
          radar: {
            radius: '66%',
            center: ['40%', '52%'],
            splitNumber: 8,
            splitArea: {
              areaStyle: {
                color: 'rgba(127,95,132,.3)',
                opacity: 1,
                shadowBlur: 45,
                shadowColor: 'rgba(0,0,0,.5)',
                shadowOffsetX: 0,
                shadowOffsetY: 15
              }
            },
            indicator: [
              { name: '难易度', max: 3 },
              { name: '重要性', max: 2 },
              { name: '流畅性', max: 3 }
            ]
          },
          legend: {
            left: 'center',
            bottom: '10',
            data: ['问题平均']
          },
          series: [{
            type: 'radar',
            symbolSize: 0,
            areaStyle: {
              normal: {
                shadowBlur: 13,
                shadowColor: 'rgba(0,0,0,.2)',
                shadowOffsetX: 0,
                shadowOffsetY: 10,
                opacity: 1
              }
            },
            formatter: (a)=>{
              return `{a|${a}}`
            },
            data: [
              {
                // value: [this.chartdata.com,this.chartdata.imp,this.chartdata.flu],
                value: [2.2, 1.3, 2.75],
                name: '问题平均质量',
                label: {
                  show: true,
                  formatter: function (params) {
                    return params.value;
                  }
                }
              }
            ],
            animationDuration: animationDuration
          }]
        })
      }
    }
  }
</script>

<style scoped>

</style>
