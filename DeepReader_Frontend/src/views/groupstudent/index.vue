<template>
  <el-container>
    <el-main>
      <el-table :data="list" style="width: 100%">
        <el-table-column prop="groupname" label="组名"/>
        <el-table-column prop="username" label="学号" />
        <el-table-column prop="name" label="姓名" />
      </el-table>
      <el-footer style="text-align: center">
        <el-pagination
          background
          layout="sizes, prev, pager, next"
          :page-sizes="[5,10,15]"
          :page-size="pageSize"
          :total="total"
          :current-page="currentPage"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </el-footer>
    </el-main>
  </el-container>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      list: null,
      currentPage: 1,
      pageSize: 8,
      total: 0,
      pages: null,
      courseid:this.$route.query.courseid,
      groupsize: this.$route.query.groupsize
    }
  },
  created() {
    const courseid = this.courseid
    const groupsize = this.groupsize
    this.getUserGroup(this.currentPage,this.pageSize, courseid, groupsize)
  },
  methods: {
    ...mapActions('classify', ['getUserGroupList']),
    getUserGroup(page, pageSize, courseid, groupsize){
      this.getUserGroupList({page, pageSize, courseid, groupsize}).then(res => {
        console.log('res:'+res.list)
        this.list = res.list
        this.total = res.total
        this.currentPage = res.pageNum
        this.pageSize = res.pageSize
      })
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.getUserGroup(this.currentPage,this.pageSize, this.courseid)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getUserGroup(this.currentPage,this.pageSize, this.courseid)
    },
  }

}
</script>

<style scoped>

</style>
