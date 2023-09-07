<template>
  <div id="nav" style="height: 650px;">
    <el-scrollbar style="height:100%">

      <el-card v-if="active == 0" class="box-card">
        <div class="text item">
          请根据你的提问能力反馈报告，结合总结句，创建测试题，考考你的同伴，主要目的在于检验学习者对文章主要内容、主题思想的理解，问题的答案在文章可以找到。
        </div>
      </el-card>

      <el-card v-if="active == 1" class="box-card" style="text-align: center">
        <el-button type="success" icon="el-icon-check" circle />
        <div class="text item">
          <br>
          提交成功
        </div>
      </el-card>
      <el-card v-if="active == 3" class="box-card">
        <div class="text item">
          小机器人尝试从不同角度提出问题，帮助你反思问题质量，从而提升你的提问能力。
        </div>
      </el-card>

      <el-divider><i class="el-icon-star-off" /></el-divider>

      <div style="text-align: center">

        <el-form
          v-if="active == 0"
          style="text-align: left"
          class="demo-dynamic"
        >
          <el-form-item>
            <el-collapse v-model="activeName" accordion style="width:600px">
<!--              <el-collapse-item title="文章总结" name="1">-->
                <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
              <el-checkbox style="width:100%;margin-top:-20px" v-for="city in summarys" :label="city" :key="city">{{city.value}}</el-checkbox>
                </el-checkbox-group>
<!--              </el-collapse-item>-->
            </el-collapse>
            <el-form-item label="问题线索" prop="questionClues">
              <el-tooltip class="item" effect="dark" content="文中主要的文本片段，可以暗示答案的问题答案（您的问题来源于文中的哪些关键句？）" placement="top">
                <i class="el-icon-question" />
              </el-tooltip>
              <el-input
                v-model="questionForm.questionClues"
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 8}"
                placeholder="请给出其他同学查找该问题答案的线索"
              />
            </el-form-item>
          </el-form-item>
          <el-form-item label="问题类型">
            <el-select v-model="questionForm.questionType" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="问题">
            <el-tooltip class="item" effect="dark" content="请输入您为其他提出的问题" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
            <el-input
              v-model="questionForm.questionDescribe"
              placeholder="请输入向其他同学提出的问题"
            />
          </el-form-item>

          <el-form-item label="问题答案">
            <el-input
              v-if="saveModifyQuestion == false"
              v-model="questionForm.questionDescribe"
              placeholder="请输入向其他同学提出的问题"
            />
            <el-input
              v-if="saveModifyQuestion"
              v-model="questionForm.modifyQuestion"
              placeholder="请输入向其他同学提出的问题"
            />
          </el-form-item>

          <el-form-item label="请求小机器人的帮助">
            <el-tooltip class="item" effect="dark" content="小机器人正处于学习初级阶段，希望得到你的帮助提高问题质量。" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
            <br>

            <el-image
              style="width: 100px; height: 100px"
              :src="require('../../../assets/robot.jpg')"
              :fit="fit"
              @click="robotHelp"
            />
          </el-form-item>
        </el-form>

      </div>
      <div v-if="this.active==3">
        <el-card>
          <el-row>
            <el-col :span="24">
              <el-form>
                <div style="background-color: #1aa2b8;text-align: left;width: 100%">
                  <div style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 13px">你提出的问题和答案</div>
                  <div style="color: white;margin: 8px;">
                    <div>问题：{{ questionForm.questionDescribe }} </div>
                    <br>
                    <div style="padding-top: 13px">答案：{{ questionForm.questionAnswer }}</div>
                  </div>
                </div>
                <div style="background-color: #1aa2b8;text-align: left;width: 100%">
                  <span style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 8px">小机器人提出的问题和答案</span>
                  <el-button style="background-color:white;color: #ffba00;margin-top: 8px;font-size: 14px;" @click="morerobotQuestion">点击获取更多机器人提问</el-button>
                  <div v-if="questionForm.onerobotQuestion === ''" style="color: #ffba00;font-weight: bold;font-size: 16px;text-align: center;margin: 8px;padding-top: 8px">正在加载中......</div>
                  <div v-if="questionForm.onerobotQuestion" style="white-space: pre-wrap;color: white;margin: 8px;">{{ questionForm.onerobotQuestion }}</div>
                  <span v-if="showmorerobotQuestion === true" style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 8px">更多机器人提问</span>
                  <div v-if="questionForm.morerobotQuestion === '' && showmorerobotQuestion === true" style="color: #ffba00;font-weight: bold;font-size: 16px;text-align: center;margin: 8px;padding-top: 8px">正在加载中......</div>
                  <div v-if="questionForm.morerobotQuestion && showmorerobotQuestion === true" style="white-space: pre-wrap;color: white;margin: 8px;">{{ questionForm.morerobotQuestion }}</div>
                </div>
                <el-form-item label="你是否愿意采用小机器人提出的问题" style="text-align: left">
                  <el-button type="primary" size="small" @click="modifyQuestion">愿意</el-button>
                  <div v-if="showmodifyQuestion">
                    <div>从表达是否合理、问题是否与材料有关、是否引发深层次思考等角度出发可以对机器人提出的问题进行修改</div>
                    <div>
                      <span>选择问题：</span>
                      <el-select v-model="selectedObj" @change="handleObjChange" placeholder="请选择问题" style="width: 85%;margin-top: 8px">
                        <el-option v-for="obj in allQuestionList" :key="obj.id" :label="obj.question" :value="obj"></el-option>
                      </el-select>
                    </div>
                    <div>
                      <span>新问题：</span>
                      <el-input v-model="selectedObj.question"  placeholder="可以修改小机器人的提问" style="width:87%;margin-top: 8px"></el-input>
                    </div>
                    <div>
                      <span>答案：</span>
                      <el-input v-model="selectedObj.answer" placeholder="可以修改小机器人提问的答案" style="width: 90%;margin-top: 8px"></el-input>
                    </div>
                    <div>
                      <span>补充线索：</span>
                      <el-input
                        v-model="questionForm.supplementClues"
                        type="textarea"
                        :autosize="{ minRows: 4, maxRows: 8}"
                        placeholder="如果该问题的答案不能在文中找到，请在这里补充线索"
                      />
                    </div>
                  </div>
                </el-form-item>
                <el-form-item label="你觉得小机器人提出的问题对你有帮助吗？">
                  <el-rate v-model="questionForm.robotRate" :show-text="true" :texts="texts" style="margin-top: 8px;width: 600px;margin-left: -100px;"/>
                  <!--                  <el-input-->
                  <!--                  v-model="questionForm.modifyQuestion"-->
                  <!--                  placeholder="请选择具体有哪些帮助" />-->
