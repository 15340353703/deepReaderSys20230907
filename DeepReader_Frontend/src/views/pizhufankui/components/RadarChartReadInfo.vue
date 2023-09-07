<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
export default {
  name: 'RadarChartReadInfo',
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
      default: '100%'
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
      chart: null
    }
  },
  watch: {
    chartData: { //  监听数据变化
      deep: true, // 深度监听
      handler(val) {
        this.setOptions(val) // 重新绘制图表数据
      }
    }
  },
  mounted() {
    console.log('数据' + this.chartData)
    // dom元素加载完成后执行
    this.$nextTick(() => {
      // 初始化echarts实例
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
      console.log('数据' + this.chartData)
      // 参数一:this.$el指向当前组件template模板中的根标签(在mounted生命周期中才有效)
      // 参数二:echarts主题颜色
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions({ expectedData, actualData } = {}) {
      this.chart.setOption({
        legend: { // 图例组件
          data: [
            // { name: '阅读批注雷达图', icon: 'circle' },
            { name: '阅读批注雷达图', icon: 'circle' }],

          left: 0,
          orient: 'vertical'
        },
        tooltip: { // 提示框组件
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        radar: {
          // shape: 'circle',
          center: ['50%', '50%'],
          splitNumber: 3, // 雷达图圈数设置
          name: {
            textStyle: {
              color: '#665757',
              fontSize: 18,
              fontWeight: '300',
              padding: [-10, -12]
            }
          },
          // 设置雷达图中间射线的颜色
          axisLine: {
            lineStyle: {
              color: '#e9e7ef'
            }
          },
          // 雷达图背景的颜色，在这儿随便设置了一个颜色，完全不透明度为0，就实现了透明背景
          splitArea: {
            show: true,
            areaStyle: {
              color: 'rgba(127,95,132,.3)', // 图表背景的颜色
              opacity: 1,
              shadowBlur: 45,
              shadowColor: 'rgba(0,0,0,.5)',
              shadowOffsetX: 0,
              shadowOffsetY: 15
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              width: 2,
              color: '#e9e7ef' // 设置网格的颜色
            }
          },
          indicator: [
            { name: '检索', max: 10 },
            { name: '提问', max: 6 },
            { name: '分析', max: 6 },
            { name: '评价', max: 8 },
            { name: '反思', max: 6 },
            { name: '创造', max: 7 }
          ]
        },
        series: [
          {
            name: 'Budget vs spending',
            type: 'radar',
            tooltip: {
              trigger: 'item'
            },
            data: [
              {
                value: [this.chartData.js, this.chartData.tw, this.chartData.fx, this.chartData.pj, this.chartData.fs, this.chartData.cz],
                name: '我的阅读批注',
                symbolSize: 2,
                symbolColor: '#3399FF',
                // symbolColor: '#47EDFC',
                // symbol: "none", //去掉折线上的小圆点
                areaStyle: { color: 'rgba(71,237,252,.8)' }, // 雷达覆盖区域背景颜色
                lineStyle: {
                  color: '#3399FF'
                },
                label: {
                  show: true,
                  formatter: function(params) {
                    return params.value
                  }
                }
              }
              // {
              //   value: [4, 3, 4],
              //   name: '我的问题',
              //   symbolSize: 2,
              //   symbolColor: 'rgba(168,71,252,0.93)',
              //   areaStyle: { color: 'rgba(177,71,252,0.95)' }, // 雷达覆盖区域背景颜色
              //   lineStyle: {
              //     color: 'rgba(168,71,252,0.93)'
              //   },
              //   label: {
              //     show: true,
              //     formatter: function(params) {
              //       return params.value
              //     }
              //   }
              //
              // }
            ]
          }
        ]
      })
    }
  }
}
</script>

<style scoped>

</style>
