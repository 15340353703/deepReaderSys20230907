<template>
  <div id="nav" style="height: 750px;">
    <el-scrollbar style="height:100%">
      <el-row>
        <el-col :span="11">
          <div v-if="this.active!=3">
            <el-card style="height: 650px; overflow: auto">
              <h3 style="text-align: center">上传文章文本</h3>
              <el-divider><i class="el-icon-mobile-phone" /></el-divider>
<!--                <ele-form-->
<!--                  v-model="articlecontent"-->
<!--                  :form-desc="formDesc"-->
<!--                  :span="25"-->
<!--                  :isShowBackBtn='false'-->
<!--                  :isShowCancelBtn='false'-->
<!--                  :isShowSubmitBtn='false'-->
<!--                />-->
              <Editor ref="editor" initialFrameHeight='500' :model="articlecontent" :content="articlecontent" />
              <el-button @click="getContent()"></el-button>
<!--              <ele-form v-model="articlecontent" :content="articlecontent" :options="editorOption" @blur="onEditorBlur($event)" @focus="onEditorFocus($event)" @ready="onEditorReady($event)" ref="QuillEditor"></ele-form>-->
            </el-card>
          </div>
        </el-col>
        <el-col v-if="this.active!=3" :span="2">
          <div style="height: 520px; overflow: auto" />
        </el-col>
        <el-col v-if="this.active!=3" :span="11">

          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">填写文章信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-form ref="bookForm" :model="this.bookForm" label-width="120px">
              <el-form-item label="文章名称">
                <el-input v-model="bookForm.bookName" />
              </el-form-item>

              <el-form-item label="文章作者">
                <el-input v-model="bookForm.author" />
              </el-form-item>

              <el-form-item label="文章简介">
                <el-input v-model="bookForm.brief_introduction" type="textarea" />
              </el-form-item>


              <el-form-item label="阅读周期">
                <el-date-picker
                  v-model="bookForm.studyDate"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                />
              </el-form-item>

              <el-form-item label="所属课程及作业">
                <el-cascader
                  placeholder="请选择课程及作业"
                  v-model="bookForm.classifyId"
                  :options="bookForm.classifyList"
                  @change="handleChange">
                </el-cascader>
              </el-form-item>

              <el-form-item label="阅读阶段">
                <el-select v-model="bookForm.stage" placeholder="请选择阅读阶段">
                  <el-option label="课前阅读" value="0" />
                  <el-option label="课后阅读" value="1" />
                </el-select>
              </el-form-item>

              <el-form-item label="文章字数">
                <el-input v-model="bookForm.wordcount" />
              </el-form-item>

            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <el-divider v-if="this.active!=3" />
      <div v-if="this.active!=3" style="text-align: center">
        <el-button size="medium" type="success" @click="submitForm">上传文章</el-button>

        <el-button
          size="medium"
          type="warning"
          @click="resetForm('bookForm')"
        >重置</el-button>
      </div>

      <div v-if="this.active==3">
        <el-card class="box-card" style="text-align: center">
          <el-button type="success" icon="el-icon-check" circle />
          <div class="text item">
            <br>
            提交成功
          </div>
        </el-card>
        <div style="text-align: center; margin: 15px">
          <el-button type="success" @click="$router.push('/index')"
            >返回主页</el-button
          >
          <el-button type="success" @click="$router.push('/createCourse/index')"
            >继续选择</el-button
          >
        </div>
      </div>

    </el-scrollbar>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Editor from './editor';

