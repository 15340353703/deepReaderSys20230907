<template>
  <div :userTestScore = "userTestScore" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'
  export default {
    name: 'TestScoreBarChart',
    mixins: [resize],
    props: {
      width: {
        type: String,
        default: '90%'
      },
      height: {
        type: String,
        default: '300px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },
      userTestScore: {
        type: Array,
        default: true
      }

    },
    data() {
      return {
        chart: null,
        testname:[],
        score:[],
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.initCharts()
      })
    },
    methods: {
      initCharts() {
        for(let i=0; i<this.userTestScore.length;i++){
          this.testname.push(this.userTestScore[i].testname)
          this.score.push(this.userTestScore[i].score)
        }


        this.chart = echarts.init(this.$el)
        this.setOptions()
      },
      setOptions() {
        this.chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          xAxis: {
            type: 'category',
            data: this.testname
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.score,
              type: 'bar',
              showBackground: true,
              // backgroundStyle: {
              //   color: 'rgba(180, 180, 180, 0.2)'
              // },
              itemStyle: {
                color: 'rgba(84, 112, 198,1)'  // 这里是你想要的颜色代码
              }
            }
          ]
        })
      }
    }


  }
</script>

<style scoped>

</style>
