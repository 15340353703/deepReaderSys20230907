<template>
  <div id="nav" style="height: 650px">
    <el-scrollbar style="height: 100%">
      <el-row>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
        <el-col v-if="this.active != 3" :span="12">
          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">填写作业信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-form
              ref="classForm"
              :model="classForm"
              label-width="120px"
            >
              <el-form-item label="所属课程">
                <el-select
                  v-model="classForm.courseId"
                  placeholder="请选择所属课程"
                >
                  <el-option
                    v-for="item in courseForm"
                    :key="item.id"
                    :label="item.courseName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="作业名称">
                <el-input v-model="classForm.classifyName" />
              </el-form-item>
              <el-form-item label="阅读周期">
                <el-date-picker
                  v-model="classForm.studyDate"
                  value-format="yyyy-MM-dd"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                />
              </el-form-item>

              <el-form-item label="模式选择">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="KWL表示只使用KWL表格进行学习，KWL+Q表示学生在KWL表格的基础上还可以进行深度提问"
                  placement="top"
                >
                  <i class="el-icon-question" />
                </el-tooltip>
                &nbsp;
                <el-radio-group v-model="classForm.displayMode">
                  <el-radio label="0">KWL</el-radio>
                  <el-radio label="1">KWL+Q</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
      </el-row>

      <el-divider v-if="this.active != 3" />
      <div v-if="this.active != 3" style="text-align: center">
        <el-button size="medium" type="success" @click="submitForm"
          >创建作业</el-button
        >

        <el-button size="medium" type="warning" @click="resetForm('classForm')"
          >重置</el-button
        >
      </div>

      <div v-if="this.active == 3">
        <el-card class="box-card" style="text-align: center">
          <el-button type="success" icon="el-icon-check" circle />
          <div class="text item">
            <br />
            创建成功
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
import { mapState, mapActions } from "vuex";
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      classForm: {
        courseId: "",
        classifyName: "",
        displayMode: "",
      },
      courseForm: [
        {
          id: 0,
          courseNum: "",
          courseName: "",
        },
      ],
      content: "",
      dialogFormVisible: false,
      percentageNum: 0,
      choiceData: [],
      active: 0,
    };
  },
  computed: {},
  created() {
    this.getCourseForm();
  },
  methods: {
    ...mapActions("books", ["getCourseList", "updateClassify"]),
    getCourseForm() {
      this.getCourseList().then((response) => {
        console.log('response:'+response)
        this.courseForm = response;
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    end() {
      this.$confirm("是否确定创建作业?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.active = 3;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消创建",
          });
        });
    },
    submitForm() {
      if (this.classForm.courseId == null || this.classForm.courseId == "") {
        this.$alert("请选择所属课程", "创建失败", {
          confirmButtonText: "确定",
        });
      } else if (
        this.classForm.classifyName == null ||
        this.classForm.classifyName == ""
      ) {
        this.$alert("作业名称不可为空", "创建失败", {
          confirmButtonText: "确定",
        });
      } else if (
        this.classForm.displayMode == null ||
        this.classForm.displayMode == ""
      ) {
        this.$alert("请选择作业模式", "创建失败", {
          confirmButtonText: "确定",
        });
      } else {
        console.log(this.classForm);
        this.updateClassify(this.classForm);
        this.active = 3;
        //this.uploadBook(book);
        //this.active = 3;
      }
    },
  },
};
</script>
<style>
.el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>

