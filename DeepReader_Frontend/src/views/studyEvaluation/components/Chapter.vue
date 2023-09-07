<template>
  <div class="block" style="height: 720px; overflow: auto">
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
    <el-card shadow="never" style="font-weight: bold">
      {{userInfo.name}}同学你好，关于《{{book.name}}》,你目前高亮和标注了{{feedback.length}}处，
      <span v-if="feedback.length<5">高亮的内容过少，标注可以让自己更专心，促使自己边读边想，锻炼思考力。请再次仔细阅读，找到文章的主要观点、术语、主题、结论以及支撑论据等，用Hypothesis高亮并标注。</span>
      <span v-else-if="feedback.length>12">高亮的内容过多，没有把握文章重要内容，下次记得高亮文章重要观点和主题</span>
      <span v-else>真棒！你高亮的次数适中，有利于帮助自己掌握文章核心内容。</span>
      <p><span v-if="userscore>=0.6">太棒了，你高亮的内容得到大多数同学们的认可，请继续加油！</span>
        <span v-else-if="userscore>0&&userscore<0.6">你高亮的内容没有得到大多数同学们的认可，请反思一下这是为什么呢？但你也不要灰心，只要理解高亮的目的和好处，文本高亮策略可以帮助读者关注文章重要的概念或主题。在阅读学术性论文，通常高亮表示文本重要概念、专业俗语、主要观点，结论以及支撑论据等，不要高亮整个段落。请读完文章一次后，进行高亮，高亮数量不易过多，只需关注重要的词或句子。看看班上其它评价高的高亮词或句子，思考为什么它很重要，多多练习和思考，你也会被大家所认可，请继续努力！</span>
      <span v-else>同伴还未评论，请等待~</span></p>
    </el-card>
    <el-divider content-position="center"><span style="color:orange">你的文本标注</span></el-divider>
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
    <el-divider content-position="center"><span style="color:limegreen">班级优秀文本标注示例</span></el-divider>
    <el-table
      :data="supercomments"
      style="width: 100%">
      <el-table-column
        prop="annotation"
        label="高亮文本"
        :show-overflow-tooltip="true"
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
    <el-divider content-position="center"><span style="color:royalblue">感想</span></el-divider>
    <el-card>
    <el-form :model="annotationForm">
      <el-form-item label="当读到此反馈报告，请谈谈您的内心感受如何？" prop="desc">
        <el-input v-model="annotationForm.feeling" type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="你觉得此反馈报告对你有用吗？1-5分.为什么？" prop="desc">
        <el-rate v-model="annotationForm.usefulrate" @change="changeRate" style="float: left;margin-top: 7px" />
        <el-input v-model="annotationForm.usefulcause" type="textarea"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm()">保存</el-button>
      </el-form-item>
    </el-form>
    </el-card>

  </div>
</template>

