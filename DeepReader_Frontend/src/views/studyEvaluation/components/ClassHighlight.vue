<template>
  <div class="block" style="height: 650px; overflow: auto">
<!--    <h3 style="text-align: center">《{{book.name}}》标注文本</h3>-->
<!--    <el-divider content-position="right">作者：{{book.author}}</el-divider>-->
    <!--    <div v-for="(fb,index) in this.feedback">-->
    <!--      <h4 style="text-align: left">标注内容：{{fb['text']}}</h4>-->
    <!--      <p style="text-align: left">-->
    <!--        高亮文本：{{fb['annotation']}}-->
    <!--      </p>-->
    <!--      <p style="text-align: left" v-for="(comment, index) in fb['comments']">-->
    <!--        同伴评价：{{comment}}-->
    <!--      </p>-->
    <!--    </div>-->
    <el-table
      :data="feedback"
      style="width: 100%">
      <el-table-column
        prop="annotation"
        label="高亮文本"
        width="350">
      </el-table-column>
      <el-table-column
        prop="text"
        label="记录文本"
        width="350">
      </el-table-column>
      <el-table-column
        prop="comments"
        label="同伴评论">
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import Axios from 'axios'
import { mapActions } from 'vuex'
export default {
  name: 'Chapter',
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      feedback: [],
      userInfo: ''
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  created() {
    this.showAnnotations()
    this.getuserInfo()
  },
  methods: {
    ...mapActions('user', ['getInfo']),
    getuserInfo() {
      this.getInfo().then(res => {
        this.userInfo = res
        console.log('user:', res)
      })
    },
    handleNodeClick(data) {
      console.log(data)
    },
    showAnnotations() {
      Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          group: '4kMmkaAL'
        }}).then(res => {
        let rowlist = res.data.rows
        for(let i=0;i<rowlist.length;i++){
          if(rowlist[i]['permissions']['admin'][0].includes(this.userInfo.username)){
            let comments = []
            for(let j=0;j<rowlist.length;j++){
              if(rowlist[j]['references']){
                if(rowlist[j]['references'][0] === rowlist[i]['id']){
                  comments.push(rowlist[j]['text'])
                }
              }
            }
            console.log('comments:', comments)
            this.feedback.push({ 'annotation': rowlist[i]['target'][0]['selector'][2]['exact'], 'text': rowlist[i]['text'], 'comments': comments })
          }
        }
      })
    }
  }
}
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
