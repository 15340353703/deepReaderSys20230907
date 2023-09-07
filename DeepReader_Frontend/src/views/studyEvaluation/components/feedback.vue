<template>
  <div id="nav" style="height: 720px;">
    <el-scrollbar v-if="!showContent" style="height:100%">

      <el-card v-if="active == 0" class="box-card">
        <ul class="text item" style="text-align: left">
          <li>请根据阅读反馈报告，结合高亮的关键信息，创建多条总结句。</li>
          <li>总结句用凝练话语概括一篇文章或者一个段落的中心思想，通过对于内容的提炼和用自己的语言复述等过程，可以使阅读者加深对于阅读内容的理解，加强重要信息的长时间记忆。</li>
          <li>总结句应该流程、易读，避免笼统、强调明确，最好在20-30字之内，包含文章主旨关键词。</li>

        </ul>
      </el-card>

      <el-divider><i class="el-icon-notebook-2" /></el-divider>

      <div style="text-align: center">
        <el-form
          v-if="active == 0"
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
            v-if="active == 0"
            size="medium"
            type="success"
            @click="addSummary"
            icon="el-icon-plus" circle
          ></el-button>
        </el-form>
      </div>
      <br>
      <div style="text-align: center">

        <el-button v-if="active != 3 && active != 4" size="medium" type="info" @click="back(book.id)">上一步</el-button>
        <!--<el-button @click="back" size="medium" type="warning" v-if="active == 2">返回上一步</el-button>-->
        <el-button
          v-if="active == 0"
          size="medium"
          type="warning"
          @click="resetForm('dynamicValidateForm')"
        >重置</el-button>
        <el-button v-if="active != 3 && active != 4" size="medium" type="primary" @click="nextstep(book.id)">下一步</el-button>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: ''
        }
      }
    }
  },
  data() {
    return {
      fit: "scale-down",
      book:null,
      haveSubmit: false,
      showContent: false,
      url: 'https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/src=http___img.zcool.cn_community_01dcd75af39d3ba8012160456172c4.jpg@1280w_1l_2o_100sh.jpg&refer=http___img.zcool.jpg',
      choiceData: [],
      active: 0,
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
      timeData: '',
      timer: 0
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  computed: {
    ...mapState('user', ['name'])
  },
  created() {
    const id = this.$route.query.bookid
    this.getBook(id)
  },
  methods: {
    ...mapActions("books", ["getBookInfo"]),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response;
      });
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
      this.$store.state.summary = this.dynamicValidateForm.summary
      this.$router.push({path:'/askQuestion/index', query:{bookid: id}})
    },
    back(id) {
      this.$router.push({path:'/studyQuestion/index', query:{bookid: id}})
    }
  }
}
</script>
<style>
.el-scrollbar__wrap{
  overflow-x: hidden;
}
</style>