<!--                  <el-select v-if="questionForm.robotRate < 3" v-model="questionForm.suggestionhelp" multiple placeholder="请选择具体有哪些帮助" style="width: 100%;margin-top: 8px">-->
<!--                    <el-option v-for="option in badsuggestionhelp" :key="option" :value="option">{{ option }}</el-option>-->
<!--                  </el-select>-->
                  <el-select v-model="questionForm.suggestionhelp" multiple placeholder="请选择具体有哪些帮助" style="width: 100%;margin-top: 8px">
                    <el-option v-for="option in goodsuggestionhelp" :key="option" :value="option">{{ option }}</el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">提交</el-button>
              </div>
            </el-col>
          </el-row>
          <div>
            <el-button type="primary" @click="saveQuestion">保存</el-button>
            <el-button @click="cancelQuestion">取消</el-button>
          </div>
        </el-card>
      </div>

<!--      <div v-if="this.active==0">-->
<!--        <el-dialog title="小机器人正处于学习初级阶段，希望得到你的帮助提高问题质量" :visible.sync="dialogFormVisible">-->
<!--          <el-row>-->
<!--            <el-col :span="18">-->
<!--              <el-form>-->
<!--                <el-form-item label="小机器人提问" :label-width="formLabelWidth">-->
<!--                  <p>{{ questionForm.robotQuestion }}</p>-->
<!--                </el-form-item>-->
<!--                <el-form-item label="你的提问" :label-width="formLabelWidth">-->
<!--                  <p>{{ this.questionForm.questionDescribe }}</p>-->
<!--                </el-form-item>-->
<!--                <el-form-item label="对比你提出的问题，你觉得小机器人提出的问题对你有帮助吗？">-->
<!--                  <el-rate v-model="questionForm.robotRate" />-->
<!--                </el-form-item>-->
<!--                <el-form-item v-if="this.questionForm.robotRate<=3" label="请改进小机器人提出的问题">-->
<!--                  <el-input-->
<!--                  v-model="questionForm.modifyQuestion"-->
<!--                  placeholder="请输入内容" />-->
<!--                </el-form-item>-->
<!--              </el-form>-->
<!--              <div slot="footer" class="dialog-footer">-->
<!--                <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--                <el-button type="primary" @click="dialogFormVisible = false">提交</el-button>-->
<!--              </div>-->
<!--            </el-col>-->
<!--            <el-col :span="6">-->
<!--              <el-image-->
<!--              style="width: 100px; "-->
<!--              :src="require('../../../assets/robotHelp.png')"-->
<!--              :fit="fit"></el-image>-->
<!--            </el-col>-->
<!--          </el-row>-->
<!--        </el-dialog>-->
<!--      </div>-->

      <div style="text-align: center">
        <el-button v-if="active !== 0 && active !== 3" size="medium" type="success" @click="createQuestion(book)">继续创建测试题</el-button>
        <el-button v-if="active !== 0 && active !== 3" size="medium" type="primary" @click="backtofirst(book)">返回选择文章页面</el-button>
        <el-button v-if="active===0" size="medium" type="info" @click="back(book)">上一步</el-button>
        <el-button v-if="active===0" size="medium" type="success" @click="next">提交问题</el-button>
      </div>

    </el-scrollbar>

  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import service from "@/utils/request";
