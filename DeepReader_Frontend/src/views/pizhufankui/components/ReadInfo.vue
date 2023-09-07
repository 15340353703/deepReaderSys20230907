<template>
  <el-row>
    <el-col>
      <el-card>
        <div class="header">
          <el-divider>
            <h3 style="text-align: center">文章批注支架反馈信息</h3>
          </el-divider>
        </div>

        <div class="content">
          <el-row>
            <el-col :span="10" style="height: 270px">
              <RadarChartReadInfo :chartData="tagsNum" />
            </el-col>
            <el-col :span="12">
              <div class="card-panel-description">
                <div style="font-size: 15px">小智说：</div>
                <div v-if="tagsNum.js < 2" class="iconfont icon-kulian" style="color: #FF3300">检索： 加油哦！掌握文章核心观点有助于理解，比如专业词汇与基本概念，请试着使用“检索”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian " style="color: #00CC00">检索： 表现不错哦，检索便于以后查找和浏览，帮助理解阅读文章。请继续保持！</div>
                <div v-if="tagsNum.tw < 2" class="iconfont icon-kulian" style="color: #FF3300">提问： 加把劲哦！文章中有没有你感到疑惑的地方呢，比如不理解的专业词汇或者对文献观点有疑问。多提问更有利于他人来解答，帮助你更快的成长哦！请试着使用“提问”批注来说明吧！
                  请试着使用“提问”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">提问： 同学，给力！问题意识比较强烈，看来你的求知欲和探究欲很强哦，多多提问，继续保持！</div>
                <div v-if="tagsNum.fx < 2" class="iconfont icon-kulian" style="color: #FF3300">分析： 加油哦！用自己的语言解释、比较和总结文章重要观点和段落信息，包括研究问题、研究设计过程（变量的测量）、研究结果。请试着使用“分析”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">分析： 你是最棒的！逻辑和整合信息能力不错，请继续保持！</div>
                <div v-if="tagsNum.pj < 2" class="iconfont icon-kulian" style="color: #FF3300">评价： 加把劲哦！文章的局部内容和整体写作框架如何，包括创新点和不足，比如研究综述、研究设计与方法的优缺点。请试着使用“评价”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">评价： 同学，干得漂亮！请继续保持！</div>
                <div v-if="tagsNum.fs < 2" class="iconfont icon-kulian" style="color: #FF3300">反思： 加油哦！通过文章学到了什么，对你以后研究有何启发？请试着使用“反思”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00"> 反思：非常棒！多多反思有利于产生新想法</div>
                <div v-if="tagsNum.cz < 2" class="iconfont icon-kulian" style="color: #FF3300">创造： 加把劲哦！对于文章内容你有什么新想法吗？比如不一样的研究方法或采取不同方式测量变量，请试着使用“创造”批注来说明吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">创造： 厉害了，同学！有自己独特的见解。请继续保持！</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-col>
    <el-col>
      <el-card>
        <div class="header">
          <el-divider>
            <h3 style="text-align: center">同伴评论支架反馈信息</h3>
          </el-divider>
        </div>

        <div class="content">
          <el-row>
            <el-col :span="10" style="height: 270px">
              <RadarChartPeerReview :chartData="tagsNum" />
            </el-col>
            <el-col :span="12">
              <div class="card-panel-description">
                <div style="font-size: 15px">小智说：</div>
                <div v-if="tagsNum.dayi < 3" class="iconfont icon-kulian" style="color: #FF3300">答疑： 解答同学的问题有助于自身的理解哦，帮助大家一起成长，请试着使用“答疑”批注来回复吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">答疑：你的回答解决了其他同学的困惑，得到了大家的认可，请继续保持！</div>
                <div v-if="tagsNum.zhiyi < 3" class="iconfont icon-kulian" style="color: #FF3300">质疑： 敢于质疑，探究真知，有助于同学间深入探讨，提出自己的困惑或者质疑他人的观点。请试着使用“提问”批注来回复吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">质疑：善于质疑，干得漂亮，请继续保持！</div>
                <div v-if="tagsNum.chanshi < 3" class="iconfont icon-kulian" style="color: #FF3300">阐释：通过举例、对比等方式对概念、观点进行辨别和详细解释，比如和……相比，它的优缺点如何。请试着使用“阐释”批注来回复吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">阐释：叙述得不错，详细的说明更能说服别人哦，请继续保持！</div>
                <div v-if="tagsNum.xieshang < 3" class="iconfont icon-kulian" style="color: #FF3300">协商： 对他人的观点进行协商讨论，表明态度并说明理由，比如我支持……，因为……，请试着使用“协商”批注来回复吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00">协商：有进一步深度思考，积极交流更有利于协作学习，请继续保持</div>
                <div v-if="tagsNum.shejiao < 3" class="iconfont icon-kulian" style="color: #FF3300">社交：情感交流有助于营造轻松氛围，在相互交流中不断成长，比如谢谢你的建议，请试着使用“社交”批注来回复吧！</div>
                <div v-else class="iconfont icon-xiaolian" style="color: #00CC00"> 社交：因为你的礼貌和鼓励，学习变得轻松友好，请继续保持！</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import RadarChartReadInfo from './RadarChartReadInfo'
