<template>
  <el-form :label-position="labelPosition" label-width="160px" :model="formLabelAlign">
    <el-form-item label="请输入你的hypothesis注册账号">
      <el-input v-model="hypothesisName"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">确认</el-button>
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
      hypothesisName: ''
    }
  },
  computed: {
    ...mapState('user', ['name'])
  },
  methods: {
    ...mapActions('user', ['updateUserHypothesis']),
    submit() {
      if (this.hypothesisName === '' || this.hypothesisName===null) {
        this.$message.error('fail: 输入不可为空!')
      } else {
        this.updateUserHypothesis(this.hypothesisName).then(response => {
        })
        this.$message.success('提交成功')
      }
    }
  }
}
</script>
