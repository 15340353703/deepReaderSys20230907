<template>
  <el-form :label-position="labelPosition" label-width="160px" :model="formLabelAlign">
    <el-form-item label="请输入课程码">
      <el-input v-model="courseNum"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">添加课程 (Add)</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: ''
        }
      }
    }
  },
  data() {
    return {
      courseNum: ''
    }
  },
  computed: {
    ...mapState('user', ['name'])
  },
  methods: {
    ...mapActions('user', ['addCourse']),
    submit() {
      if (this.courseNum === '' || this.courseNum===null) {
        this.$message.error('fail: 课程码不可为空!')
      } else {
        this.addCourse(this.courseNum).then(response => {
          if(response.code===500){
            this.$message.warning('fail: 已有该课程!')
          }
        })
      }
    }
  }
}
</script>
