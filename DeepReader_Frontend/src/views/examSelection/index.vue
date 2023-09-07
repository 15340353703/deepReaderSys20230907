<template>
  <el-container>
    <el-main style="text-align: center">

      <div>
        <el-card>
        <el-radio :key="0"  v-model="radio" :label="0">全部</el-radio>
        <el-radio v-for="(item, id) in courseList" :key="id"  v-model="radio" :label="item.id">{{item.courseName}}</el-radio>
        </el-card>
      </div>

      <div>
        <el-col v-for="(item, id) in list" :key="id" :span="6">
          <el-col :span="20">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <img src="../../assets/knowledge.png" class="image">
              <div style="padding: 14px;">
                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">

                  <el-tooltip class="item" effect="dark" :content="item.name" placement="top">
                    <span>《{{ item.name }}》</span>
                  </el-tooltip>
                </div>
                <el-divider content-position="right">作者：{{ item.author }}</el-divider>

                <div class="bottom clearfix">
                  <span>阅读周期：{{ formatDate(item.start_date) }}到{{ formatDate(item.end_date) }}<br><br></span>
                  <span>所属课程：{{ item.classify_name }}<br><br></span>
                  <span>所属作业：{{ item.work }}<br><br></span>
                  <el-button v-if="!item.have_answer" size="mini" round type="warning" @click="startPractice(item.id)">开始练习</el-button>
                  <el-button type="success" v-if="item.have_answer" size="mini" round disabled @click="$router.push('/exam/index?bookId='+item.id)">已练习</el-button>&ensp;
                  <el-button v-if="item.have_answer" type="success" size="mini" round @click="lookPractice(item.id)">查看习题</el-button>
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
      list: [{
        id: 0,
        name: '',
        author: ''

      }],
      courseList: [{
        id: 0,
        courseNum: '',
        courseName: ''
      }],
      url: [
        'https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/QQ%E6%88%AA%E5%9B%BE20211011181319.png'
      ],
      total: 0,
      currentPage: 1,
      pageSize: 50,
      pages: null,
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
    lookPractice(id){
      const data = {userid: getToken(),behavior:'查看习题',bookid:id,stamptime:new Date()}
      service.post('/user/readlog',data).then(res=>{
        console.log('记录查看习题')
      })
      this.$router.push({path:'/allQuestion/index', query: {bookid: id}})
    },
    startPractice(id) {
      const data = {userid: getToken(),behavior:'开始练习',bookid:id,stamptime:new Date()}
      service.post('/user/readlog',data).then(res=>{
        console.log('记录开始练习')
      })
      this.$router.push({path:'/exam/index', query: {bookid: id}})
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

    getPhoto() {
      var num = this.randomNum(0, 1)
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
    randomNum(minNum, maxNum) {
      switch (arguments.length) {
        case 1:
          return parseInt(Math.random() * minNum + 1, 10)
          break
        case 2:
          return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
          break
        default:
          return 0
          break
      }
    },
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return dateUtils.formatDate(date, 'MM-dd')
    }
  }
}
</script>

<style>

.el-aside {
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
      clear: both
  }

  .el-card {
    margin-bottom: 20px;
  }
</style>
