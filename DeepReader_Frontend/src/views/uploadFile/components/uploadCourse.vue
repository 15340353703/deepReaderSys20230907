<template>
  <div id="nav" style="height: 650px">
    <el-scrollbar style="height: 100%">
      <el-row>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
        <el-col v-if="this.active != 3" :span="12">
          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">填写课程信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-form
              ref="courseForm"
              :model="this.courseForm"
              label-width="120px"
            >

              <el-form-item label="课程名称">
                <el-input v-model="courseForm.name" />
              </el-form-item>

              <el-form-item label="授课教师">
                <el-input v-model="courseForm.instructorName" />
              </el-form-item>

              <el-form-item label="课程简介">
                <el-input type="textarea" v-model="courseForm.courseContent" />
              </el-form-item>
              <el-form-item label="hypothesis组号">
                <el-input v-model="courseForm.groupnameHP" />
              </el-form-item>

            </el-form>
          </el-card>
        </el-col>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
      </el-row>

      <el-divider v-if="this.active != 3" />
      <div v-if="this.active != 3" style="text-align: center">
        <el-button size="medium" type="success" @click="submitForm"
          >创建课程</el-button
        >

        <el-button size="medium" type="warning" @click="resetForm('courseForm')"
          >重置</el-button
        >
      </div>

      <div v-if="this.active == 3">
        <el-card class="box-card" style="text-align: center">
          <el-button type="success" icon="el-icon-check" circle />
          <div class="text item">
            <br />
            创建成功，课程码为：{{this.courseNum}}
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
      courseForm: {
        id: "",
        name: "",
        instructorName: "",
        courseContent: "",
        groupnameHP:''
      },
      courseNum: "",
      content: "",
      dialogFormVisible: false,
      percentageNum: 0,
      choiceData: [],
      active: 0,
    };
  },
  computed: {},
  created() {
    // this.getCourse();
  },
  methods: {
    ...mapActions("books", ['updateCourse']),
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    submitForm() {
      if (this.courseForm.name == null || this.courseForm.name == "") {
        this.$alert("请输入课程名称", "创建失败", {
          confirmButtonText: "确定",
        });
      }else {
        console.log(this.courseForm);
        //this.updateClassify(this.courseForm);

        this.updateCourse(this.courseForm).then((response) => {
          //console.log(response)
          this.courseNum=response
          console.log(this.courseNum)
          this.active=3;
        });
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

