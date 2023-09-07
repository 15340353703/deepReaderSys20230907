<template>
  <div>
    <el-scrollbar style="height: 100%">
      <el-row>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
        <el-col v-if="this.active != 3" :span="24">
          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">作业信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-table :data="classifyInfo" border style="width: 100%;margin-left: 5%">
              <el-table-column prop="name" label="课程名称" width="200" class="courseName" />
              <el-table-column prop="work" label="作业名称" width="200" />
              <el-table-column prop="displayMode" :formatter="displayModeFormatter" label="阅读模式" width="150" />
              <el-table-column prop="start_date" :formatter="dateFormat" sortable label="开始时间" width="150" />
              <el-table-column prop="end_date" :formatter="dateFormat" label="结束时间" width="150" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="mini" type="primary" style="margin-right: 8px;margin-left: 15px" @click="editClassifyInfo(scope.row)">修改</el-button>
                  <el-popconfirm title="确认删除吗?" @onConfirm="deleteClassify(scope.row.id)">
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
    <el-dialog :visible.sync="dialogFormVisible" title="修改作业信息" width="40%">
      <el-form v-if="active != 4" :model="classifyInfo" label-width="120px">
        <el-form-item label="课程名称">
          <el-input v-model="classifyInfo.name" disabled />
        </el-form-item>
        <el-form-item label="作业名称">
          <el-input v-model="classifyInfo.work" clearable />
        </el-form-item>
        <el-form-item label="阅读周期">
          <el-date-picker
            v-model="classifyInfo.studyDate"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item label="阅读模式">
          <el-tooltip
            class="item"
            effect="dark"
            content="KWL表示只使用KWL表格进行学习，KWL+Q表示学生在KWL表格的基础上还可以进行深度提问"
            placement="top"
          >
            <i class="el-icon-question" />
          </el-tooltip>
          &nbsp;
          <el-radio-group v-model="classifyInfo.displayMode">
            <el-radio :label="0">KWL</el-radio>
            <el-radio :label="1">KWL+Q</el-radio>
          </el-radio-group>
        </el-form-item>
        <span class="dialog-footer">
          <el-button type="primary" size="mini" @click="updateClassifyInfo1(classifyInfo)">确认修改</el-button>
          <el-button size="mini" @click="dialogFormVisible = false">取消</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>

</template>

<script>
import { mapActions } from 'vuex'
// import moment from 'moment'

export default {
  name: 'ShowWorkInfo',
  data() {
    return {
      // classifyInfoList: [],
      classifyInfo: [],
      dialogFormVisible: false
    }
  },
  created() {
    this.getClassify()
  },
  methods: {
    ...mapActions('user', ['getClassifyInfo', 'updateClassifyInfo', 'deleteClassifyInfo']),
    getClassify() {
      this.getClassifyInfo({}).then((response) => {
        this.classifyInfo = response
        console.log(this.classifyInfo)
      })
    },
    displayModeFormatter(row) {
      const displayMode = row.displayMode
      if (displayMode === 0) {
        return 'KWL'
      } else {
        return 'KWL+Q'
      }
    },
    dateFormat(row, column) {
      const date = row[column.property]
      if (date == undefined) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD')
    },
    editClassifyInfo(row) {
      this.classifyInfo = JSON.parse(JSON.stringify(row)) // 深拷贝，避免row发生变化时
      this.$set(this.classifyInfo, 'studyDate', [row.start_date, row.end_date])
      this.dialogFormVisible = true
    },
    updateClassifyInfo1(classifyInfo) {
      this.updateClassifyInfo(classifyInfo).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '更新成功'
          })
          this.dialogFormVisible = false
          this.getClassify()
          console.log('调用了getClassify')
        } else {
          this.$message({
            type: 'error',
            message: '失败'
          })
        }
      })
    },
    deleteClassify(id) {
      console.log(id)
      this.deleteClassifyInfo(id).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.dialogFormVisible = false
          this.getClassify()
          console.log('调用了getcourse')
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

