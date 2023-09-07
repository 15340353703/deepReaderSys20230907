<template>
  <div :taskCount="taskCount" :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from 'echarts'
  import resize from './mixins/resize'

  require('echarts/theme/macarons') // echarts theme

  export default {
  name: 'DailyTaskCount',
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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    taskCount: {
      type: Array,
      default: true
    }
  },
  data() {
    return {
      // chart: null,
      // taskCount: [],
    }
  },
  created() {
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  methods: {
    initCharts() {
      this.chart = echarts.init(this.$el)
      this.setOptions()
    },
    setOptions() {
      this.chart.setOption({
        tooltip: {
          show: true, // 显示提示框组件
          trigger: 'item', // 触发类型
          triggerOn: 'mousemove', // 出发条件
          formatter: function(params) {
            // 这里可以debugger出params内容
            // var xValue = params[0];
            // var yValue = params[1];
            var result = '您在' + params.data[0] + '时，查看任务' + params.data[1] +'次'
            return result
          }
        },
        visualMap: {
          min: 0,
          max: 10,
          calculable: true,
          seriesIndex: [2, 4, 8],
          orient: 'horizontal',
          // left: '55%',
          top: 230
        },

        calendar:{
          orient: 'vertical',
          yearLabel: {
            nameMap: 'cn',
            margin: 40,
          },
          monthLabel: {
            nameMap: 'cn',
            margin: 20,
          },
          dayLabel: {
            firstDay: 1,
            nameMap: 'cn'
          },
          cellSize: 30,
          left: 60,
          range: '2023-08',
          nameMap: 'cn',

        },
        series: [
          {
          type: 'effectScatter',
          coordinateSystem: 'calendar',
            symbolSize: function (val) {
              return val[1] / 0.3;
            },
          data: this.getVirtulData('2023'),
        }]
      })
    },

    getVirtulData(year) {
      year = year || '2023'
      // 将数据按照日期分组统计
      let groupedData = {};
      this.taskCount.forEach(item => {
        // let date = item.stamptime;
        let date =echarts.format.formatTime('yyyy-MM-dd', item.stamptime)
        // let behavior = item.behavior;
        let behavior = 1;
        if (!groupedData[date]) {
          groupedData[date] = [];
        }
        groupedData[date].push(behavior);
      });
      // console.log('groupedData.length:'+groupedData)

      // 构造热力图数据
      // console.log('heatmapData.length:'+heatmapData[0])


      // for (let time = date; time < end; time += dayTime) {
      //   let formatedTime = echarts.format.formatTime('yyyy-MM-dd', time)
      //   let heat = 0
      //   for (let i = 0; i < this.taskCount.length; i++) {
      //     if (this.taskCount[i].stamptime === formatedTime) {
      //       console.log('hh'+this.taskCount[i].stamptime + ':' + formatedTime)
      //       heat = 1
      //     }
      //   }
      //   data.push([formatedTime, heat])
      // }
      // console.log(data)
      // 构造热力图数据
      return Object.keys(groupedData).map(date => {
        let behaviorArray = groupedData[date]
        let behaviorSum = behaviorArray.reduce((sum, val) => sum + val, 0)
        return [date, behaviorSum]
      })
      // return data
    }
},
}
</script>

<style scoped>

</style>
