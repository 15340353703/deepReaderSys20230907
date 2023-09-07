<template>
  <div>
    <el-scrollbar style="height: 100%">
      <el-row>
        <el-col v-if="this.active != 3" :span="6"> - </el-col>
        <el-col v-if="this.active != 3" :span="24">
          <el-card style="height: 520px; overflow: auto">
            <h3 style="text-align: center">文章信息</h3>

            <el-divider><i class="el-icon-mobile-phone" /></el-divider>

            <el-table :data="bookInfoList" border style="width: 100%" :row-style="{height: '50px'}">
              <el-table-column prop="name" :show-overflow-tooltip="true" sortable label="文章名称" width="180" />
              <el-table-column prop="author" :show-overflow-tooltip="true" label="作者" width="150" />
              <el-table-column prop="start_date" :formatter="dateFormat" sortable label="开始时间" width="130" />
              <el-table-column prop="end_date" :formatter="dateFormat" label="结束时间" width="130" />
              <!--              <el-table-column prop="pages" label="文章页数" width="180" />-->
              <el-table-column prop="reading_stage" :formatter="displayModeFormatter" label="阅读阶段" width="150" />
              <el-table-column prop="brief_introduction" :show-overflow-tooltip="true" v-html="bookInfoList.brief_introduction" label="文章简介" width="180">
                <template slot-scope="scope">
                  <span v-html="scope.row[scope.column.property]"></span>
                  <span v-html="scope.row[bookInfoList.brief_introduction]"></span>
                </template>
              </el-table-column>
              <!--              <el-table-column prop="url" label="文章链接" />-->
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button size="mini" type="primary" style="margin-right: 8px;margin-left: 15px" @click="editBookInfo(scope.row)">修改</el-button>
                  <el-popconfirm title="确认删除吗?" @onConfirm="deleteBook(scope.row.id)">
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
    <el-dialog :visible.sync="dialogFormVisible" title="修改文章信息" width="40%">
      <el-form v-if="active != 4" :model="bookInfo" label-width="120px">
        <el-form-item label="文章名称">
          <el-input v-model="bookInfo.name" clearable />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="bookInfo.author" clearable />
        </el-form-item>
        <el-form-item label="阅读阶段">
          <el-select v-model="bookInfo.reading_stage" placeholder="请选择阅读阶段">
            <el-option label="课前阅读" :value="0" />
            <el-option label="课后阅读" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="bookInfo.studyDate"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item label="文章字数">
          <el-input v-model="bookInfo.wordcount" />
        </el-form-item>

        <!--        <el-form-item label="文章简介">-->
        <!--          <el-input v-model="bookInfo.brief_introduction" type="textarea" clearable />-->
        <!--        </el-form-item>-->
        <el-form-item label="文章简介" size="small">
          <Editoritem v-model="bookInfo.brief_introduction" ></Editoritem>
        </el-form-item>
        <span class="dialog-footer">
          <el-button type="primary" size="mini" @click="updateBookInfo1(bookInfo)">确认修改</el-button>
          <el-button size="mini" @click="dialogFormVisible = false">取消</el-button>
        </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
// import moment from 'moment'
import Editoritem from '@/components/WangEditor/index'
export default {
  components: { Editoritem }, // 富文本编辑器组件
  name: 'ShowFileInfo',
  data() {
    return {
      active: 0,
      bookInfoList: [],
      bookInfo: {},
      dialogFormVisible: false
    }
  },
  created() {
    this.getBookInfoList()
  },
  mounted() {
  },
  methods: {
    ...mapActions('books', ['getBookInfoByClassifyId', 'updateBookInfo', 'deleteBookInfo']),
    getBookInfoList() {
      this.getBookInfoByClassifyId({}).then((response) => {
        this.bookInfoList = response
        console.log(response)
      })
    },
    displayModeFormatter(row) {
      const stage = row.reading_stage
      if (stage === 0) {
        return '课前阅读'
      } else {
        return '课后阅读'
      }
    },
    dateFormat(row, column) {
      const date = row[column.property]
      if (date == undefined) {
        return ''
      }
      // return moment(date).format('YYYY-MM-DD')
    },
    editBookInfo(row) {
      this.bookInfo = JSON.parse(JSON.stringify(row)) // 深拷贝，避免row发生变化时
      this.$set(this.bookInfo, 'studyDate', [row.start_date, row.end_date])
      this.dialogFormVisible = true
    },
    updateBookInfo1(bookFrom) {
      // this.$delete(bookFrom, 'start_date')
      // this.$delete(bookFrom, 'end_date')
      // this.$delete(bookFrom, 'classify_name')
      this.updateBookInfo(bookFrom).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '更新成功'
          })
          this.dialogFormVisible = false
          this.getBookInfoList()
          console.log('getBookInfoList')
        } else {
          this.$message({
            type: 'error',
            message: '失败'
          })
        }
      })
    },
    deleteBook(id) {
      console.log(id)
      this.deleteBookInfo(id).then((response) => {
        if (response) {
          this.$message({
            type: 'success',
            message: '删除成功'
          })
          this.dialogFormVisible = false
          this.getBookInfoList()
          console.log('getBookInfoList')
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