import RadarChartPeerReview from './RadarChartPeerReview'
import Axios from 'axios'
import { mapState, mapActions } from 'vuex'
export default {
  name: 'ReadInfo',
  components: {
    RadarChartReadInfo,
    RadarChartPeerReview
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      userInfo: '',
      hypothesisid: '',
      groupnameHp: '',
      result: {},
      TagsInfo: {},
      tagsNum: {
        js: 4,
        tw: 1,
        fx: 5,
        pj: 0,
        fs: 2,
        cz: 2,
        dayi: 1,
        zhiyi: 5,
        chanshi: 6,
        xieshang: 2,
        shejiao: 1
      }

    }
  },
  computed: {
    ...mapState('books', ['title'])
  },
  created() {
    console.log('这是图书编号' + this.book.bookId)
    this.getCourse(this.book.bookId)
    this.getTagsInfo(this.book)
  },
  methods: {
    ...mapActions('classify', ['getCourseBybookid']),
    getCourse(bookid) {
      this.getCourseBybookid(bookid).then(res => {
        this.groupnameHp = res[0].groupnameHP
        this.getBook(this.book.bookId)
      })
    },
    ...mapActions('books', ['getBookInfo']),
    getBook(bookId) {
      this.getBookInfo(bookId).then((response) => {
        this.book = response
        this.book.bookId = this.$route.query.bookId
        document.title = this.book.name
        this.getuserInfo()
      })
    },
    ...mapActions('user', ['getInfo', 'getUserCourse']),
    getuserInfo() {
      this.getInfo().then(res => {
        this.userInfo = res
        console.log('查询用户id' + this.userInfo.id)
        this.hypothesisid = this.userInfo.hypothesisname
        this.getTagsInfo(this.book)
        this.getUserCourse(this.userInfo.id).then(res => {
          this.course = res
        })
      })
    },
    getTagsInfo(book) {
      console.log('bookname:' + this.book.name)
      console.log('bookurl:' + this.book.url)
      const usernamehy = 'acct:' + this.userInfo.hypothesisname + '@hypothes.is'
      Axios.get('https://api.hypothes.is/api/search', {
        headers: { Authorization: 'Bearer' + ' ' + '6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          limit: 200, user: usernamehy, uri: this.book.url
        }
      }).then(res => {
        const userrows = res.data.rows
        console.log(userrows)
        const tags = []
        for (let i = 0; i < userrows.length; i++) {
          tags.push(userrows[i].tags)
          // Axios.get('https://api.hypothes.is/api/search', {
          //   headers: { Authorization: 'Bearer' + ' ' + '6879-B1Xo4omWN4aijHbJH3VwB0Vd083X9logI-yR87ixl0k' }, params: {
          //     limit: 200, references: userrows[i].id, uri: this.book.url
          //   }
          // }).then(res => {
          //   const refs = res.data.rows
          //   for (let j = 0; j < refs.length; j++) {
          //     tags.push(refs[j].text + ' ')
          //     console.log(' hahha ' + this.tags)
          //   }
          // })
        }
        console.log(tags)
        for (let i = 0; i < tags.length; i++) {
          for (let j = 0; j < tags[i].length; j++){
            if (tags[i][j] === '检索') {
              this.tagsNum.js++
            } else if (tags[i][j] === '提问') {
              this.tagsNum.tw++
            } else if (tags[i][j] === '反思') {
              this.tagsNum.fs++
            } else if (tags[i][j] === '创造') {
              this.tagsNum.cz++
            } else if (tags[i][j] === '评价') {
              this.tagsNum.pj++
            } else if (tags[i][j] === '分析') {
              this.tagsNum.fx++
            } else if (tags[i][j] === '答疑') {
              this.tagsNum.dayi++
            } else if (tags[i][j] === '质疑') {
              this.tagsNum.zhiyi++
            } else if (tags[i][j] === '阐释') {
              this.tagsNum.chanshi++
            } else if (tags[i][j] === '协商') {
              this.tagsNum.xieshang++
            } else if (tags[i][j] === '社交') {
              this.tagsNum.shejiao++
            }
          }
        }
        console.log(this.tagsNum)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
                     .card-panel-description {
                       /*background-color: rgb(240, 242, 245);*/
                       position: relative;
                       line-height: 30px;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
  /*div{*/
  /*  word-break:normal;*/
  /*  width:auto;*/
  /*  display:block;*/
  /*  white-space:pre-wrap;*/
  /*  word-wrap : break-word ;*/
  /*  overflow: hidden ;*/
  /*}*/
  .icon-kulian{
    color: #df5000;
  }
  .icon-xiaolian{
    color: #5070C5;
  }
</style>
