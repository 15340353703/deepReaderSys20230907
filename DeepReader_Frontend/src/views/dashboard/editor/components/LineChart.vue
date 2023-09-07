<template>
  <div :class="chartData" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { mapState, mapActions } from 'vuex'
import service from '@/utils/request'

const lineChartData = {
  newVisitis:{

  }
}
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '90%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      books: [],
      kwls: null,
      readtimedata: [],
      booknamedata: []
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  created(){
    this.getUserKWL()
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
    ...mapActions('user',['getUserKWLs']),
    getUserKWL(){
      this.getUserKWLs().then(res => {
        this.kwls = res
        this.initChart()
      })
    },
    ...mapActions('books',['getUserBookList']),
    getBooks(){
      this.getUserBookList().then(res=>{
        this.books = res
        console.log('books:'+books[0])
      })
    },
    initChart() {
    this.chart = echarts.init(this.$el, 'macarons')
     for(let i=0;i<this.kwls.length;i++){
        service.get('/book/info',{params:{bookId:this.kwls[i].bookId}}).then(res=>{
          this.readtimedata.push(Math.floor(res.data.wordcount/this.kwls[i].read_time))
          this.booknamedata.push(res.data.name)
          if(this.booknamedata.length === this.kwls.length){
            this.setOptions(this.chartData)
            this.chart.setOption({
              xAxis: {
                data: this.booknamedata,
                boundaryGap: false,
                axisTick: {
                  show: false
                },
                axisLabel: {
                  show: true,
                  rotate:0,
                  interval:0,
                  // formatter:function(val){
                  //   return val.split('').join('\n')
                  // }
                  //设置字数限制
                  formatter: function(value) {
                    if (value.length > 3) {
                      return value.substring(0, 3) + "...";
                    } else {
                      return value;
                    }
                  }
                }
              },
              grid: {
                left: 5,
                right: 5,
                bottom: 20,
                top: 30,
                containLabel: true
              },
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'cross'
                },
                padding: [5, 10]
              },
              yAxis: {
                axisTick: {
                  show: false
                }
              },
              legend: {
                data: ['阅读速度（字/秒）']
              },
              series: [{
                name: '阅读速度（字/秒）', itemStyle: {
                  normal: {
                    color: '#FF005A',
                    lineStyle: {
                      color: '#FF005A',
                      width: 2
                    }
                  }
                },
                smooth: true,
                type: 'line',
                data: this.readtimedata,
                animationDuration: 2800,
                animationEasing: 'cubicInOut'
              },
              // {
              //   name: '我的成绩',
              //   smooth: true,
              //   type: 'line',
              //   itemStyle: {
              //     normal: {
              //       color: '#3888fa',
              //       lineStyle: {
              //         color: '#3888fa',
              //         width: 2
              //       },
              //       areaStyle: {
              //         color: '#f3f8ff'
              //       }
              //     }
              //   },
              //   data: actualData,
              //   animationDuration: 2800,
              //   animationEasing: 'quadraticOut'
              // }]
              ]
            })
          }
        })
      }

    },
    setOptions({ expectedData, actualData } = {}) {

    }
  }
}
</script>
