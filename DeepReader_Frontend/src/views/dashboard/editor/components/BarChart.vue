<template>
  <div style="width: 100%;height: 400px" id="main">
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import { mapState, mapActions } from 'vuex' 
  import service from '@/utils/request'
  export default {
    name: "echarts",
    data() {
      return {
        user: '',
        answerforms:'',
        books:[],
        bookname:[]
      }
    },
    created(){
      this.getUserInfo()
    },
    mounted() {
      
    },
    methods:{
      ...mapActions('user',['getInfo']),
      getUserInfo(){
        const _this = this
        this.getInfo().then(res => {
          _this.user = res
          _this.getUserAnswers(this.user.id)
          
        })
      },
      ...mapActions('user',['getAnswerForm']),
      getUserAnswers(userId){
        const _this = this
        this.getAnswerForm(userId).then(res => {
          _this.answerforms = res
          _this.echartsInit()
        })
      },
      ...mapActions('books',['getBookInfo']),
      echartsInit() {
        for(let i=0; i<this.answerforms.length; i++){
          this.books.push(this.answerforms[i].bookId)
        }
        this.books = [...new Set(this.books)]
        let newbooks = []
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
          newbooks.push({bookid:this.books[i],right:right,wrong:wrong})
          console.log(newbooks[i])
        }
        let bookdata = []
        let rightdata = []
        let wrongdata = []
        let _this = this
        for(let i=0;i<newbooks.length;i++){
          service.get('/book/info',{params:{bookId:newbooks[i].bookid}}).then(res=>{
            _this.bookname.push(res.data.name)
            rightdata.push(newbooks[i].right)
            wrongdata.push(newbooks[i].wrong)
            if(_this.bookname.length === newbooks.length){
                bookdata = _this.bookname
                const labelRight = {
                  position: 'right'
                };
        　　　　　//使用时只需要把setOption里的对象换成echarts中的options或者自己的参数即可
                echarts.init(document.getElementById('main')).setOption({
                  tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                      // Use axis to trigger tooltip
                      type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                    }
                  },
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
                    data: bookdata
                  },
                  series: [
                    {
                      name: '错误个数',
                      type: 'bar',
                      stack: 'total',
                      label: {
                        show: true
                      },
                      emphasis: {
                        focus: 'series'
                      },
                      data: wrongdata
                    },
                    {
                      name: '正确个数',
                      type: 'bar',
                      stack: 'total',
                      label: {
                        show: true
                      },
                      emphasis: {
                        focus: 'series'
                      },
                      data: rightdata
                    },
                  
                    
                  ]
                })
            }
          
          })
          
          
        }
        
      }
    }
  }
</script>