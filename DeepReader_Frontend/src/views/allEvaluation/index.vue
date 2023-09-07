<template>
  <el-container>

    <el-main style="text-align: center">
      <div>
        <el-card>
        <el-radio :key="0"  v-model="radio" :label="0">全部</el-radio>
        <el-radio v-for="(item, id) in this.courseList" :key="id"  v-model="radio" :label="item.id">{{item.courseName}}</el-radio>
        </el-card>
      </div>

      <div>
        <el-col v-for="(item, id) in this.list" :key="id" :span="6">
          <el-col :span="20">
            <el-card :body-style="{ padding: '0px' }" shadow="hover" v-if="item.have_read">
              <img
                src="../../assets/knowledge.png"
                class="image"
              >
              <div style="padding: 14px">
                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                  <el-tooltip class="item" effect="dark" :content="item.name" placement="top">
                    <span>《{{ item.name }}》</span>
                  </el-tooltip>
                </div>
                <el-divider
                  content-position="right"
                >作者：{{ item.author }}</el-divider>

                <div class="bottom clearfix">
<!--                  <span> 阅读周期：{{ formatDate(item.start_date) }}到{{ formatDate(item.end_date) }}<br><br></span>-->
<!--                  <span>所属课程：{{ item.classify_name }}<br><br></span>-->
<!--                  <span><el-tag>{{ returnStage(item.reading_stage) }}</el-tag>-->
<!--                    <el-tag v-if="item.have_read" type="success">已阅读</el-tag>-->
<!--                    <el-tag v-if="!item.have_read" type="danger">未阅读</el-tag>-->
<!--                    <br><br>-->
<!--                  </span>-->
<!--                  <el-button-->
<!--                  size="mini" round-->
<!--                    v-if="!item.have_read"-->
<!--                    type="warning"-->
<!--                    @click="$router.push('/study/index?bookId='+item.id)"-->
<!--                  >开始阅读</el-button>&ensp;-->
<!--                  <el-button-->
<!--                  size="mini" round-->
<!--                    v-if="item.have_read"-->
<!--                    type="success"-->
<!--                    @click="viewBookAndNum(item.id)"-->
<!--                  >查看文章</el-button>&ensp;-->
                  <el-button
                  size="mini" round
                    v-if="item.have_read"
                    type="success"
                    @click="startDeepQuestion(item.id)"
                  >开始深度提问</el-button>
<!--                   <el-button-->
<!--                  size="mini" round-->
<!--                    v-if="item.have_read"-->
<!--                    type="success"-->
<!--                    @click="$router.push('/studyEvaluation/index?bookId='+item.id)"-->
<!--                  >阅读反馈报告</el-button>-->
                </div>
              </div>
            </el-card>
          </el-col>
        </el-col>
      </div>
    </el-main>
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
  </el-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import * as dateUtils from '../../utils/date'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
export default {
  data() {
    return {
      radio: 0,
      list: [
        {
          id: 0,
          name: '',
          author: ''
        }
      ],
      courseList: [{
        id: 0,
        courseNum: '',
        courseName: ''
      }],
      url: [
        'https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/QQ%E6%88%AA%E5%9B%BE20211011181319.png',
        'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.51yuansu.com%2Fpic3%2Fcover%2F02%2F45%2F36%2F59e52fc345cdb_610.jpg',
        'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.kaoersi.com%2Fuploads%2Fueditor%2F20200430%2F3cfd390b7c5bb9add0c44751c9b2a4c4.jpg'
      ],
      total: 0,
      currentPage: 1,
      pageSize: 50,
      listLoading: true,
      currentDate: new Date()
    }
  },
  watch: {
    radio: function(newVal,oldVal){
      console.log("newVal: "+newVal+", oldVal: "+oldVal)
      if(newVal==0){
        this.getAllBookList(this.currentPage,this.pageSize)
      }else{
        this.getBookListByCourse(1,this.pageSize,newVal)
      }
    }
  },
  computed: {
    ...mapState('books', ['title'])
  },
  created() {
    this.getAllBookList(this.currentPage, this.pageSize)
    this.getCourse()
  },
  methods: {
    startDeepQuestion(id){
      const data = {userid: getToken(),behavior:'开始深度提问',bookid:id,stamptime:new Date()}
      service.post('/user/readlog',data).then(res=>{
        console.log('记录开始深度提问')
      })
      this.$router.push({path:'/studyQuestion/index', query:{bookid: id}})
    },
    ...mapActions('books', ['getUserBookList', 'getUserBookListByCourse']),
    ...mapActions('user', ['viewBook', 'getUserCourse']),
    getAllBookList(page, pageSize) {
      console.log('test: ' + page + ' : ' + pageSize)
      this.listLoading = true
      this.getUserBookList({ page, pageSize }).then((response) => {
        // console.log(response)
        this.list = response.list
        this.total = response.total
        this.currentPage = response.pageNum
        this.listLoading = false
      })
    },

    getCourse() {
      this.getUserCourse({}).then((response) => {
        this.courseList = response
        console.log(this.courseList)
      })
    },

    getBookListByCourse(page, pageSize, courseId) {
      console.log('test: ' + page + ' : ' + pageSize)
      this.listLoading = true
      this.getUserBookListByCourse({ page, pageSize, courseId }).then((response) => {
        // console.log(response)
        this.list = response.list
        this.total = response.total
        this.currentPage = response.pageNum
        this.listLoading = false
      })
    },

    viewBookAndNum(id) {
      this.viewBook(id)
      this.$router.push('/studyLook/index?bookId=' + id)
    },
    getPhoto() {
      var num = this.randomNum(this.url.length)
      return this.url[num]
    },
    handleCurrentChange(current) {
      this.data = []
      this.currentPage = current
      this.getAllBookList(this.currentPage, this.pageSize)
    },
    handleSizeChange(size) {
      this.data = []
      this.pageSize = size
      this.getAllBookList(this.currentPage, this.pageSize)
    },
    randomNum(maxNum) {
      Math.round(Math.random() * maxNum)
    },
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return dateUtils.formatDate(date, 'yyyy-MM')
    },
    returnStage(stageId) {
      if (stageId == '0') {
        return '课前阅读'
      } else {
        return '课后阅读'
      }
    },
    returnHaveRead(have_read) {
      if (have_read) {
        return '已阅读'
      } else {
        return '未阅读'
      }
    },
    gotoBookPage(page) {
      alert(page)
    },
    getRadio(){
      console.log(this.radio)
    }
  }
}
</script>

<style>
.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.el-card {
  margin-bottom: 20px;
}
</style>
