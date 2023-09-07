<template>
  <div id="nav" style="height: 650px;">
      <el-table
        :data="tableData"
        style="width: 100%"
        max-height="600">
        <el-table-column
          prop="question"
          label="问题"
         >
        </el-table-column>

        <el-table-column
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click.native.prevent="$router.push('/questionEvaluation/index?questionId='+scope.$index)"
              type="text"
              size="small">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table><br>

    <el-footer style="text-align: center">
      <el-pagination
        background
        layout="sizes, prev, pager, next"
        :page-sizes="[12, 16, 20, 30]"
        :page-size="pageSize"
        :total="total"
        :current-page="currentPage"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-footer>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {

  data() {
    return {
      tableData: null,
      currentPage: 1,
      pageSize: 8,
      total: 0
    }
  },
  created() {
    this.getQuestions(this.currentPage,this.pagesize)
  },

  methods: {
    changeRate() {
      this.$forceUpdate();
    },
    ...mapActions('user',['getUserQuestions']),
    getQuestions(currentPage, pageSize) {
      this.getUserQuestions(currentPage,pageSize).then(res => {
        this.tableData = res.list
        this.total = res.total
        this.currentPage = res.pageNum
      })
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.getQuestions(this.currentPage, this.pageSize)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getQuestions(this.currentPage, this.pageSize)
    },
  }
}
</script>
<style>
.el-scrollbar__wrap{
  overflow-x: hidden;
}
</style>

