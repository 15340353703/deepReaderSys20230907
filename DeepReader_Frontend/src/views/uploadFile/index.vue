<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">
        <el-col :span="24" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane v-if="this.active==0" label="上传文章pdf" name="uploadFile">
                <uploadFile  :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="this.active==0" label="上传文章文本" name="uploadtext">
                <uploadtext  :user="user" />
              </el-tab-pane>
<!--              <el-tab-pane v-if="this.active==1" label="创建作业" name="uploadWork">-->
<!--                <uploadWork  :user="user" />-->
<!--              </el-tab-pane>-->
<!--              <el-tab-pane v-if="this.active==2" label="创建课程" name="uploadCourse">-->
<!--                <uploadCourse  :user="user" />-->
<!--              </el-tab-pane>-->
<!--              <el-tab-pane v-if="this.active == 0" label="上传文章" name="uploadFile">-->
<!--                <uploadFile :user="user" />-->
<!--              </el-tab-pane>-->
              <el-tab-pane v-if="this.active == 1" label="创建作业" name="uploadWork">
                <uploadWork :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="this.active == 2" label="创建课程" name="uploadCourse">
                <uploadCourse :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="this.active == 3" label="查看课程" name="showCourseInfo">
                <showCourseInfo :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="this.active == 4" label="查看作业" name="showWorkInfo">
                <showWorkInfo :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="this.active == 5" label="查看文章" name="showFileInfo">
                <showFileInfo :user="user" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import uploadFile from './components/uploadFile'
import uploadWork from './components/uploadWork'
import uploadCourse from './components/uploadCourse'
import uploadtext from './components/uploadtext'
import showCourseInfo from './components/showCourseInfo'
import showWorkInfo from './components/showWorkInfo'
import showFileInfo from './components/showFileInfo'

export default {
  name: 'Study',
  components: { uploadFile,uploadWork,uploadCourse,uploadtext,showCourseInfo, showWorkInfo, showFileInfo },
  data() {
    return {
      user: {},
      major: {},
      activeTab: 'uploadFile',
      active: 0
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles',
      'username',
      'sex',
      'nation'
    ])
  },
  created() {
    this.active=this.$route.query.cid //获取操作编号
    if(this.active==0){
      this.activeTab='uploadFile'
    }else if(this.active==1){
      this.activeTab='uploadWork'
    }else if (this.active == 2) {
      this.activeTab = 'uploadCourse'
    } else if (this.active == 3) {
      this.activeTab = 'showCourseInfo'
    } else if (this.active == 4) {
      this.activeTab = 'showWorkInfo'
    } else if (this.active == 5) {
      this.activeTab = 'showFileInfo'
    }
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        name: this.name,
        role: this.roles.join(' | '),
        email: 'admin@test.com',
        avatar: this.avatar,
        username: this.username,
        sex: this.sex,
        nation: this.nation
      }
    },
    showCID(){
      console.log(this.controlNum)
    }
  }
}
</script>
