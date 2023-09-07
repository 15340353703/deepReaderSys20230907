<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success">
      <el-step title="查看问题反馈报告"></el-step>
      <el-step title="文章总结"></el-step>
      <el-step title="深度提问"></el-step>
    </el-steps>
    <el-container>
      <el-main>
        <div style="text-align: center">
          <el-row :gutter="20">
            <div>
              <el-col :span="contentSpan" :xs="24">
                <el-card>
                  <el-tabs @tab-click="handleclick" v-model="activeTab2">
                    <el-tab-pane label="阅读反馈报告" name="chapter">
<!--                      <chapter :book="book" />-->
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
                          {{userInfo.yourname}}同学你好，关于《{{book.name}}》,你目前高亮和标注了{{feedback.length}}处<br><br>
                          <span style="color:#B22222" v-if="feedback.length<5">高亮的内容过少，标注可以让自己更专心，促使自己边读边想，锻炼思考力。请再次仔细阅读，找到文章的主要观点、术语、主题、结论以及支撑论据等，用Hypothesis高亮并标注。</span>
                          <span style="color:#B22222" v-else-if="feedback.length>12">高亮的内容过多，没有把握文章重要内容，下次记得高亮文章重要观点和主题</span>
                          <span v-else style="color:#228B22">真棒！你高亮的次数适中，有利于帮助自己掌握文章核心内容。</span>
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
                          </el-form>
                        </el-card>

                      </div>

                    </el-tab-pane>
                    <el-tab-pane label="文章内容" name="myContent">
                      <myContent :book="book" />
                    </el-tab-pane>
                  </el-tabs>
                </el-card>
              </el-col>
            </div>
            <el-col :span="questionSpan" :xs="24">
              <el-card>
                <el-tabs v-model="activeTab">
                  <el-tab-pane label="创建总结句" name="strategyForm">
<!--                    <feedback-->
<!--                      :user="user"-->
<!--                      :book="book"-->
<!--                      @close="closeContent()"-->
<!--                      @next="nextPage()"-->
<!--                      @open="openContent()"-->
<!--                      @back="backPage()"-->
<!--                      @active="changePercent()"-->
<!--                    />-->
                    <div id="nav" style="height: 720px;">
                      <el-scrollbar v-if="!showContent" style="height:100%">

                        <el-card class="box-card">
                          <ul class="text item" style="text-align: left">
                            <li>请根据阅读反馈报告，结合高亮的关键信息，创建多条总结句。</li>
                            <li>总结句用凝练话语概括一篇文章或者一个段落的中心思想，通过对于内容的提炼和用自己的语言复述等过程，可以使阅读者加深对于阅读内容的理解，加强重要信息的长时间记忆。</li>
                            <li>总结句应该流程、易读，避免笼统、强调明确，最好在20-30字之内，包含文章主旨关键词。</li>

                          </ul>
                        </el-card>

                        <el-divider><i class="el-icon-notebook-2" /></el-divider>

                        <div style="text-align: center">
                          <el-form
                            ref="dynamicValidateForm"
                            :model="dynamicValidateForm"
                            label-width="100px"
                            class="demo-dynamic"
                          >
                            <el-form-item
                              v-for="(s, index) in dynamicValidateForm.summary"
                              :key="s.key"
                              :label="'总结' + (index+1)"
                              :prop="'summary.' + index + '.value'"
                              :rules="{
                              required: true,
                              message: '不可为空！',
                              trigger: 'blur',
                            }"
                            >
                              <el-col :span="20">
                                <el-input
                                  v-model="s.value"
                                  type="textarea"
                                  :rows="2"
                                  placeholder="请输入内容"
                                />
                              </el-col>
                              <el-button
                                type="danger"
                                @click.prevent="removeSummary(s)"
                                icon="el-icon-delete" circle
                              ></el-button>
                            </el-form-item>

                            <el-button
                              size="medium"
                              type="success"
                              @click="addSummary"
                              icon="el-icon-plus" circle
                            ></el-button>
                          </el-form>
                        </div>
                        <br>
                        <div style="text-align: center">

                          <el-button size="medium" type="info" @click="back(book.id)">上一步</el-button>
                          <!--<el-button @click="back" size="medium" type="warning" v-if="active == 2">返回上一步</el-button>-->
                          <el-button
                            size="medium"
                            type="warning"
                            @click="resetForm('dynamicValidateForm')"
                          >重置</el-button>
                          <el-button size="medium" type="primary" @click="nextstep(book.id)">下一步</el-button>
                        </div>
                      </el-scrollbar>
                    </div>

                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import StrategyForm from "./components/StrategyForm";
