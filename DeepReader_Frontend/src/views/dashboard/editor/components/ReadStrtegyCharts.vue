<template>
  <div id="container" :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import Highcharts from 'highcharts'

  require('echarts/theme/macarons') // echarts theme
  import resize from './mixins/resize'
  import { mapActions } from 'vuex'
  import Axios from 'axios'
  import { getToken } from '@/utils/auth'
  import service from '@/utils/request'
  export default {
    name: 'ReadStrtegyCharts',
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
        default: '320px'
      },
      autoResize: {
        type: Boolean,
        default: true
      },
    },
    data() {
      return {
        booklist:[],
        groupnameHp:'',
        annotationNumtotal:[],//批注条数
        reReadingNumtotal:[],//重读
        quizNumtotal:[],//提问个数
        practiceNumtotal:[],//回答问题个数
        bookName:[],//书名
        bookData:[],//
        bookname:[],
        newbooks:[],

      }
    },
    created() {
      this.getAllBookList(this.currentPage, this.pageSize)
      this.getuserInfo()

    },
    mounted() {
      this.$nextTick(() => {
        this.initCharts()
      })
    },
    methods: {

      ...mapActions('books', ['getUserBookList','getbookInfoById']),
      ...mapActions('user',['getUserQuestions']),
      ...mapActions('user', ['getInfo','getUserCourse']),
      getuserInfo() {
        this.getInfo().then(res => {
          this.userInfo = res
          console.log('userInfo: ' + this.userInfo)
          this.hypothesisid = this.userInfo.hypothesisname
          this.getUserCourse(this.userInfo.id).then(res=>{
            this.course = res
          })
        })
      },
      getCourse(bookid){
        this.getCourseBybookid(bookid).then(res=>{
          this.groupnameHp = res[0].groupnameHP
          // this.getuserInfo()
          console.log('groupnameHp: ' + this.groupnameHp)
        })
      },
      getAllBookList(page, pageSize) {
        this.listLoading = true
        this.getUserBookList({ page, pageSize }).then((response) => {
          console.log("booklist111:")
          console.log(response)
          this.booklist = response.list
          this.total = response.total
          this.currentPage = response.pageNum
          this.listLoading = false

          this.getUserStregyNum()
        })
      },
      getUserStregyNum(){
        // let newbooks = []
        for(let i=0;i<this.booklist.length;i++){
          let quizNum = 0
          let annotationNum = 0
          let reReadingNum = 0
          let practiceNum = 0
          //获取每一篇文章的ID用于策略数量的查询
          //1.提问数量
          let bookid = this.booklist[i].id
          console.log('quizNum:'+ bookid)
          service.get('/user/getUsersquestionById', { params: { token: getToken(),bookid:bookid }}).then(res => {
            quizNum = res.data.length
            console.log('quizNum11:')
            console.log(quizNum)

            //2.批注条数
            const username='acc:'+this.userInfo.hypothesisname+'@hypothes.is'
            Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
                limit: 200, user: username, uri: this.booklist[i].url
              }}).then(res => {
              annotationNum = res.data.total
              console.log('annotationNum:')
              console.log(res.data.total)

              //3.重读次数
              let readNum = []
              let answerNum = []
              service.get('/dq/getReadlogByBookId', { params: { token: getToken(),bookid:bookid }}).then(res => {
                for(let i = 0; i < res.data.length; i++){
                  if(res.data[i].behavior === '查看原文'){
                    readNum.push(res.data[i])
                  }
                  if(res.data[i].behavior === '开始练习'){
                    answerNum.push(res.data[i])
                  }
                }
                reReadingNum = readNum.length
                practiceNum = answerNum.length

                this.newbooks.push({bookid:bookid,quizNum:quizNum,annotationNum:annotationNum,reReadingNum:reReadingNum,practiceNum:practiceNum})
                console.log('newbooks:')
                console.log(this.newbooks)


              })

            })


          })

        }








        setTimeout(() => {

          this.getChartsData()

        }, 2000);


      },

      getChartsData(){
        let _this = this
        for(let i=0;i<this.newbooks.length;i++){
          this.getbookInfoById(this.newbooks[i].bookid).then(res=>{
            console.log('bookname:')
            console.log(res)
            // service.get('/book/info',{params:{bookId:newbooks[i].bookid}}).then(res=>{
            _this.bookname.push(res.name)
            this.annotationNumtotal.push(this.newbooks[i].annotationNum)
            console.log('rightdata1:'+this.annotationNumtotal)
            this.reReadingNumtotal.push(this.newbooks[i].reReadingNum)
            console.log('rightdata12:'+this.reReadingNumtotal)
            this.quizNumtotal.push(this.newbooks[i].quizNum)
            this.practiceNumtotal.push(this.newbooks[i].practiceNum)
            if(_this.bookname.length === this.newbooks.length){
              this.bookdata = _this.bookname
            }
          })

        }

        setTimeout(() => {

          this.initCharts()

        }, 2000);
      },





      initCharts() {
        Highcharts.chart('container',{
          // dataZoom: [
          //   {
          //     type: 'inside',
          //     start: 15,
          //     end: 100
          //   }
          // ],
          chart: {
            type: 'column'
          },
          title: {
            text: '阅读策略使用情况'
          },
          xAxis: {
            categories: this.bookdata,
            // min: 0,        // X轴的最小值为0
            // max: 4       // X轴的最大值为100
          },
          yAxis: [{
            min: 0,
            title: {
              text: '批注'
            }
          }, {
            title: {
              text: '重读'
            },
            opposite: true
          }],
          legend: {
            shadow: false
          },
          tooltip: {
            shared: true
          },
          plotOptions: {
            column: {
              grouping: false,
              shadow: false,
              borderWidth: 0
            }
          },
          series: [{
            name: '批注',
            color: 'rgba(165,170,217,1)',
            // data: [90, 73],
            data:this.annotationNumtotal,
            pointPadding: 0.3, // 通过 pointPadding 和 pointPlacement 控制柱子位置
            pointPlacement: -0.2,
            tooltip: {  // 为当前数据列指定特定的 tooltip 选项
              valueSuffix: ' 条数'
            },
          }, {
            name: '重读',
            color: 'rgba(126,86,134,.9)',
            // data: [110, 90],
            data:this.reReadingNumtotal,
            pointPadding: 0.4,
            pointPlacement: -0.2,
            tooltip: {  // 为当前数据列指定特定的 tooltip 选项
              valueSuffix: '次数'
            },
          }, {
            name: '提问',
            color: 'rgba(248,161,63,1)',
            // data: [115, 116],
            data:this.quizNumtotal,
            tooltip: {  // 为当前数据列指定特定的 tooltip 选项
              valueSuffix: ' 个数'
            },
            pointPadding: 0.3,
            pointPlacement: 0.2,
            yAxis: 1  // 指定数据列所在的 yAxis
          }, {
            name: '练习',
            color: 'rgba(186,60,61,.9)',
            // data: [50, 40],
            data:this.practiceNumtotal,
            tooltip: {
              valueSuffix: '次数'
            },
            pointPadding: 0.4,
            pointPlacement: 0.2,
            yAxis: 1
          }]
        })
      }
    }
  }
</script>

<style scoped>

</style>
