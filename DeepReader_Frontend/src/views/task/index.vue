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
        <el-col v-for="(item, id) in this.list" :key="id" :span="12">
          <el-col :span="20">
            <el-card shadow="hover">
              <h3>{{item.work}}</h3>
              <div>
                <h3>所属课程：{{item.name}}</h3>
              </div>
              <el-divider></el-divider>
              <div style="text-align: left">
                <div><span style="font-size: large">阅读周期：</span>{{ formatDate(item.start_date) }}至{{ formatDate(item.end_date)}}</div>
                <div><span style="font-size: large">阅读文章：</span></div>
                <div v-for="(item2,id) in item.children" :key="id">
                  <div>《{{item2.name}}》</div>
                </div>
                <div><span style="font-size: large">创建测试题：</span>灵活运用机器人提问，每篇文章提出3个以上问题。</div>
                <div><span style="font-size: large">他问我答：</span>在{{item.end_date}}之前完成他问我答部分</div>
                <div><span style="font-size: large">参与讨论：</span>在讨论区参与讨论。</div>
              </div>

            </el-card>
          </el-col>
        </el-col>

<!--        <el-col v-for="(item, id) in this.list" :key="id" :span="6">-->
<!--          <el-col :span="20">-->
<!--            <el-card :body-style="{ padding: '0px' }" shadow="hover">-->
<!--              <h2>{{item.work}}</h2>-->
<!--              <el-divider></el-divider>-->
<!--              <div style="padding: 14px">-->
<!--                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">-->
<!--                    <span>阅读周期：{{ item.start_date }}至{{item.end_date}}</span>-->
<!--                </div>-->
<!--                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">-->
<!--                  <span>阅读文章：</span>-->
<!--                  <span v-for="item2 in item.children">-->
<!--                    {{item2.name}}-->
<!--                  </span>-->
<!--                </div>-->
<!--                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">-->
<!--                  <span>创建测试题：根据对文章内容的理解，借助小机器人向同伴提问，每篇文章至少提3个问题</span>-->
<!--                </div>-->
<!--                <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">-->
<!--                  <span>他问我答：在{{item.end_date}}之前完成他问我答部分</span>-->
<!--                </div>-->

<!--              </div>-->
<!--            </el-card>-->
<!--          </el-col>-->
<!--        </el-col>-->
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
import service from '@/utils/request'
import { getToken } from '@/utils/auth'
export default {
  data() {
    return {
      radio: 0,
      list: [
        {
          id: 0,
          name: '',
          // author: ''
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
        this.gettaskAllList()
      }else{
        this.gettaskAllListBycourse(newVal)
      }
    }
  },
  computed: {
    ...mapState('books', ['title'])
  },
  created() {
    this.getAllBookList(this.currentPage, this.pageSize)
    this.getCourse()
    this.gettaskAllList()
    this.ViewTask(1000517)
  },
  methods: {
    ViewTask(id){
      const data = {userid: getToken(),behavior:'查看阅读任务',bookid:id,stamptime:new Date()}
      service.post('/user/readlog',data).then(res=>{
        console.log('记录开始kwl阅读')
      })
    },
    ...mapActions('books', ['getUserBookList', 'getUserBookListByCourse']),
    ...mapActions('user', ['viewBook', 'getUserCourse']),
    getAllBookList(page, pageSize) {
      console.log('test: ' + page + ' : ' + pageSize)
      this.listLoading = true
      this.getUserBookList({ page, pageSize }).then((response) => {
        console.log("booklist:"+response)
        this.booklist = response.list
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
        this.booklist = response.list
        this.total = response.total
        this.currentPage = response.pageNum
        this.listLoading = false
      })
    },
    gettaskAllList() {
      // const data1 = { userid: getToken() }
      service.get('/course/getClassifyListAndBook', { params: { token: getToken() }}).then(res => {
        // console.log('直接调用后端：' + res.data[0].work)
        // console.log('直接调用后端：' + res.data[0].children)
        this.list = res.data
      })
    },
    gettaskAllListBycourse(courseId) {
      // const data1 = { userid: getToken() }
      service.get('/course/getClassifyListAndBookByCourse', { params: { courseId: courseId }}).then(res => {
        this.list = res.data
      })
    },
    // formatDate(dateStr) {
    //   const date = new Date(dateStr)
    //   return dateUtils.formatDate(date, 'MM-dd')
    // },

    // viewBookAndNum(id) {
    //   this.viewBook(id)
    //   this.$router.push({ path: '/studyLook/index', query: {bookid: id}})
    //   const data = { userid: getToken(), behavior:'查看文章',bookid:id, stamptime:new Date()}
    //   service.post('/user/readlog',data).then(res=>{
    //     console.log('阅读文章页面：记录查看文章')
    //   })
    // },
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
      return dateUtils.formatDate(date, 'MM-dd')
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