import MyContent from "./components/MyContent";
import Chapter from "./components/Chapter";
import feedback from '@/views/studyEvaluation/components/feedback'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
import Axios from 'axios'

export default {
  name: "Study",
  components: { MyContent, StrategyForm, Chapter, feedback },
  data() {
    return {
      // book: {
      //   bookId: "",
      //   name: "",
      //   author: "",
      //   pages: "",
      //   brief_introduction: "",
      //   url: "",
      // },
      contentSpan: 14,
      questionSpan: 10,
      active: 1,
      percent: 0,
      user: {},
      major: {},
      activeTab: "strategyForm",
      activeTab2: "chapter",

      annotationForm: {
        feeling: this.$store.state.annotationfeedbackfeeling,
        usefulrate: this.$store.state.annotationfeedbackusefulrate,
        usefulcause: this.$store.state.annotationfeedbackusefulcause
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
      useravgscore:0,
      showContent:'',

      dialogFormVisible: false,
      questionNum: 0,
      fullscreenLoading: false,
      dynamicValidateForm: {
        bookId: "",
        summary: [{
          value: '',
          key: Date.now()}]
      },
      transferProps: {
        key: 'key',
        label: 'value'
      },
    };
  },
  computed: {
    ...mapState("books", ["title"]),
  },
  created() {
    this.bookid = this.$route.query.bookid
    this.getCourse(this.bookid)
  },
  methods: {
    ...mapActions("classify", ["getCourseBybookid"]),
    getCourse(bookid){
      this.getCourseBybookid(bookid).then(res=>{
        this.groupnameHp = res[0].groupnameHP
        this.getBook(this.bookid)
      })
    },
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
        document.title = this.book.name
        this.getuserInfo()
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
        this.showAnnotations(this.book)
        this.showClassAnnotations()
        this.getUserCourse(this.userInfo.id).then(res=>{
          this.course = res
        })
      })
    },
    handleNodeClick(data) {
      console.log(data)
    },
    showAnnotations(book) {
      console.log('bookurl:'+book.name)
      const username='acct:'+this.userInfo.hypothesisname+'@hypothes.is'
      console.log('username:'+username)
      Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          limit: 200, user: username, group: this.groupnameHp, uri: book.url
        }}).then(res => {
        let userrows = res.data.rows
        for(let i=0;i<userrows.length;i++) {
          let comments = []
          Axios.get('https://api.hypothes.is/api/search', { headers: { Authorization: 'Bearer'+' '+'6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
              limit: 200, references: userrows[i].id, group: this.groupnameHp, uri: book.url
            }}).then(res => {
              let refs = res.data.rows
              for(let j=0;j<refs.length;j++){
                comments.push(refs[j].text+' ')
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
            this.feedback.push({
              'annotation': userrows[i]['target'][0]['selector'][1]['exact'],
              'text': userrows[i]['text'],
              'comments': comments,
              'peerscore': this.userscore
            })
        })
        }
        // console.log('userrows:'+userrows)
        // for(let i=0;i<userrows.length;i++) {
        //   let comments = []
        //   for (let j = 0; j < rowlist.length; j++) {
        //     if (rowlist[j]['references']) {
        //       for (let m = 0; m < rowlist[j]['references'].length; m++) {
        //         if (userrows[i].id === rowlist[j]['references'][m]) {
        //           comments.push(rowlist[j].text+' ')
        //         }
        //       }
        //     }
        //   }
        //   if(userrows[i]['target'][0]['selector']){
        //     this.feedback.push({
        //       'annotation': userrows[i]['target'][0]['selector'][1]['exact'],
        //       'text': userrows[i]['text'],
        //       'comments': comments,
        //     })
        //   }

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
                  comments.push(rowlist[j].text+' ')
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
      })
    },
    handleclick(obj){
      const id = this.$route.query.bookid;
      if(obj.name==='myContent') {
        const data = {userid: getToken(),behavior:'查看文章',bookid:id,stamptime:new Date()}
        service.post('/user/readlog',data).then(res=>{
          console.log('记录查看文章')
        })
      }
    },

    ...mapActions('user', ['updateStuInfo','updateUserKWLForm','updateQuestionForm']),
    next() {
      this.timeCount()
      // if (this.active++ > 2) this.active = 0;
      // console.log(this.dynamicValidateForm.doubts[0].value);
      // console.log(this.dynamicValidateForm.doubts[0].result);
      if (this.active == 0) {
        var result = true
        for (var i = 0; i < this.dynamicValidateForm.doubts.length; i++) {
          var val = this.dynamicValidateForm.doubts[i].value
          if (val == '' || val == null) {
            this.failNextAtFirstForm()
            result = false
            break
          }
        }
        for (var i = 0; i < this.dynamicValidateForm.summary.length; i++) {
          var val = this.dynamicValidateForm.summary[i].value
          if (val == '' || val == null) {
            this.failNextAtFirstForm()
            result = false
            break
          }
        }
        if (result) {
          this.submitQuestion()
        }
      } else if (this.active == 1) {
        var result = true
        for (var i = 0; i < this.dynamicValidateForm.doubts.length; i++) {
          console.log(this.dynamicValidateForm.doubts[i])
          var val = this.dynamicValidateForm.doubts[i].result
          if (val == '' || val == null) {
            this.failNextAtSecondForm()
            result = false
            break
          }
        }
        if (result) {
          this.dynamicValidateForm.time=this.readTime
          this.active++
          this.showContent = true
          this.$emit('active', this.active)
          this.$emit('close')
        }
      } else if (this.active == 2) {
        this.active++
        this.$emit('active', this.active)
        this.$emit('open')
        this.showContent = false
      } else if (this.active == 3) {
        /*
        if (questionClues == '' || questionAnswer == '') {
          this.failSubimit()
        }else{
          this.submit()
        }
        */
        this.submit()
      } else {
        this.active = 0
        this.$emit('active', this.active)
        this.$emit('back')
        this.$router.push('/bookSelection/index');
      }
    },
    // back() {
    //   this.active--
    // },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    removeDomain(item) {
      var index = this.dynamicValidateForm.doubts.indexOf(item)
      var num = this.dynamicValidateForm.doubts.length
      if (index !== -1 && num > 1) {
        this.dynamicValidateForm.doubts.splice(index, 1)
      }
      if (num == 1) {
        this.failDelete()
      }
    },
    removeSummary(item) {
      var index = this.dynamicValidateForm.summary.indexOf(item)
      var num = this.dynamicValidateForm.summary.length
      if (index !== -1 && num > 1) {
        this.dynamicValidateForm.summary.splice(index, 1)
      }
      if (num == 1) {
        this.failDelete()
      }
    },
    addDomain() {
      var num = this.dynamicValidateForm.doubts.length
      if (num >= 5) {
        this.failAdd()
      }
      if (num < 5) {
        this.dynamicValidateForm.doubts.push({
          value: '',
          key: Date.now()
        })
      }
    },
    addSummary() {
      var num = this.dynamicValidateForm.summary.length
      if (num >= 5) {
        this.failAdd()
      }
      if (num < 5) {
        this.dynamicValidateForm.summary.push({
          value: '',
          key: Date.now()
        })
      }
    },
    failDelete() {
      this.$alert('请至少进行一项总结', '删除失败', {
        confirmButtonText: '确定',
        callback: (action) => {
          this.$message({
            type: 'info',
            message: `action: 禁止删除`
          })
        }
      })
    },
    failAdd() {
      this.$alert('至多进行五项总结', '增加失败', {
        confirmButtonText: '确定',
        callback: (action) => {
          this.$message({
            type: 'info',
            message: `action: 禁止增加`
          })
        }
      })
    },

    failNextAtFirstForm() {
      this.$alert('提问或已知不可以为空，请填写您的问题或删除部分空白问题', '跳转失败', {
        confirmButtonText: '确定'
      })
    },

    submitQuestion() {
      this.$confirm('是否结束泛读，进入精读阶段?（您将不能重新修改问题）', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.active++
        this.$emit('active', this.active)
        this.$emit('next')
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消提交'
        })
      })
    },
    nextstep(id) {
      const readfeedback = [{feedback:this.feedback,supercomments:this.supercomments}]
      this.$store.state.readfeedback = readfeedback
      this.$store.state.summary = this.dynamicValidateForm.summary
      this.$store.state.annotationfeedbackfeeling = this.annotationForm.feeling
      this.$store.state.annotationfeedbackusefulrate = this.annotationForm.usefulrate
      this.$store.state.annotationfeedbackusefulcause = this.annotationForm.usefulcause
      this.$router.push({path:'/askQuestion/index', query:{bookid: id}})
    },
    back(id) {
      this.$router.push({path:'/studyQuestion/index', query:{bookid: id}})
    },

    changeCard() {
      if (this.activeTab2 == "chapter") {
        this.activeTab2 = "myContent";
      } else {
        this.activeTab2 = "chapter";
      }
    },
    nextPage() {
      this.activeTab2 = "myContent";
      this.active = 1;
    },
    backPage() {
      this.activeTab2 = "chapter";
      this.active = 0;
    },
    changePercent() {
      if (this.percent >= 4) {
        this.percent = 0;
      } else {
        this.percent++;
      }
    },
  },
};
</script>

<style>
</style>
