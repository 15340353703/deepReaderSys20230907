<template>
  <div :courseId="courseId" style="margin-left:30px">
    <div v-for="(item, id) in this.list" :key="id" style="width: 80%;line-height: 28px;font-size: larger;">
      <div>{{item.work}}:已阅读：{{item.haveReadSum + '/' + item.children.length}}篇</div>
      <div><el-progress :percentage=" item.percentage" :text-inside="true" :stroke-width="22" :color="customColorMethod" /></div>
<!--      <div>已阅读：{{item.haveReadSum + '/' + item.children.length}}</div>-->
    </div>
  </div>
</template>

<script>
  import echarts from 'echarts'
  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'
  import service from '@/utils/request'
  import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'
  import { getToken } from '@/utils/auth'
  export default {
    name: 'ReadProgress',
    mixins: [resize],
    props: {
      courseId: {
        value: IntegerObject,
        default: 31
      },
      bookList: {
        type: IntegerObject,
        default: 2283115
      },
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
      }
    },
    data() {
      return {
        list: [
          {
            id: 0,
            name: '',
            // author: ''
          }
        ],


      }
    },
    created() {
      // this.getReadPregress()
    },
    watch: {
      courseId(newValue) {
        // 监听 courseId 变化
        this.courseId = newValue
        this.getReadPregress()
        // console.log('Course Id in grandchild component changed:', newValue);
      },
    },
    computed: {
      customColorMethod() {
        return (percentage) => {
          if (percentage < 30) {
            return '#f56c6c';
          }
          if (percentage < 70) {
            return '#67c23a';
          }
          return '#1989fa';
        };
      },
    },
    methods: {
      getReadPregress() {
        service.get('/course/getUsersReadProgressByCourseId', { params: { token: getToken(),courseId:this.courseId }}).then(res => {
          // console.log('直接调用后端：' + res.data[0].work)
          // console.log('直接调用后端：' + res.data[0].children)
          this.list = res.data

          for(let i = 0; i<this.list.length;i++){
            let haveRead = 0
            // console.log('list.length:'+this.list[i].children)
            for(let j = 0; j<this.list[i].children.length; j++){
              if(this.list[i].children[j].have_read){
                 haveRead += 1
              }
            }
            this.list[i].haveReadSum = haveRead
            this.list[i].bookSum = this.list[i].children.length
            this.list[i].percentage = (haveRead/ this.list[i].children.length).toFixed(3) * 100
          }

        })
      },

    }
  }
</script>

<style scoped>

</style>