import { getToken, setToken, removeToken } from '@/utils/auth'
import Axios from 'axios'
// const summaryOptions = ['总结1', '总结2', '总结3', '总结4'];
export default {
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      saveModifyQuestion: false,
      allQuestionList: [],
      selectedObj: {},
      goodsuggestionhelp: [
        '问题表述清晰准确，帮助提升我的问题表达能力',
        '问题具有深度和广度，帮助我深层次思考',
        '问题具有开放性和启发性，帮助提升我的创造力',
        '问题具有相关性和实用性，帮助激发我的发散思维',
        '问题难度适当，帮助提升我的反思能力',
        '问题模糊不清',
        '问题深度停留在材料本身',
        '问题难度过高（过低）',
        '提问角度单一',
        '问题与实际应用无关'
      ],
      // badsuggestionhelp: [
      //   '问题模糊不清',
      //   '问题深度停留在材料本身',
      //   '问题难度过高（过低）',
      //   '提问角度单一',
      //   '问题与实际应用无关'
      // ],
      texts: ['没有帮助', '帮助不大', '一点帮助', '有有帮助', '很大帮助'],
      Qclues: '',
      // allQuestionList: [
      //   // { questions: '问题1', answers: '答案1' },
      //   // { questions: '问题2', answers: '答案2' },
      //   // { questions: '问题3', answers: '答案3' },
      //   // { questions: '问题4', answers: '答案4' },
      //   { id: 1, name: 'obj1', otherAttr: 'other1' },
      //   { id: 2, name: 'obj2', otherAttr: 'other2' },
      //   { id: 3, name: 'obj3', otherAttr: 'other3' }
      // ],
      showmodifyQuestion: false,
      showmorerobotQuestion: false,
      bookid:this.$route.query.bookid,
      checkAll: false,
      checkedCities: [],
      isIndeterminate: true,
      summarys:this.$store.state.summary,
      questionfeedback: this.$store.state.questionfeedback,
      readfeedback: this.$store.state.readfeedback,
      questionfeedbackfeeling: this.$store.state.questionfeedbackfeeling,
      questionfeedbackusefulrate: this.$store.state.questionfeedbackusefulrate,
      questionfeedbackusefulcause: this.$store.state.questionfeedbackusefulcause,
      annotationfeedbackfeeling: this.$store.state.annotationfeedbackfeeling,
      annotationfeedbackusefulrate: this.$store.state.annotationfeedbackusefulrate,
      annotationfeedbackusefulcause: this.$store.state.annotationfeedbackusefulcause,
      questionType: '',
      newData: '新数据',
      url: 'https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/src=http___img.zcool.cn_community_01dcd75af39d3ba8012160456172c4.jpg@1280w_1l_2o_100sh.jpg&refer=http___img.zcool.jpg',
      choiceData: [],
      active: 0,
      dialogFormVisible: false,
      haveSubmit: true,
      fullscreenLoading: false,
      questionNum: 0,
      questionForm: {
        bookId: '',
        robotRate: 0,
        robotQuestion: '',
        modifyQuestion: '',
        questionType: '',
        questionDescribe: '',
        questionClues: '',
        questionAnswer: '',
        onerobotQuestion: '',
        morerobotQuestion: '',
        suggestionhelp: '',
        supplementClues: '',
        clues:'',

      },
      // questionForm: {
      //   bookId: '',
      //   robotRate: 0,
      //   modifyQuestion: '',
      //   robotQuestion:'',
      //   questionType: '',
      //   questionDescribe: '',
      //   questionClues: '',
      //   questionAnswer: '',
      //   summary:''
      // },
      qid:'',
      transferProps: {
        key: 'key',
        label: 'value'
      },
      options: [{
        value: '时间',
        label: '时间'
      }, {
        value: '谁',
        label: '谁'
      }, {
        value: '为什么',
        label: '为什么'
      }, {
        value: '怎么样',
        label: '怎么样'
      }, {
        value: '什么',
        label: '什么'
      }, {
        value: '哪些',
        label: '哪些'
      }, {
        value: '哪里',
        label: '哪里'
      }, {
        value: '哪个',
        label: '哪个'
      }, {
        value: '数字',
        label: '数字'
      }]
    }
  },
  created() {
    console.log(this.$store.state.summary)
  },
  computed: {
    ...mapState('user', ['name'])
  },
  methods: {
    // handleCheckAllChange(val) {
    //   this.checkedCities = val ? summaryOptions : [];
    //   this.isIndeterminate = false;
    // },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.cities.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;
    },
    ...mapActions('user', ['updateStuInfo','updateUserKWLForm','updateQuestionForm','sendChatGPT']),
    next() {
      if (this.active === 0) {
        this.submit()
      } else {
        this.active = 0
        this.$emit('active', this.active)
        this.$emit('back')
        this.$router.push('/bookSelection/index');
      }
    },
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
    // resetQuestionForm(){
    //   this.questionForm.robotRate=0
    //   this.questionForm.robotQuestion = ''
    //   this.questionForm.modifyQuestion=""
    //   this.questionForm.questionType=""
    //   this.questionForm.questionDescribe=""
    //   this.questionForm.questionClues=""
    //   this.questionForm.questionAnswer=""
    // },
    resetQuestionForm(){
      this.questionForm.robotRate=0
      this.questionForm.modifyQuestion=""
      this.questionForm.questionType=""
      this.questionForm.questionDescribe=""
      this.questionForm.questionClues=""
      this.questionForm.questionAnswer=""
      this.questionForm.onerobotQuestion = ''
      this.questionForm.morerobotQuestion = ''
      this.questionForm.suggestionhelp = ''
      this.questionForm.supplementClues = ''
      this.allQuestionList = []
      this.showmorerobotQuestion = false
      this.showmodifyQuestion = false
      this.saveModifyQuestion = false
      this.questionForm.onerobotQuestion = ''
      this.questionForm.morerobotQuestion = ''
      this.selectedObj = {}
    },
    submit() {
      // if (this.questionForm.questionDescribe === '' && this.questionForm.modifyQuestion !== ''){
      //   this.questionForm.questionDescribe = '无'
      // }
      // if (this.questionForm.questionDescribe !== '' && this.questionForm.modifyQuestion === ''){
      //   this.questionForm.modifyQuestion = '无'
      // }
      if (this.questionForm.questionType!=='' && this.questionForm.questionType!==null && this.questionForm.questionDescribe!=='' && this.questionForm.questionDescribe!==null && this.questionForm.questionAnswer!=='' && this.questionForm.questionAnswer!==null
        && this.questionForm.questionClues!=='' && this.questionForm.questionClues!==null) {
        this.$confirm('是否结束提问并提交！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.active++
          this.questionForm.bookId = this.book.id
          this.questionForm.summary = this.checkedCities
          console.log('问题类型' + this.questionForm.questionType)
          this.questionForm.robotQuestion = this.questionForm.onerobotQuestion + this.questionForm.morerobotQuestion
          this.updateQuestionForm(this.questionForm).then(res => {
            this.qid = res
            const data = {
              token: getToken(),
              questionid: this.qid,
              bookid: this.bookid,
              summarys: this.summarys,
              questionfeedbackfeeling: this.questionfeedbackfeeling,
              questionfeedbackusefulrate: this.questionfeedbackusefulrate,
              questionfeedbackusefulcause: this.questionfeedbackusefulcause,
              annotationfeedbackfeeling: this.annotationfeedbackfeeling,
              annotationfeedbackusefulrate: this.annotationfeedbackusefulrate,
              annotationfeedbackusefulcause: this.annotationfeedbackusefulcause,
              readfeedback: this.readfeedback,
              questionfeedback: this.$store.state.questionfeedback
            }
            if (this.summarys) {
              service.post('/dq/submitsummary', data).then(res => {
                console.log(res + '提交总结成功')
              })
            }
          })
          this.$store.state.questionfeedbackfeeling = ''
          this.$store.state.questionfeedbackusefulrate = ''
          this.$store.state.questionfeedbackusefulcause = ''
          this.$store.state.annotationfeedbackfeeling = ''
          this.$store.state.annotationfeedbackusefulrate = ''
          this.$store.state.annotationfeedbackusefulcause = ''
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消'
          })
        })
      } else {
        this.$message.warning('请将问题信息输入完整')
      }
    },
    splitQuestion(){
      const allRobotQuestion = (this.questionForm.onerobotQuestion + this.questionForm.morerobotQuestion).replace(/\?/g, '？').replace(/\./g, '。').replace(/:/g, '：')

      // let text = "问题1？问题2？问题3？答案1。答案2。答案3。";
      // let questionArr = allRobotQuestion.match(/[^？。]+[？]/g); // 匹配问号句子
      // let answerArr = allRobotQuestion.match(/[^？。]+[。]/g); // 匹配句号句子
      // const regex = /问题：([^?]+)\?答案：([^。]+)。/g;  [^：？]+[：？]([^：？]+)[：？]/g",
      // const questionArr = allRobotQuestion.match(/问题|问|Question|Q|Q1|Q2|Q3[^：？]+[：？]([^：？]+)[：？]/g); // 匹配问号句子
      // const answerArr = allRobotQuestion.match(/答案：|答：|Answer:|A|A1:|A2:|A3:+[^？。]+[。]/g); // 匹配问号句子
      // const questionArr = allRobotQuestion.match(/[^：？]+[：？]([^：？]+)[：？]/g); // 匹配问号句子
      // const answerArr = allRobotQuestion.match(/[^：。]+[：。]([^：。]+)[：。]/g); // 匹配问号句子
      const questionArr = allRobotQuestion.match(/[^？。]+[？]/g); // 匹配问号句子
      const answerArr = allRobotQuestion.match(/[^？。]+[。]/g); // 匹配问号句子
      for (let i = 0; i < questionArr.length; i++) {
        const obj = {};
        obj.question = questionArr[i];
        obj.answer = answerArr[i];
        this.allQuestionList.push(obj);
      }
    },
    robotHelp() {
      if ((this.questionForm.questionDescribe ==='' || this.questionForm.questionDescribe ===null )|| (this.questionForm.questionClues==='' || this.questionForm.questionClues===null)){
        this.$message.warning('请将问题信息输入完整')
      } else {
        this.active = 3
        console.log("this.questionForm.questionClues" + this.questionForm.questionClues)
        // 判断文本是中文为主还是英文为主
        const isChinese = /[\u4e00-\u9fa5]/.test(this.questionForm.questionClues); // 中文正则
        if (isChinese) {
          this.questionForm.clues = '请根据下面这段话以一问一答的形式提一个问题并给出答案：' + this.questionForm.questionClues
        } else {
          this.questionForm.clues = 'Please ask a question and provide an answer in the form of a question and answer based on the following paragraph:' + this.questionForm.questionClues
        }
        this.sendChatGPT(this.questionForm).then((res) => {
          console.log(res)
          this.questionForm.onerobotQuestion = res
        })
      }
      // let data = {clue: this.questionForm.questionClues,answer:this.questionForm.questionAnswer}
      // Axios.post("http://222.198.118.211:9091/autoquestion",data).then((res) => {
      //   this.questionForm.robotQuestion = res.data.question
      // });
      // this.fullscreenLoading = true;
      // setTimeout(() => {
      //   this.fullscreenLoading = false;
      //   this.dialogFormVisible = true
      // }, 3000);
    },
    morerobotQuestion(){
      this.showmorerobotQuestion = true
      // 判断文本是中文为主还是英文为主
      const isChinese = /[\u4e00-\u9fa5]/.test(this.questionForm.questionClues); // 中文正则
      if (isChinese){
        this.questionForm.clues = '请根据下面这段话以一问一答的形式提出多个问题并给出答案：'+this.questionForm.questionClues
      } else {
        this.questionForm.clues = 'Please ask multiple questions and provide answers in the form of a question and answer based on the following paragraph:'+this.questionForm.questionClues
      }
      console.log('clues:'+ this.questionForm.questionClues)
      this.sendChatGPT(this.questionForm).then((res) =>{
        console.log(res)
        this.questionForm.morerobotQuestion = res
      })
    },
    modifyQuestion(){
      this.selectedObj = {}
      this.splitQuestion()
      this.showmodifyQuestion = true
    },
    handleObjChange() {
      this.selectedObj = Object.assign({}, this.selectedObj);
    },
    saveQuestion(){
      this.questionForm.modifyQuestion = this.selectedObj.question
      this.questionForm.questionAnswer = this.selectedObj.answer
      this.saveModifyQuestion = true
      this.active = 0

    },
    cancelQuestion(){
      this.active = 0
      this.selectedObj = {}
      this.questionForm.robotRate = ''
      this.questionForm.modifyQuestion = ''
      this.questionForm.questionAnswer = ''
      this.questionForm.suggestionhelp = ''
      this.questionForm.supplementClues = ''
      this.showmorerobotQuestion = false
      this.showmodifyQuestion = false
      this.saveModifyQuestion = false
    },
    // handleChange(val) {
    //   console.log(val)
    // },
    // robotHelp() {
    //   let data = {clue: this.questionForm.questionClues,answer:this.questionForm.questionAnswer}
    //   Axios.post("http://222.198.118.211:9091/autoquestion",data).then((res) => {
    //     this.questionForm.robotQuestion = res.data.question
    //   });
    //   this.fullscreenLoading = true;
    //   setTimeout(() => {
    //     this.fullscreenLoading = false;
    //     this.dialogFormVisible = true
    //   }, 3000);
    // },
    toParent: function() {
      this.$emit('greet')
    },
    createQuestion(book) {
      this.active = 0
      // this.$emit('requestion')
      this.resetQuestionForm()
      this.$router.push({path:'/askQuestion/index',query:{bookid: book.id}})
    },
    back(book){
      this.$router.push({path:'/studyEvaluation/index',query:{bookid: book.id}})
    },
    backtofirst(book){
      this.$store.state.questionfeedbackfeeling = ''
      this.$store.state.questionfeedbackusefulrate = ''
      this.$store.state.questionfeedbackusefulcause = ''
      this.$store.state.annotationfeedbackfeeling = ''
      this.$store.state.annotationfeedbackusefulrate = ''
      this.$store.state.annotationfeedbackusefulcause = ''
      this.$store.state.summary = null
      this.$router.push('/allEvaluation/index')
    }
  }
}
</script>
<style>
.el-scrollbar__wrap{
  overflow-x: hidden;
}
</style>

