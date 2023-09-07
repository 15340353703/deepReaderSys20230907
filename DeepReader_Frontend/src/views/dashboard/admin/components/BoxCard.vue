<template>
  <div :bookList="bookList" :allUser="allUser">
  <el-progress :percentage=" (parseFloat(this.readCount) / parseFloat(this.bookList.length * this.allUser.length)).toFixed(2)*100" :stroke-width="26" :color="customColorMethod"></el-progress>
  </div>
</template>
<script>
import service from '@/utils/request'

export default {
  data() {
    return {
      percentage: 0,
      readCount: 0,
      customColor: '#409eff',
      customColors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ]
    };
  },
  props:['bookList','allUser'],
  watch: {
    bookList(newVal,oldVal){
      this.bookList = newVal
      this.readCount = 0
      this.percentage = 0
      console.log('booklength:'+this.bookList.length)
      console.log('userlength:'+this.allUser.length)
      this.bookList.forEach((book,index) => {
        service.get('/kwl/getKWLBybookId', { params: { bookId: book.id } }).then(res => {
          this.readCount += res.data.length
          if(index===this.bookList.length-1) {
            console.log('readCount:' + this.readCount)
          }
        })
      })
      // for(let i=0;i<this.bookList.length;i++){
      //   service.get('/kwl/getKWLBybookId', { params: { bookId: this.bookList[i].id }}).then(res => {
      //     this.readCount += res.data.length
      //     if(i===this.bookList.length-1){
      //       if(this.bookList.length!==0){
      //         console.log('readCount:'+this.readCount)
      //         this.percentage = (parseFloat(this.readCount) / parseFloat(this.bookList.length * this.allUser.length)).toFixed(2)*100
      //       }else{
      //         this.percentage = 0
      //       }
      //     }
      //   })
      // }
    }
  },
  methods: {
    customColorMethod(percentage) {
      if (percentage < 30) {
        return '#909399';
      } else if (percentage < 70) {
        return '#e6a23c';
      } else {
        return '#67c23a';
      }
    }
  }
}
</script>