<script>
import Axios from 'axios'
import { mapActions } from 'vuex'
import service from '@/utils/request'
export default {
  name: 'Chapter',
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      annotationForm: {
        feeling: this.$store.state.annotationfeedbackfeeling,
        usefulrate: this.$store.state.annotationfeedbackusefulrate,
        usefulcause: this.$store.state.annotationfeedbackusefulcause
        // feeling: '',
        // usefulrate: '',
        // usefulcause: ''
      },
      feedback: [],
      userInfo: '',
      supercomments:[],
      course:null,
      book: null,
      bookid:null,
      groupnameHp: '',
      usersentimentscore:'',
      hypothesisid:'',
      userscore:0,
      useravgscore:0
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  created() {
    this.bookid = this.$route.query.bookid
    this.getCourse(this.bookid)
  },
  mounted() {

  },
  methods: {
    ...mapActions("classify", ["getCourseBybookid"]),
    getCourse(bookid){
      this.getCourseBybookid(bookid).then(res=>{
        this.groupnameHp = res[0].groupnameHP
        this.getuserInfo()
        this.getBook(this.bookid)
        this.showAnnotations()
        this.showClassAnnotations()
      })
    },
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
        console.log('response:'+response.name)
        document.title = this.book.name
      });
    },
    changeRate() {
      this.$forceUpdate();
    },
    ...mapActions('user', ['getInfo','getUserCourse']),
    getuserInfo() {
      this.getInfo().then(res => {
        this.userInfo = res
        this.hypothesisid = this.userInfo.hypothesisname
        this.getUserCourse(this.userInfo.id).then(res=>{
          this.course = res
        })
      })
    },
    handleNodeClick(data) {
      console.log(data)
    },
    showAnnotations() {
      const username='acc:'+this.userInfo.hypothesisname+'@hypothes.is'
      console.log('username:'+username)
      Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          limit: 200, user: username, group: this.groupnameHp, uri: this.book.url
        }}).then(res => {
        let userrows = []
        let rowlist = res.data.rows
        console.log('rowlist:'+rowlist.length)
        for(let i=0;i<rowlist.length;i++){
          if(rowlist[i].user===username){
            userrows.push(rowlist[i])
          }
        }
        console.log('userroows:'+userrows.length)
        for(let i=0;i<userrows.length;i++) {
          let comments = []
          for (let j = 0; j < rowlist.length; j++) {
            if (rowlist[j]['references']) {
              for (let m = 0; m < rowlist[j]['references'].length; m++) {
                if (userrows[i].id === rowlist[j]['references'][m]) {
                  comments.push(rowlist[j].text+' ')
                }
              }
            }
          }
          if(userrows[i]['target'][0]['selector']){
            this.feedback.push({
              'annotation': userrows[i]['target'][0]['selector'][1]['exact'],
              'text': userrows[i]['text'],
              'comments': comments,
            })
          }
          let sentimentscore = 0
          for (let k = 0; k < comments.length; k++) {
            let data = { 'comment': comments[k] }
            Axios.post("http://222.198.118.211:9091/sentiments", data).then((res) => {
              sentimentscore += res.data.score
              if (k === comments.length - 1) {
                this.usersentimentscore = sentimentscore / comments.length
                this.userscore += this.usersentimentscore
                this.useravgscore++
                if (i === userrows.length - 1) {
                  this.userscore = this.userscore / this.useravgscore
                }
              }
            })
          }


          // }else{
          //   this.feedback.push({ 'annotation': userrows[i]['target'][0]['selector'][2]['exact'], 'text': userrows[i]['text'], 'comments': comments })
          // }
        }
      })
    },
    showClassAnnotations() {
      Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          group: this.groupnameHp, limit: 200, uri: this.book.url
        }}).then(res => {
        let rowlist = res.data.rows
        console.log(rowlist.length)
        let classcomments = []
        for(let i=0;i<rowlist.length;i++) {
          let comments = []
          for(let j=0;j<rowlist.length;j++){
            if(rowlist[j]['references']){
              for(let m=0;m<rowlist[j]['references'].length;m++){
                if(rowlist[i].id === rowlist[j]['references'][m]){
                  comments.push(rowlist[j].text)
                }
              }
            }
          }
          if(comments.length!==0){
            let sentimentscore = 0
            let usersentimentscore = 0
            for (let k = 0; k < comments.length; k++) {
              let data = { 'comment': comments[k] }
              Axios.post("http://222.198.118.211:9091/sentiments", data).then((res) => {
                sentimentscore += res.data.score
                if (k === comments.length - 1) {
                  usersentimentscore = sentimentscore / comments.length
                  classcomments.push({ 'comment': comments, 'from': rowlist[i], 'score': usersentimentscore })
                  if (i === rowlist.length - 1) {
                    console.log('classcomments:' + classcomments.length)

                    let scorelist = []
                    for (let n = 0; n < classcomments.length; n++) {
                      if(classcomments[n].score>0.8){
                        scorelist.push(classcomments[n])
                        console.log(classcomments[n].comment)
                        console.log(classcomments[n].score)
                      }
                    }
                    // score = score.sort((old, New) => {
                    //   return New - old
                    // })
                    // console.log('score:'+score)
                    for (let l = 0; l < 8; l++) {
                      if (scorelist[l].from.target[0]['selector']) {
                        this.supercomments.push({
                          'annotation': scorelist[l].from.target[0]['selector'][1]['exact'],
                          'text': scorelist[l].from.text,
                          'comments': scorelist[l].comment
                        })
                      }
                    }
                  }
                }

              })
            }
          }
        }
        // for(let i=0;i<rowlist.length;i++) {
        //   for (let j = 0; j < rowlist.length; j++) {
        //     if (rowlist[j]['references']) {
        //       if (rowlist[j]['references'][0] === rowlist[i]['id']) {
        //         classcomments.push({'comment':rowlist[j]['text'],'from':rowlist[i]})
        //       }
        //       if(rowlist[j]['references'][1]){
        //         if (rowlist[j]['references'][1] === rowlist[i]['id']){
        //           classcomments.push({'comment':rowlist[j]['text'],'from':rowlist[i]})
        //         }
        //       }
        //     }
        //   }
        // }

        // let sentimentscore = []
        // for(let i=0;i<classcomments.length;i++){
        //   let data = {'comment':classcomments[i].comment}
        //   Axios.post("http://222.198.118.211:9091/sentiments",data).then((res) => {
        //     console.log('classcomment:'+classcomments[i].comment)
        //     let score = res.data.score
        //     scorelist[score] = classcomments[i]
        //     console.log('scorelist:'+scorelist[score].from.target[0]['selector'][1]['exact'])
        //     sentimentscore.push(res.data.score)
        //     if(i===classcomments.length-1){
        //       sentimentscore = sentimentscore.sort((old,New)=>{
        //         return New -old
        //       })
        //       console.log('sentimentscore:'+sentimentscore[0])
        //       for(let j=3;j<8;j++){
        //         if(scorelist[sentimentscore[j]].from.target[0]['selector']){
        //           this.supercomments.push({ 'annotation': scorelist[sentimentscore[j]].from.target[0]['selector'][1]['exact'], 'text': scorelist[sentimentscore[j]].from.text, 'comments': scorelist[sentimentscore[j]].comment })
        //         }
        //           // else{
        //         //   this.supercomments.push({ 'annotation': scorelist[sentimentscore[j+5]].from.target[0]['selector'][1]['exact'], 'text': scorelist[sentimentscore[j+5]].from.text, 'comments': scorelist[sentimentscore[j+5]].comment })
        //         // }
        //       }
        //       const readfeedback = [{feedback:this.feedback,supercomments:this.supercomments}]
        //       this.$store.state.readfeedback = readfeedback
        //     }
        //   })
        // }
      })
    },
    submitForm(){
      this.$store.state.annotationfeedbackfeeling = this.annotationForm.feeling
      this.$store.state.annotationfeedbackusefulrate = this.annotationForm.usefulrate
      this.$store.state.annotationfeedbackusefulcause = this.annotationForm.usefulcause
      this.$message.success('保存成功')
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
