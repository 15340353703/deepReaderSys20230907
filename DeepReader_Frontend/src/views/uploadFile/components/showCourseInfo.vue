<template>
  <div>
    <el-scrollbar style="height: 100%">
      <el-row>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
        <el-col v-if="this.active != 3" :span="24">
          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">课程信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-table :data="courseInfoList" border style="width: 100%">
              <el-table-column prop="id" label="课程id" width="180" v-show="false"/>
              <el-table-column prop="courseNum" label="课程码" width="180" />
              <el-table-column prop="courseName" label="课程名称" width="180" />
              <el-table-column prop="instructorName" label="授课教师" width="180" />
              <el-table-column prop="groupnameHP" label="hypothesis组号" width="180" />
              <el-table-column prop="courseContent" :show-overflow-tooltip="true" label="课程简介" width="180" />
              <el-table-column label="操作" width="240">
                <template #default="scope">
                  <el-button size="mini" type="primary" style="margin-right: 8px;margin-left: 15px" @click="editCourseInfo(scope.row)">修改</el-button>
                  <el-button size="mini" type="success" style="margin-right: 8px;margin-left: 15px" @click="downLoad(scope.row)">下载</el-button>
                  <el-popconfirm title="确认删除吗?" @onConfirm="deleteCourse(scope.row.id)">
                    <template #reference>
                      <el-button size="mini" type="danger">删除</el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>

          </el-card>
        </el-col>
      </el-row>
    </el-scrollbar>
    <el-dialog :visible.sync="dialogFormVisible" title="修改课程信息" width="40%">
      <el-form v-if="active != 4" :model="courseInfo" label-width="120px">
        <el-form-item label="课程码">
          <el-input v-model="courseInfo.courseNum" disabled />
          <el-input v-model="courseInfo.id" style="display:none" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="courseInfo.courseName" clearable />
        </el-form-item>
        <el-form-item label="授课教师">
          <el-input v-model="courseInfo.instructorName" clearable />
        </el-form-item>
        <el-form-item label="hypothesis组号">
          <el-input v-model="courseInfo.groupnameHP" clearable />
        </el-form-item>
        <el-form-item label="课程内容">
          <el-input v-model="courseInfo.courseContent" type="textarea" clearable />
        </el-form-item>
        <span class="dialog-footer">
          <el-button type="primary" size="mini" @click="updateCourse(courseInfo)">确认修改</el-button>
          <el-button size="mini" @click="dialogFormVisible = false">取消</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'ShowCourseInfo',
  data() {
    return {
      active: 0,
      courseInfoList: [],
      courseInfo: {},
      dialogFormVisible: false
    }
  },
  created() {
    this.getCourse()
  },
  methods: {
    ...mapActions('user', ['getCourseNum', 'updateCourseInfo', 'deleteCourseInfo']),
    ...mapActions('books', ['getCourseList']),
    getCourse() {
      this.getCourseList({}).then((response) => {
        this.courseInfoList = response
        console.log(response)
      })
    },
    editCourseInfo(row) {
      this.courseInfo = JSON.parse(JSON.stringify(row)) // 深拷贝，避免row发生变化时
      this.dialogFormVisible = true
    },
    updateCourse(courseInfo) {
      this.updateCourseInfo(courseInfo).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '更新成功'
          })
          this.dialogFormVisible = false
          this.getCourse()
          console.log('调用了getcourse')
        } else {
          this.$message({
            type: 'error',
            message: '失败'
          })
        }
      })
    },
    downLoad(row) {
      location.href = 'http://222.198.118.211:8081/dq/hypothesis/'+row.id
    },
    deleteCourse(id) {
      console.log(id)
      this.deleteCourseInfo(id).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.dialogFormVisible = false
          this.getCourse()
        } else {
          this.$message({
            type: 'error',
            message: '删除失败'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer{
  margin-left: 70%;
}

</style>

