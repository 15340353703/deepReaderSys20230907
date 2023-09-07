<template>
  <div>
    <div>
      <el-container>
        <el-main style="text-align: center">
          <el-card>
            <el-row :gutter="20">
              <el-col :span="8">
                <el-card shadow="hover">
                  <el-image
                    :src="require('@/assets/class.jpg')"
                    style="height: 200px; width: 100%"
                    :fit="this.fit"
                  ></el-image>
                  <el-divider></el-divider>
                  <div style="height: 50px">
                    学生随机分组
                  </div>
                  <el-divider></el-divider>
                  <div>
                    <el-button
                      type="primary"
                      @click="$router.push('/group/index')"
                      >学生分组</el-button
                    >
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover">
                  <el-image
                    :src="require('@/assets/class.jpg')"
                    style="height: 200px; width: 100%"
                    :fit="this.fit"
                  ></el-image>
                  <el-divider></el-divider>
                  <div style="height: 50px">
                    创建课程，保存课程基本信息并生成课程码。
                  </div>
                  <el-divider></el-divider>
<!--                  <div>-->
<!--                    <el-button-->
<!--                      type="primary"-->
<!--                      @click="$router.push('/uploadFile/index?cid=2')"-->
<!--                    >创建课程</el-button-->
<!--                    >-->
<!--                    <el-button type="success" @click="this.courseNumShow"-->
<!--                    >查询课程码</el-button-->
<!--                    >-->
<!--                  </div>-->
                  <div>
                    <el-button
                      type="primary"
                      @click="$router.push('/uploadFile/index?cid=2')"
                    >创建课程</el-button>
                    <el-button
                      type="success"
                      @click="this.courseNumShow"
                    >查询课程码</el-button>
                    <el-button
                      type="success"
                      @click="$router.push('/uploadFile/index?cid=3')"
                    >查看课程</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover">
                  <el-image
                    :src="require('@/assets/homework.jpg')"
                    style="height: 200px; width: 100%"
                    :fit="this.fit"
                  ></el-image>
                  <el-divider></el-divider>
                  <div style="height: 50px">
                    创建作业，为指定课程创建相关作业并保存作业信息。
                  </div>
                  <el-divider></el-divider>
<!--                  <div>-->
<!--                    <el-button-->
<!--                      type="primary"-->
<!--                      @click="$router.push('/uploadFile/index?cid=1')"-->
<!--                      >创建作业</el-button-->
<!--                    >-->
<!--                  </div>-->
                  <div>
                    <el-button
                      type="primary"
                      @click="$router.push('/uploadFile/index?cid=1')"
                    >创建作业</el-button>
                    <el-button
                      type="success"
                      @click="$router.push('/uploadFile/index?cid=4')"
                    >查询作业</el-button>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover">
                  <el-image
                    :src="require('@/assets/article.jpg')"
                    style="height: 200px; width: 100%"
                    :fit="this.fit"
                  ></el-image>
                  <el-divider></el-divider>
                  <div style="height: 50px">
                    上传文章，为指定作业上传文章并上传文章基本信息。
                  </div>
                  <el-divider></el-divider>
<!--                  <div>-->
<!--                    <el-button-->
<!--                      type="primary"-->
<!--                      @click="$router.push('/uploadFile/index?cid=0')"-->
<!--                      >上传文章</el-button-->
<!--                    >-->
<!--                  </div>-->
                  <div>
                    <el-button
                      type="primary"
                      @click="$router.push('/uploadFile/index?cid=0')"
                    >上传文章</el-button>
                    <el-button
                      type="success"
                      @click="$router.push('/uploadFile/index?cid=5')"
                    >
                      查看文章</el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </el-main>
      </el-container>
    </div>
    <div >
      <el-dialog title="课程码" :visible.sync="dialogFormVisible" >
        <el-row style="height: 500px; overflow: auto">
          <el-col :span="24">
            <el-table :data="courseList" stripe style="width: 100%">
              <el-table-column prop="instructorName" label="授课教师" width="180">
              </el-table-column>
              <el-table-column prop="courseName" label="课程名称" width="180">
              </el-table-column>
              <el-table-column prop="courseNum" label="课程码"> </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  data() {
    return {
      fit: "cover",
      courseList: [
        {
          id: 0,
          instructorName: "",
          courseNum: "",
          courseName: "",
        },
      ],
      dialogFormVisible: false,
    };
  },
  created() {
    this.getCourse();
  },
  methods: {
    ...mapActions("user", ["getCourseNum"]),
    courseNumShow() {
      this.dialogFormVisible = true;
    },
    getCourse() {
      this.getCourseNum({}).then((response) => {
        this.courseList = response;
        console.log(this.courseList);
      });
    },
  },
};
</script>

<style>
</style>