export default {
  components: {
    Editor
  },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
        }
      }
    }
  },
  data() {
    return {
      editor: '',
      isChange: false,
      actionUrl: process.env.VUE_APP_BASE_API + '/file/upload',
      bookForm: {
        bookName: '',
        author: '',
        brief_introduction: '',
        content: '',
        studyDate: '',
        stage: '',
        classifyId: 0,
        classifyList: [{
          label: '',
          children:[{
            value: 0,
            label: '',
          }]
        }],
        url: '',
        wordcount:''
      },
      articlecontent: '',
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      dialogFormVisible: false,
      percentageNum: 0,
      choiceData: [],
      active: 0,
      questionNum: 0,
      question: '',
      dynamicValidateForm: {
        domains: [
          {
            value: '请写出文章的中心主旨，并对文章主题进行总结',
            result: ''
          }
        ],
        summary: {
          content: '',
          questionDescribe: '',
          questionClues: '',
          questionAnswer: '',
          rate: 0,
          damainList: [],
        },
      },
      transferProps: {
        key: 'key',
        label: 'value'
      },
      customColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 40 },
        { color: '#5cb87a', percentage: 60 },
        { color: '#1989fa', percentage: 80 },
        { color: '#6f7ad3', percentage: 100 }
      ],
    }
  },
  computed: {
  },
  // watch: {
  //   remark(val) {
  //     if (this.editor !== '') {
  //       this.editor.clear();
  //       this.editor.txt.html(val)
  //     }
  //   }
  // },
  mounted() {
    console.log(this.articlecontent)
  },
  created() {
    this.getClassifyList()
    // this.initText()
  },
  methods: {
    handleRequest(data) {
      console.log(data)
      return Promise.resolve()
    },
    handleSuccess() {
      this.$message.success('提交成功')
    },
    ...mapActions('books', ['getClassify','uploadBook']),
    getClassifyList(){
      this.getClassify().then(response => {
        this.bookForm.classifyList = response
      })
    },
    next() {
      if (this.active == 0) {
        this.dynamicValidateForm.domains[0].value = '请写出故事新编中描写的几处核心情节',
        this.dynamicValidateForm.domains[0].result = ''
        this.active = 1
      } else if (this.active == 1) {
        this.dynamicValidateForm.domains[0].value = '请写出文章中提到的几处核心论点并给出自己的想法',
        this.dynamicValidateForm.domains[0].result = ''
        this.active = 0
      }

      if (this.percentageNum < 100) {
        this.percentageNum += 10
      } else {
        this.percentageNum = 10
      }
    },
    // initText() {
    //    this.$nextTick(() => {
    //     // 关联弹窗里面的div，new一个 editor对象
    //    if (!this.editor) {
    //    this.editor = new E('#div1')
    //      // 配置 server 接口地址
    //      this.editor.config.uploadImgServer = 'http://222.198.118.211:9090/files/editor/upload'
    //      this.editor.config.uploadFileName = "file"
    //      this.editor.create()
    //    }
    //      if (this.$refs['upload']) {
    //        this.$refs['upload'].clearFiles()
    //      }
    //      this.editor.config.zIndex = 200
    //   })
    //  },
    submitForm() {
      if(this.bookForm.bookName==null || this.bookForm.bookName=='' ){
        this.$alert('文章题目不可为空', '上传失败', {
          confirmButtonText: '确定'
        })
      }else if(this.bookForm.author==null || this.bookForm.author==''){
        this.$alert('文章作者不可为空', '上传失败', {
          confirmButtonText: '确定'
        })
      }else if(this.bookForm.studyDate==null || this.bookForm.studyDate==''){
        this.$alert('请确定文章阅读时间', '上传失败', {
          confirmButtonText: '确定'
        })
      }else if(this.bookForm.stage==null || this.bookForm.stage==''){
        this.$alert('请确定文章阅读阶段', '上传失败', {
          confirmButtonText: '确定'
        })
      }else if(this.bookForm.classifyId==null || this.bookForm.classifyId==0){
        this.$alert('请选择文章所属作业', '上传失败', {
          confirmButtonText: '确定'
        })
      }else{
        console.log('this.articlecontent.content:'+this.articlecontent.content)
        console.log('this.articlecontent'+this.articlecontent)
        this.bookForm.content = this.$refs.editor.getUEContent()
        console.log(this.bookForm)
        var book=this.bookForm
        book.classifyList=null
        this.uploadBook(book)
        this.active = 3
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    failNextAtFirstForm() {
      this.$alert('提问不可以为空，请填写您的问题或删除部分空白问题', '跳转失败', {
        confirmButtonText: '确定'
      })
    },
    failNextAtSecondForm() {
      this.$alert('请回答您所提出的问题', '跳转失败', {
        confirmButtonText: '确定'
      })
    },
    end() {
      this.$confirm('是否确定上传文章?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.active = 3
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消提交'
        })
      })
    },
    handleChange(val) {
      console.log(val)

      this.bookForm.classifyId=val[1]
    },
    openDialog() {
      this.dialogFormVisible = true
    },
  }
}
</script>
<style>
.el-scrollbar__wrap{
  overflow-x: hidden;
}
#content {
  width: 100%;
  display: block;
  word-break: break-all;
  word-wrap: break-word;
}
</style>

