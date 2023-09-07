<template>
  <el-container>
    <el-main>
      <el-table :data="list" style="width: 100%">
        <el-table-column prop="courseNum" label="课程编号"/>
        <el-table-column prop="courseName" label="课程名称" />
        <el-table-column prop="havegroup" label="是否分组" />
        <el-table-column label="操作" width="400">
          <template #default="scope">
            <el-button size="mini" type="primary" plain @click="handleGroup(scope.row.id)" :disabled="scope.row.havegroup"><span v-if="scope.row.havegroup===1">已 分 组</span><span v-else>进行分组</span></el-button><span style="margin-right: 20px"></span>
            <el-button size="mini" type="success" plain @click="lookGroup(scope.row.id, scope.row.havegroup)">查看分组</el-button><span style="margin-right: 20px"></span>
            <el-popconfirm title="确定对新成员分组吗？" @onConfirm="newGroup(scope.row.id)">
              <el-button slot="reference" size="mini" type="danger">新成员分组</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
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
      active: false,
      text: '',
      value:''
    }
  },
  created() {
    this.getCourseList(this.currentPage, this.pageSize)
    // this.getGroup();
  },
  methods: {
    ...mapActions('classify', ['getCourse']),
    getCourseList(page, pageSize){
      this.getCourse({page, pageSize}).then(res => {
        console.log(res)
        this.list = res.list
        this.total = res.total
        this.currentPage = res.pageNum
        this.pageSize = res.pageSize
      })
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.getCourseList(this.currentPage, this.pageSize)
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.getCourseList(this.currentPage, this.pageSize)
    },
    handleGroup(id){
      this.$prompt('请输入每组人数', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        // inputPattern: /^0|[^\d]/g,
        // inputErrorMessage: '输入格式不正确'
      }).then(({ value }) => {
        this.value = value
        this.$router.push({path: '/groupstudent/index',query:{courseid: id, groupsize: value }})
        this.$message({
          type: 'success',
          message: '你的邮箱是: ' + value
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });

    },
    lookGroup(id, havegroup) {
      if(havegroup!==1){
        this.$message.warning('该课程还未分组，请进行分组后查看')
      } else {
        // this.getCourseList(this.currentPage, this.pageSize)
        this.$router.push({path: '/groupstudent/index',query:{courseid: id, groupsize: 4 }})
      }
    },
    ...mapActions('classify', ['setUserGroup']),
    newGroup(courseid){
      this.$prompt('请输入每组人数', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        // inputPattern: /^0|[^\d]/g,
        // inputErrorMessage: '输入格式不正确'
      }).then(({ value }) => {
        const groupsize = value
        this.setUserGroup({courseid,groupsize}).then(res => {
          this.$router.push({path: '/groupstudent/index', query:{courseid: courseid, groupsize: 4 }})
        })
        this.$message({
          type: 'success',
          message: '每组人数是: ' + value
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });

    }
  }

}
</script>

<style scoped>

</style>
