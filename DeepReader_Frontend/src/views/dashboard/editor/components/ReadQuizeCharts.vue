<template>
  <div :courseId="courseId" :booklist="bookList" :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import * as echarts from 'echarts';
  import { mapState, mapActions } from 'vuex'
  import service from '@/utils/request'
  import { IntegerObject } from '../../../../../public/pdfjs/build/pdf.worker'
  import resize from './mixins/resize'
  export default {
    name: 'ReadQuizeCharts',
    mixins: [resize],
    props: {
      courseId: {
        value: IntegerObject,
        default: 31
      },
      bookList: {
        type: Array
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
        default: '300px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },

    },
    data() {
      return {
        chart: null,
        users: [],
        booknameList: [],
        loginCount: [],
        readCount: [],
        highlight: [],
        answerforms:[],
        bookdata:[],
        rightdata:[],
        wrongdata:[],
        bookname:[],
        newbooks:[],

      }
  },
    watch: {
      courseId(newValue) {
        // 监听 courseId 变化
        this.courseId = newValue
        this.getUserAnswerForm(this.courseId)
        // console.log('Course Id in grandchild component changed:', newValue);
      },
    },
    created(){
      this.getUserAnswerForm(this.courseId)

      // console.log('courseid:'+this.courseId)
    },
    // async  mounted() {
    //   await this.getUserAnswerForm(this.courseId)
    //   // this.$nextTick(() => {
    //     this.initCharts()
    //   // })
    // },
    methods:{
      initCharts() {
        this.chart = echarts.init(this.$el)
        this.setOptions()
        // this.getUserAnswerForm(this.courseId)
      },
      ...mapActions('user', [ 'getUserCourseAnswerForm']),
      ...mapActions('books', [ 'getbookInfoById']),
      getUserAnswerForm(courseId) {
        this.getUserCourseAnswerForm(courseId).then(res => {
          this.answerforms = res
          console.log('answerList:'+this.answerforms)
          for(let i=0; i<this.answerforms.length; i++){
            this.books.push(this.answerforms[i].bookId)
          }
          this.books = [...new Set(this.books)]
          // let newbooks = []
          for(let i=0;i<this.books.length; i++){
            let right = 0
            let wrong = 0
            for(let j=0; j<this.answerforms.length; j++){
              if(this.answerforms[j].bookId === this.books[i]){
                // console.log('判断正误：'+this.answerforms[j].judge)
                if(this.answerforms[j].judge === '正确'){
                  right += 1
                } else {
                  wrong += 1
                }
              }
            }
            this.newbooks.push({bookid:this.books[i],right:right,wrong:wrong})
            console.log(this.newbooks[i])
            console.log('newbooksii:')
          }
          console.log(this.newbooks)

          // this.bookdata = []
          // this.rightdata = []
          // this.wrongdata = []
          // this.getchartsData()
          // for(let i=0;i<this.newbooks.length;i++){
          //   this.rightdata.push(this.newbooks[i].right)
          //   this.wrongdata.push(this.newbooks[i].wrong)
          //   this.bookdata.push(this.newbooks[i].bookid)
          // }
          // console.log('rightdata:'+this.rightdata)
          // this.initCharts()

          let _this = this
          for(let i=0;i<this.newbooks.length;i++){
            this.getbookInfoById(this.newbooks[i].bookid).then(res=>{
              console.log('bookname:')
              console.log(res)
              // service.get('/book/info',{params:{bookId:newbooks[i].bookid}}).then(res=>{
              _this.bookname.push(res.name)
              this.rightdata.push(this.newbooks[i].right)
              console.log('rightdata1:'+this.rightdata)
              this.wrongdata.push(this.newbooks[i].wrong)
              if(_this.bookname.length === this.newbooks.length){
                this.bookdata = _this.bookname
              }



            })

          }

          setTimeout(() => {

            this.initCharts()

          }, 2000);

        })

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
          // title: {
          //   text: '阅读测试正确率'
          //
          // },
          legend: {},
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'value'
          },
          yAxis: {
            type: 'category',
            // data: ['学习分析','教育技术咨询','智能作文分析']
            data:this.bookdata
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
          },
          series: [
            {
              name: '正确题目',
              type: 'bar',
              stack: 'total',
              label: {
                show: true
              },
              emphasis: {
                focus: 'series'
              },
              // data: [5,3,8]
              data:this.rightdata
            },
            {
              name: '错误题目',
              type: 'bar',
              stack: 'total',
              label: {
                show: true
              },
              emphasis: {
                focus: 'series'
              },
              // data: [4,6,7]
              data:this.wrongdata
            },

          ]
        })
      }
    }
  }
</script>

<style scoped>

</style>
