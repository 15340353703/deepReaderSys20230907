<template>
  <div class="app-container">
    <el-container v-loading="loading">
      <el-header>
        <div>
          <el-button @click="showScore">测试得分</el-button>
          <el-button @click="showQuestion">测试提问</el-button>
        </div>
      </el-header>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Axios from "axios";
import qs from 'qs';

export default {
  name: "Study",
  data() {
    return {
        loading: false
    };
  },
  computed: {
  },
  created() {
  },
  methods: {
    showScore() {
        this.loading=true
      let data = {
        answer: "毛泽东故乡是湖南。",
        a: "湖南。",
      };
      Axios.post("http://222.198.118.211:9090/similarity",data).then((res) => {
        console.log("res=>", res);
        alert("您的回答得分为："+res.data.score)
      });
    },
    showQuestion(){
        this.loading=true
      let data = {
        answer: "毛泽东故乡是湖南。",
        clue: "湖南。",
      };
      Axios.post("http://222.198.118.211:8888/deepreader",data).then((res) => {
        this.loading=false
        console.log("res=>", res);
        alert("问题生成成功=>"+res.data.generate_Q)
      });
    }
  },
};
</script>

<style>
</style>
