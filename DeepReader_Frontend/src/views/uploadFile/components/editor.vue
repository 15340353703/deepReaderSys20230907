<template>
  <vue-ueditor-wrap :config="myConfig" v-model="copyContent" :editor-id="editorId"></vue-ueditor-wrap>
</template>

<script>
import VueUeditorWrap from 'vue-ueditor-wrap';

export default {
  name: 'Editor',
  components: {
    VueUeditorWrap
  },
  data() {
    return {
      editorId: `editor-${new Date().getTime()}`, //唯一id,防止editor缓存
      copyContent: '',
      editor:''
    };
  },
  props: {
    // 富文本高度
    initialFrameHeight: {
      type: Number,
      default() {
        return 1000;
      }
    },
    // 富文本内容
    content: {
      type: String,
      default: ''
    },
    // 富文本是否只读状态
    readonly:{
      type:Boolean,
      default:false
    }
  },
  computed: {
    myConfig() {
      return {
        // 如果需要上传功能,找后端小伙伴要服务器接口地址,否则无法上传，上传功能需后端配合。
        serverUrl: 'http://222.198.118.211:8081/config',
        // 你的UEditor资源存放的路径,相对于打包后的index.html(路由使用history模式注意使用绝对路径或者填写正确的相对路径)
        UEDITOR_HOME_URL: '/UEditor/',
        // // 编辑器不自动被内容撑高
        // autoHeightEnabled: false,
        // 初始容器高度
        initialFrameHeight: this.initialFrameHeight,
        // 初始容器宽度
        initialFrameWidth: '100%',
        // 关闭自动保存
        enableAutoSave: true,
        // zIndex:10,
        readonly: this.readonly
      };
    }
  },
  mounted() {
    const _this = this;
    this.editor = VueUeditorWrap.getEditor('editor', this.config); // 初始化UE
    this.editor.addListener("ready", function () {
      _this.editor.setContent(_this.msg); // 确保UE加载完成后，放入内容。
    });
  },
  model: {
    prop: 'content',
    event: 'change'
  },
  methods: {
    getUEContent() { // 获取内容方法
      return this.copyContent;
    }
  },
  watch: {
    copyContent(val) {
      let me = this;
      me.$emit('change', val);
    },
    content:{
      immediate:true,
      handler(val){
        let me = this;
        me.copyContent = val;
      }
    }
  }
};
</script>
