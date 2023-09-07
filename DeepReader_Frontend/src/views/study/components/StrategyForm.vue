<template>
  <div id="nav" style="height: 650px;">
    <el-scrollbar v-if="!showContent" style="height:100%">

      <el-card v-if="active == 0" class="box-card" style="text-align: center">
        <div class="text item">
          在精读文章之前，关于这个主题，你能联想到曾经学过的哪些知识？
        </div>
      </el-card>

      <el-card v-if="active == 1" class="box-card">
        <div class="text item">
          请精读左侧文章，精读完成后，根据文章内容，在下方回答您之前提出的问题
        </div>
      </el-card>

      <el-card v-if="active == 3" class="box-card">
        <div class="text item">
          读完此文，请创建测试题，考考你的同伴，主要目的在于检验学习者对文章主要内容、主题思想的理解，问题的答案在文章可以找到。
        </div>
      </el-card>

      <el-card v-if="active == 4" class="box-card" style="text-align: center">
        <el-button type="success" icon="el-icon-check" circle />
        <div class="text item">
          <br>
          提交成功
        </div>
      </el-card>
      <el-card v-if="active == 5" class="box-card">
        <div class="text item">
          小机器人尝试从不同角度提出问题，帮助你反思问题质量，从而提升你的提问能力。
        </div>
      </el-card>

      <el-divider><i class="el-icon-notebook-2" /></el-divider>

      <div style="text-align: center">
        <el-form
          v-if="active == 0"
          ref="dynamicValidateForm"
          :model="dynamicValidateForm"
          label-width="100px"
          class="demo-dynamic"
        >
          <el-form-item
            v-for="(know, index) in dynamicValidateForm.knows"
            :key="know.key"
            :label="'已知' + (index+1)"
            :prop="'knows.' + index + '.value'"
            :rules="{
              required: true,
              message: '不可为空！',
              trigger: 'blur',
            }"
          >
            <el-col :span="20">
              <el-input
                v-model="know.value"
                type="textarea"
                :rows="2"
                placeholder="请输入内容"
              />
            </el-col>
            <el-button
              type="danger"
              @click.prevent="removeKnow(know)"
              icon="el-icon-delete" circle
            ></el-button>
          </el-form-item>

          <el-button
            v-if="active == 0"
            size="medium"
            type="success"
            @click="addKnow"
            icon="el-icon-plus" circle
          ></el-button>

          <el-divider><i class="el-icon-notebook-2" /></el-divider>

          <el-card class="box-card">
            <div class="text item">
              <!-- 关于这个主题，你存在哪些疑惑？你将带着这个疑惑去精度这个文章。 -->
              关于这篇文章，你可以提出1-5个问题，参考问题如下：
              <br/>
              <el-divider/>
              <div style="line-height: 25px;text-align:center">
               1.作者的主要观点是什么?<br/>
               2.这篇文章解决了什么问题？<br/>
               3.作者为什么要进行本项研究？<br/>
               4.作者是如何得出这些结论的，逻辑合理吗？<br/>

              </div>
            </div>
          </el-card>
          <br>

          <el-form-item
            v-for="(domain, index) in dynamicValidateForm.doubts"
            :key="domain.key"
            :label="'问题' + (index+1)"
            :prop="'doubts.' + index + '.value'"
            :rules="{
              required: true,
              message: '提问不能为空',
              trigger: 'blur',
            }"
          >
            <el-col :span="20">
              <el-input v-model="domain.value" />
            </el-col>
            <el-button
              type="danger"
              @click.prevent="removeDomain(domain)"
              icon="el-icon-delete" circle
            ></el-button>
          </el-form-item>

          <el-button
            v-if="active == 0"
            size="medium"
            type="success"
            @click="addDomain"
            icon="el-icon-plus" circle
          ></el-button>

          <el-divider><i class="el-icon-notebook-2" /></el-divider>

        </el-form>

        <el-form
          v-if="active == 1"
          ref="dynamicValidateForm"
          :model="dynamicValidateForm"
          class="demo-dynamic"
        >
          <el-form-item
            v-for="(domain, index) in dynamicValidateForm.doubts"
            :key="domain.key"
            :label="'问题' + (index+1) + ' : ' + domain.value"
            :prop="'doubts.' + index + '.result'"
            :rules="{
              required: true,
              message: '提问不能为空',
              trigger: 'blur',
            }"
          >
            <el-input v-model="domain.result" />
          </el-form-item>
        </el-form>

        <el-form
          v-if="active == 3"
          style="text-align: left"
          class="demo-dynamic"
          ref="questionForm" >
<!--          :rules="questionrule"-->


          <el-form-item label="问题线索" prop="questionClues">
            <el-tooltip class="item" effect="dark" content="文中主要的文本片段，可以暗示答案的问题答案（您的问题来源于文中的哪些关键句？）" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
            <el-input
              v-model="questionForm.questionClues"
              type="textarea"
              :autosize="{ minRows: 4, maxRows: 8}"
              placeholder="请给出其他同学查找该问题答案的线索"
            />
          </el-form-item>
          <el-form-item label="问题类型" prop="questionType">
            <el-select v-model="questionForm.questionType" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="问题" prop="questionDescribe">
            <el-tooltip class="item" effect="dark" content="请输入您为其他提出的问题" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
            <el-input
              v-if="saveModifyQuestion == false"
              v-model="questionForm.questionDescribe"
              placeholder="请输入向其他同学提出的问题"
            />
            <el-input
              v-if="saveModifyQuestion"
              v-model="questionForm.modifyQuestion"
              placeholder="请输入向其他同学提出的问题"
            />
          </el-form-item>

          <el-form-item label="问题答案" prop="questionAnswer">
            <el-input
              v-model="questionForm.questionAnswer"
              placeholder="请给出该问题的答案"
            />
          </el-form-item>

          <el-form-item label="请求小机器人的帮助">
            <el-tooltip class="item" effect="dark" content="小机器人尝试从不同角度提出问题，帮助你反思问题质量，从而提升你的提问能力。" placement="top">
              <i class="el-icon-question" />
            </el-tooltip>
            <br>
            <el-image
              style="width: 100px; height: 100px"
              :src="require('../../../assets/robot.jpg')"
              :fit="fit"
              @click="robotHelp"
            />
          </el-form-item>
        </el-form>

      </div>
      <div v-if="this.active==5">
        <el-card>
          <el-row>
            <el-col :span="24">
              <el-form>
                <div style="background-color: #1aa2b8;text-align: left;width: 100%">
                  <div style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 13px">你提出的问题和答案</div>
                  <div style="color: white;margin: 8px;">
                    <div>问题：{{ questionForm.questionDescribe }} </div>
                    <br>
                    <div style="padding-top: 13px">答案：{{ questionForm.questionAnswer }}</div>
                  </div>
                </div>
                <div style="background-color: #1aa2b8;text-align: left;width: 100%;">
                  <span style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 8px">小机器人提出的问题和答案</span>
                  <el-button style="background-color:white;color: #ffba00;margin-top: 8px;font-size: 14px;" @click="morerobotQuestion">点击获取更多机器人提问</el-button>
                  <div v-if="questionForm.onerobotQuestion === ''" style="color: #ffba00;font-weight: bold;font-size: 16px;text-align: center;margin: 8px;padding-top: 8px">正在加载中......</div>
                  <div v-if="questionForm.onerobotQuestion" style="white-space: pre-wrap;color: white;margin: 8px;">{{ questionForm.onerobotQuestion }}</div>
                  <span v-if="showmorerobotQuestion === true" style="color: #ffba00;font-weight: bold;font-size: 16px;margin: 8px;padding-top: 8px">更多机器人提问</span>
                  <div v-if="questionForm.morerobotQuestion === '' && showmorerobotQuestion === true" style="color: #ffba00;font-weight: bold;font-size: 16px;text-align: center;margin: 8px;padding-top: 8px">正在加载中......</div>
                  <div v-if="questionForm.morerobotQuestion && showmorerobotQuestion === true" style="white-space: pre-wrap;color: white;margin: 8px;">{{ questionForm.morerobotQuestion }}</div>
                </div>
                <el-form-item label="你是否愿意采用小机器人提出的问题" style="text-align: left">
                  <el-button type="primary" size="small" @click="modifyQuestion">愿意</el-button>
                  <div v-if="showmodifyQuestion">
                    <div>从表达是否合理、问题是否与材料有关、是否引发深层次思考等角度出发可以对机器人提出的问题进行修改</div>
                    <div>
                      <span>问题：</span>
                      <el-select v-model="selectedObj" @change="handleObjChange" placeholder="请选择问题" style="width: 90%;margin-top: 8px">
                        <el-option v-for="obj in allQuestionList" :key="obj.id" :label="obj.question" :value="obj"></el-option>
                      </el-select>
                    </div>
                    <div>
                      <span>新问题：</span>
                      <el-input v-model="selectedObj.question"  placeholder="可以修改小机器人的提问" style="width: 87%;margin-top: 8px"></el-input>
                    </div>
                    <div>
                      <span>答案：</span>
                      <el-input v-model="selectedObj.answer" placeholder="可以修改小机器人提问的答案" style="width: 89%;margin-top: 8px"></el-input>
                    </div>
                    <div>
                      <span>补充线索：</span>
                      <el-input
                        v-model="questionForm.supplementClues"
                        type="textarea"
                        :autosize="{ minRows: 4, maxRows: 8}"
                        placeholder="如果该问题的答案不能在文中找到，请在这里补充线索"
                      />
                    </div>
                  </div>
                </el-form-item>
                <el-form-item label="你觉得小机器人提出的问题对你有帮助吗？">
                  <el-rate v-model="questionForm.robotRate" :show-text="true" :texts="texts" style="margin-top: 8px;width: 600px;margin-left: -100px;"/>
                  <!--                  <el-input-->
                  <!--                  v-model="questionForm.modifyQuestion"-->
                  <!--                  placeholder="请选择具体有哪些帮助" />-->
<!--                  <el-select v-if="questionForm.robotRate < 3" v-model="questionForm.suggestionhelp" multiple placeholder="请选择具体有哪些帮助" style="width: 100%;margin-top: 8px">-->
<!--                    <el-option v-for="option in badsuggestionhelp" :key="option" :value="option">{{ option }}</el-option>-->
<!--                  </el-select>-->
                  <el-select v-model="questionForm.suggestionhelp" multiple placeholder="请选择具体有哪些帮助" style="width: 100%;margin-top: 8px">
                    <el-option v-for="option in goodsuggestionhelp" :key="option" :value="option">{{ option }}</el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogFormVisible = false">提交</el-button>
              </div>
            </el-col>
          </el-row>
          <div style="text-align: center">
            <el-button type="primary" @click="saveQuestion">保存</el-button>
            <el-button @click="cancelQuestion">取消</el-button>
          </div>
        </el-card>
      </div>

<!--      <div v-if="this.active==3">-->
<!--        <el-dialog title="小机器人尝试从不同角度提出问题，帮助你反思问题质量，从而提升你的提问能力。" :visible.sync="dialogFormVisible">-->
<!--          <el-row>-->
<!--            <el-col :span="18">-->
<!--              <el-form>-->
<!--                <el-form-item label="小机器人提问" :label-width="formLabelWidth">-->
<!--                  <p>{{ questionForm.robotQuestion }}</p>-->
<!--                </el-form-item>-->
<!--                <el-form-item label="你的提问" :label-width="formLabelWidth">-->
<!--                  <p>{{ questionForm.questionDescribe }}</p>-->
<!--                </el-form-item>-->
<!--&lt;!&ndash;                <el-form-item label="对比你提出的问题，你觉得小机器人提出的问题对你有帮助吗？">&ndash;&gt;-->
<!--                <el-form-item label="你愿意采用小机器人提的问题吗？">-->
<!--                  <br>-->
<!--                  <el-rate v-model="questionForm.robotRate" />-->
<!--                  <el-input-->
<!--                    v-model="questionForm.suggestionhelp"-->
<!--                    placeholder="请输入内容" />-->
<!--                </el-form-item>-->
<!--&lt;!&ndash;                <el-form-item v-if="this.questionForm.robotRate<=3" label="你觉得自己提出的问题需要改进吗？">&ndash;&gt;-->
<!--                <el-form-item label="你觉得自己提出的问题是否需要改进？">-->
<!--                  <el-button type="primary" size="small" @click="modifyQue=true">是</el-button>-->
<!--                  <el-input-->
<!--                    v-if="modifyQue === true"-->
<!--                    v-model="questionForm.modifyQuestion"-->
<!--                    placeholder="请输入改进后的问题" />-->
<!--                </el-form-item>-->
<!--              </el-form>-->
<!--&lt;!&ndash;              <div slot="footer" class="dialog-footer">&ndash;&gt;-->
<!--&lt;!&ndash;                <el-button @click="dialogFormVisible = false">取 消</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                <el-button type="primary" @click="dialogFormVisible = false">提交</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;              </div>&ndash;&gt;-->
<!--            </el-col>-->
<!--            <el-col :span="6">-->
<!--              <el-image-->
<!--              style="width: 100px; "-->
<!--              :src="require('../../../assets/robotHelp.png')"-->
<!--              :fit="fit"></el-image>-->
<!--              <el-button-->
<!--                size="medium"-->
<!--                type="primary"-->
<!--                @click="openDialog"-->
<!--              >查看原文</el-button>-->
<!--            </el-col>-->
<!--          </el-row>-->
<!--          <el-row>-->
<!--            <div style="margin-left: 45%">-->
<!--              <el-button  size="medium" type="primary" @click="saveRobatInfo">保存</el-button>-->
<!--              <el-button @click="cancelRobatInfo">取 消</el-button>-->
<!--            </div>-->
<!--          </el-row>-->
<!--        </el-dialog>-->
<!--      </div>-->

<!--      <div>-->
<!--        <el-dialog title="原文" :visible.sync="dialogFormVisible">-->
<!--          <div>-->
<!--            <div v-if="active1" style="height: 650px; overflow: auto">-->
<!--              <p v-html="book.content"></p>-->
<!--            </div>-->
<!--            <div v-else-if="active2" class="block" style="height: 650px; overflow: auto">-->
<!--              &lt;!&ndash; <h3 style="text-align: center">《{{ book.name }}》</h3>-->
<!--              <el-divider content-position="right">作者：{{ book.author }}</el-divider> &ndash;&gt;-->

<!--              <div style="text-align: center">-->
<!--                &lt;!&ndash; <el-button @click="scalBig" type="success">放大</el-button>-->
<!--                <el-button @click="scalSmall" type="primary">缩小</el-button>  &ndash;&gt;-->
<!--                <el-button @click="newPage(book.url)" type="primary" style="margin-bottom:10px">文本标注</el-button>-->
<!--              </div>-->
<!--              <div class="drag-box" id="dragBox" style="text-align: center">-->
<!--                <iframe :src="book.url" height="600" width="100%"></iframe>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->

<!--          <div slot="footer" class="dialog-footer">-->
<!--            <el-button-->
<!--              type="primary"-->
<!--              @click="dialogFormVisible = false"-->
<!--            >关闭</el-button>-->
<!--          </div>-->
<!--        </el-dialog>-->
<!--      </div>-->


      <div style="text-align: center">
        <el-button v-if="active == 4" size="medium" type="success" @click="createQuestion">继续创建测试题</el-button>
        <el-button v-if="active == 4" size="medium" type="primary" @click="next">返回</el-button>

        <el-button v-if="active != 3 && active != 4 && active != 5" size="medium" type="primary" @click="next">下一步</el-button>

        <el-button v-if="active == 3" size="medium" type="success" @click="next">提交问题</el-button>

        <!--<el-button @click="back" size="medium" type="warning" v-if="active == 2">返回上一步</el-button>-->

        <el-button
          v-if="active == 0"
          size="medium"
          type="warning"
          @click="resetForm('dynamicValidateForm')"
        >重置</el-button>
      </div>

      <div v-if="active==1" style="text-align: center">
        <h4>阅读时间：{{ this.timeData }}</h4>
      </div>
    </el-scrollbar>

    <el-scrollbar v-if="showContent" style="height:100%">
      <el-row :gutter="20">
        <el-col :span="7" :xs="24">
          <el-card style="height: 600px; overflow: auto">
            <div style="text-align:center">
              <h3 style="background-color: rgba( 203,27,69,0.2 );">在读这篇文章之前， 我已经知道了以下知识</h3>
            </div>
            <el-divider><i class="el-icon-notebook-2" /></el-divider>
            <el-timeline :reverse="true">
              <el-timeline-item
                v-for="(know, index) in this.dynamicValidateForm.knows"
                :key="index"
              >
                {{ know.value }}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
        <el-col :span="7" :xs="24">
          <el-card style="height: 600px; overflow: auto">
            <div style="text-align:center">
              <h3 style="background-color: rgba( 67,52,27,0.2 );">我带着疑问去精读，解决了如下疑惑</h3>
            </div>
            <el-divider><i class="el-icon-notebook-2" /></el-divider>
            <el-card v-for="(domain, index) in dynamicValidateForm.doubts" :key="index" :value="domain" :title="domain.value" style="margin-bottom: 20px">
              <div>问题：{{ domain.value }}</div>
              <el-divider />
              <div>答案：{{ domain.result }}</div>
            </el-card>
          </el-card>
        </el-col>
        <el-col :span="10" :xs="24">
          <el-card style="height: 600px; overflow: auto">

            <div style="text-align:center">
              <h3 style="background-color: rgba( 27,129,62,0.2 );">读完这篇文章之后，我的总结反思如下：</h3>
            </div>
            <el-divider><i class="el-icon-notebook-2" /></el-divider>

            <!-- <el-card v-if="active == 2" class="box-card">
              <div class="text item">
                请用自己的语言，概括本文章的主要内容
              </div>
            </el-card> -->

            <el-divider v-if="active!=2"><i class="el-icon-star-off"/></el-divider>

            <div style="text-align: center">
              <el-form
                style="text-align: left"
                class="demo-dynamic"
              >

                <el-form-item label="读完这篇文章，你的感想是什么？">
                  <span>（包括但不仅限于以下内容：
                    文章的核心观点是什么？
                    对于文章的观点或结论，你有什么想法？你能想到哪些作者没有考虑到的替代性论点？
                    文章创新点在哪？有不足之处吗，怎么改进？
                    注意：有自己的思考，学会阐述自己的观点。）</span>
                  <el-input
                    v-model="dynamicValidateForm.summary.content"
                    type="textarea"
                    :autosize="{ minRows: 4, maxRows: 8}"
                    placeholder="请写下阅读后对文章的概括与自己的反思"
                  />
                </el-form-item>

                <el-form-item label="你喜欢这篇文章吗？请为其打分">
                  <el-rate v-model="dynamicValidateForm.summary.rate" />
                </el-form-item>

              </el-form>
            </div>

            <div style="text-align: center">
              <el-button v-if="active != 3 && active != 5" size="medium" type="primary" @click="next">下一步</el-button>

              <el-button v-if="active == 3" size="medium" type="success" @click="next()">提交问题</el-button>

              <el-button v-if="active == 2" size="medium" type="warning" @click="back">返回上一步</el-button>
              <el-button
                v-if="active == 0"
                size="medium"
                type="primary"
                @click="addDomain"
              >新增问题</el-button>
              <el-button
                v-if="active == 0"
                size="medium"
                type="primary"
                @click="addKnow"
              >新增已知</el-button>
              <el-button
                v-if="active == 0"
                size="medium"
                type="warning"
                @click="resetForm('dynamicValidateForm')"
              >重置</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Axios from 'axios'
import { getToken } from '@/utils/auth'
import service from '@/utils/request'
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
      saveModifyQuestion: false,
      allQuestionList: [],
      selectedObj: {},
      goodsuggestionhelp: [
        '问题表述清晰准确，帮助提升我的问题表达能力',
        '问题具有深度和广度，帮助我深层次思考',
        '问题具有开放性和启发性，帮助提升我的创造力',
        '问题具有相关性和实用性，帮助激发我的发散思维',
        '问题难度适当，帮助提升我的反思能力',
        '问题模糊不清',
        '问题深度停留在材料本身',
        '问题难度过高（过低）',
        '提问角度单一',
        '问题与实际应用无关'
      ],
      // badsuggestionhelp: [
      //   '问题模糊不清',
      //   '问题深度停留在材料本身',
      //   '问题难度过高（过低）',
      //   '提问角度单一',
      //   '问题与实际应用无关'
      // ],
      texts: ['没有帮助', '帮助不大', '一点帮助', '有有帮助', '很大帮助'],
      Qclues: '',
      // allQuestionList: [
      //   // { questions: '问题1', answers: '答案1' },
      //   // { questions: '问题2', answers: '答案2' },
      //   // { questions: '问题3', answers: '答案3' },
      //   // { questions: '问题4', answers: '答案4' },
      //   { id: 1, name: 'obj1', otherAttr: 'other1' },
      //   { id: 2, name: 'obj2', otherAttr: 'other2' },
      //   { id: 3, name: 'obj3', otherAttr: 'other3' }
      // ],
      showmodifyQuestion: false,
      showmorerobotQuestion: false,
      fit: "scale-down",
      modifyQue: false,
      haveSubmit: false,
      showContent: false,
      url: 'https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/src=http___img.zcool.cn_community_01dcd75af39d3ba8012160456172c4.jpg@1280w_1l_2o_100sh.jpg&refer=http___img.zcool.jpg',
      choiceData: [],
      active: 0,
      dialogFormVisible: false,
      questionNum: 0,
      fullscreenLoading: false,
      dynamicValidateForm: {
        bookId: "",
        time: 0,
        knows: [
          {
            value: ''
          }
        ],
        doubts: [
          {
            value: '',
            result: ''
          }
        ],
        summary: {
          content: '',
          rate: 0
        }
      },
      questionForm: {
        bookId: '',
        robotRate: 0,
        robotQuestion: '',
        modifyQuestion: '',
        questionType: '',
        questionDescribe: '',
        questionClues: '',
        questionAnswer: '',
        onerobotQuestion: '',
        morerobotQuestion: '',
        suggestionhelp: '',
        supplementClues: '',

      },
      // questionForm: {
      //   bookId: '',
      //   robotRate: 0,
      //   robotQuestion:'',
      //   modifyQuestion: '',
      //   questionType: '',
      //   questionDescribe: '',
      //   questionClues: '',
      //   questionAnswer: '',
      //   suggestionhelp: ''
      // },
      questionrule: {
        questionType: [
          { required: true, message: '请选择问题类型', trigger: 'change' },
        ],
        questionDescribe: [
          { required: true, message: '请输入你向同伴提出的问题', trigger: 'blur' }
        ],
        questionClues: [
          { required: true, message: '请输入问题线索', trigger: 'blur' }
        ],
        questionAnswer: [
          { required: true, message: '请输入问题的标准答案', trigger: 'blur' }
        ],
      },
      transferProps: {
        key: 'key',
        label: 'value'
      },
      options: [{
        value: '时间',
        label: '时间'
      }, {
        value: '谁',
        label: '谁'
      }, {
        value: '为什么',
        label: '为什么'
      }, {
        value: '怎么样',
        label: '怎么样'
      }, {
        value: '什么',
        label: '什么'
      }, {
        value: '哪些',
        label: '哪些'
      }, {
        value: '哪里',
        label: '哪里'
      }, {
        value: '哪个',
        label: '哪个'
      }, {
        value: '数字',
        label: '数字'
      }],
      timeData: '',
      readTime: 0,
      timer: 0
    }
  },
  props: {
    book: {
      type: Object,
      required: true
    }
  },
  computed: {
    ...mapState('user', ['name'])
  },
  methods: {
    ...mapActions('user', ['updateStuInfo','updateUserKWLForm','updateQuestionForm','sendChatGPT']),
    next() {
      this.timeCount()
      // if (this.active++ > 2) this.active = 0;
      // console.log(this.dynamicValidateForm.doubts[0].value);
      // console.log(this.dynamicValidateForm.doubts[0].result);
      if (this.active == 0) {
        var result = true
        for (var i = 0; i < this.dynamicValidateForm.doubts.length; i++) {
          var val = this.dynamicValidateForm.doubts[i].value
          if (val == '' || val == null) {
            this.failNextAtFirstForm()
            result = false
            break
          }
        }
        for (var i = 0; i < this.dynamicValidateForm.knows.length; i++) {
          var val = this.dynamicValidateForm.knows[i].value
          if (val == '' || val == null) {
            this.failNextAtFirstForm()
            result = false
            break
          }
        }
        if (result) {
          this.submitQuestion()
        }
      } else if (this.active == 1) {
        var result = true
        for (var i = 0; i < this.dynamicValidateForm.doubts.length; i++) {
          var val = this.dynamicValidateForm.doubts[i].result
          if (val == '' || val == null) {
            this.failNextAtSecondForm()
            result = false
            break
          }
        }
        if (result) {
          this.dynamicValidateForm.time=this.readTime
          this.active++
          this.showContent = true
          this.$emit('active', this.active)
          this.$emit('close')
        }
      } else if (this.active == 2) {
        this.active++
        this.$emit('active', this.active)
        this.$emit('open')
        this.showContent = false
      } else if (this.active == 3) {
        /*
        if (questionClues == '' || questionAnswer == '') {
          this.failSubimit()
        }else{
          this.submit()
        }
        */
        this.submit()
      } else {
        this.active = 0
        this.$emit('active', this.active)
        this.$emit('back')
        this.$router.push('/bookSelection/index');
      }
    },
    back() {
      this.active--
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // openDialog() {
    //   const data = {userid: getToken(),behavior:'查看原文',bookid:this.book.id,stamptime:new Date()}
    //   service.post('/user/readlog',data).then(res=>{
    //     console.log('查看原文')
    //   })
    //   this.dialogFormVisible = true
    //   // this.getPdfUrl()
    //   this.viewBookAtExam(this.book.id)
    // },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    removeDomain(item) {
      var index = this.dynamicValidateForm.doubts.indexOf(item)
      var num = this.dynamicValidateForm.doubts.length
      if (index !== -1 && num > 1) {
        this.dynamicValidateForm.doubts.splice(index, 1)
      }
      if (num == 1) {
        this.failDelete()
      }
    },
    removeKnow(item) {
      var index = this.dynamicValidateForm.knows.indexOf(item)
      var num = this.dynamicValidateForm.knows.length
      if (index !== -1 && num > 1) {
        this.dynamicValidateForm.knows.splice(index, 1)
      }
      if (num == 1) {
        this.failDelete()
      }
    },
    addDomain() {
      var num = this.dynamicValidateForm.doubts.length
      if (num >= 5) {
        this.failAdd()
      }
      if (num < 5) {
        this.dynamicValidateForm.doubts.push({
          value: '',
          key: Date.now()
        })
      }
    },
    addKnow() {
      var num = this.dynamicValidateForm.knows.length
      if (num >= 5) {
        this.failAdd()
      }
      if (num < 5) {
        this.dynamicValidateForm.knows.push({
          value: '',
          key: Date.now()
        })
      }
    },
    failDelete() {
      this.$alert('请至少提出一个问题或已知', '删除失败', {
        confirmButtonText: '确定',
        callback: (action) => {
          this.$message({
            type: 'info',
            message: `action: 禁止删除`
          })
        }
      })
    },
    failAdd() {
      this.$alert('至多提出五个问题或已知', '增加失败', {
        confirmButtonText: '确定',
        callback: (action) => {
          this.$message({
            type: 'info',
            message: `action: 禁止增加`
          })
        }
      })
    },
    failSubimit(){
      this.$alert('问题内容，问题答案和不可以为空，请重新填写', '提交失败', {
        confirmButtonText: '确定'
      })
    },
    failNextAtFirstForm() {
      this.$alert('提问或已知不可以为空，请填写您的问题或删除部分空白问题', '跳转失败', {
        confirmButtonText: '确定'
      })
    },
    failNextAtSecondForm() {
      this.$alert('请回答您所提出的问题', '跳转失败', {
        confirmButtonText: '确定'
      })
    },

    // resetQuestionForm(){
    //   alert("清空表单")
    //   this.questionForm.robotRate=5
    //   this.questionForm.modifyQuestion=""
    //   this.questionForm.questionType=""
    //   this.questionForm.questionDescribe=""
    //   this.questionForm.questionClues=""
    //   this.questionForm.questionAnswer=""
    // },
    resetQuestionForm(){
      this.questionForm.robotRate=0
      this.questionForm.modifyQuestion=""
      this.questionForm.questionType=""
      this.questionForm.questionDescribe=""
      this.questionForm.questionClues=""
      this.questionForm.questionAnswer=""
      this.questionForm.onerobotQuestion = ''
      this.questionForm.morerobotQuestion = ''
      this.questionForm.suggestionhelp = ''
      this.questionForm.supplementClues = ''
      this.allQuestionList = []
      this.showmorerobotQuestion = false
      this.showmodifyQuestion = false
      this.saveModifyQuestion = false
      this.questionForm.onerobotQuestion = ''
      this.questionForm.morerobotQuestion = ''
      this.selectedObj = {}
    },
    submit() {
      // if (this.questionForm.questionDescribe === '' && this.questionForm.modifyQuestion !== ''){
      //   this.questionForm.questionDescribe = '无'
      // }
      // if (this.questionForm.questionDescribe !== '' && this.questionForm.modifyQuestion === ''){
      //   this.questionForm.modifyQuestion = '无'
      // }
      if (this.questionForm.questionType!=='' && this.questionForm.questionType!==null && this.questionForm.questionDescribe!=='' && this.questionForm.questionDescribe!==null && this.questionForm.questionAnswer!=='' && this.questionForm.questionAnswer!==null
        && this.questionForm.questionClues!=='' && this.questionForm.questionClues!==null) {
        this.$confirm('是否结束文章阅读并提交！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.active++
          this.$emit('active', this.active)
          if (this.haveSubmit) {
            const data = {userid: getToken(),behavior:'创建问题',bookid:this.book.id,stamptime:new Date()}
            service.post('/user/readlog',data).then(res=>{
              console.log('记录查看文章')
            })
            this.questionForm.bookId = this.book.id
            this.questionForm.robotQuestion = this.questionForm.onerobotQuestion + this.questionForm.morerobotQuestion
            this.updateQuestionForm(this.questionForm)
            // this.resetQuestionForm()
          } else {
            // this.dynamicValidateForm.bookId = this.book.id
            // this.questionForm.bookId = this.book.id
            // this.questionForm.robotQuestion = this.allQuestionList
            this.dynamicValidateForm.bookId = this.book.id
            this.questionForm.bookId = this.book.id
            // this.questionForm.robotQuestion = this.allQuestionList
            this.questionForm.robotQuestion = this.questionForm.onerobotQuestion + this.questionForm.morerobotQuestion
            this.updateUserKWLForm(this.dynamicValidateForm)
            this.updateQuestionForm(this.questionForm)
            this.haveSubmit = true
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消'
          })
        })
      } else {
        this.$message.warning('请将问题信息输入完整')
      }
    },
    splitQuestion(){
      const allRobotQuestion = (this.questionForm.onerobotQuestion + this.questionForm.morerobotQuestion).replace(/\?/g, '？').replace(/\./g, '。').replace(/:/g, '：')

      // let text = "问题1？问题2？问题3？答案1。答案2。答案3。";
      // let questionArr = allRobotQuestion.match(/[^？。]+[？]/g); // 匹配问号句子
      // let answerArr = allRobotQuestion.match(/[^？。]+[。]/g); // 匹配句号句子
      // const regex = /问题：([^?]+)\?答案：([^。]+)。/g;  [^：？]+[：？]([^：？]+)[：？]/g",
      // const questionArr = allRobotQuestion.match(/问题|问|Question|Q|Q1|Q2|Q3[^：？]+[：？]([^：？]+)[：？]/g); // 匹配问号句子
      // const answerArr = allRobotQuestion.match(/答案：|答：|Answer:|A|A1:|A2:|A3:+[^？。]+[。]/g); // 匹配问号句子
      // const questionArr = allRobotQuestion.match(/[^：？]+[：？]([^：？]+)[：？]/g); // 匹配问号句子
      // const answerArr = allRobotQuestion.match(/[^：。]+[：。]([^：。]+)[：。]/g); // 匹配问号句子
      const questionArr = allRobotQuestion.match(/[^？。]+[？]/g); // 匹配问号句子
      const answerArr = allRobotQuestion.match(/[^？。]+[。]/g); // 匹配问号句子
      for (let i = 0; i < questionArr.length; i++) {
        const obj = {};
        obj.question = questionArr[i];
        obj.answer = answerArr[i];
        this.allQuestionList.push(obj);
      }
    },
    api(content) {
      const aws_auth_header = {
        'Accept': 'application/json, textain, */*',
        'Content-Type': 'application/json; charset=utf-8',
        'Authorization': 'Basic Q2hhdEdQVEFkbWluOkNHQWRtaW4wOTgp'
      }
      return fetch('http://18.191.181.112/api-auth-be/post-chat-content3-5', {
        method: 'post',
        headers: aws_auth_header,
        body: JSON.stringify({ 'messages': [{ 'role': 'user', 'content': content }] })
      }).then(res => res.json())
    },
    robotHelp() {
      if ((this.questionForm.questionDescribe ==='' || this.questionForm.questionDescribe ===null )|| (this.questionForm.questionClues==='' || this.questionForm.questionClues===null)){
        this.$message.warning('请将问题信息输入完整')
      } else {
        this.active = 5
        this.questionForm.onerobotQuestion =''
        // console.log("this.questionForm.questionClues"+this.questionForm.questionClues)
        // 判断文本是中文为主还是英文为主
        const isChinese = /[\u4e00-\u9fa5]/.test(this.questionForm.questionClues); // 中文正则
        if (isChinese){
          this.questionForm.clues = '请根据下面这段话以一问一答的形式提一个问题并给出答案：'+this.questionForm.questionClues
        } else {
          this.questionForm.clues = 'Please ask a question and provide an answer in the form of a question and answer based on the following paragraph:'+this.questionForm.questionClues
        }
        console.log('clues:'+ this.questionForm.questionClues)
        // this.sendChatGPT(this.questionForm).then((res) =>{
        this.api(this.questionForm.clues).then((res) => {
          console.log(res)
          this.questionForm.onerobotQuestion = res
        })
      }

      // let data = {clue: this.questionForm.questionClues,answer:this.questionForm.questionAnswer}
      // Axios.post("http://222.198.118.211:9091/autoquestion",data).then((res) => {
      //   this.questionForm.robotQuestion = res.data.question
      // });
      // this.fullscreenLoading = true;
      // setTimeout(() => {
      //   this.fullscreenLoading = false;
      //   this.dialogFormVisible = true
      // }, 3000);
    },
    morerobotQuestion(){
      this.showmorerobotQuestion = true
      this.questionForm.morerobotQuestion = ''
      // 判断文本是中文为主还是英文为主
      const isChinese = /[\u4e00-\u9fa5]/.test(this.questionForm.questionClues); // 中文正则
      if (isChinese){
        this.questionForm.questionClues = '请根据下面这段话以一问一答的形式提出多个问题并给出答案：'+this.questionForm.questionClues
      } else {
        this.questionForm.questionClues = 'Please ask multiple questions and provide answers in the form of a question and answer based on the following paragraph:'+this.questionForm.questionClues
      }
      console.log('clues:'+ this.questionForm.questionClues)
      // this.sendChatGPT(this.questionForm).then((res) =>{
      //   console.log(res)
      //   this.questionForm.morerobotQuestion = res
      // })
      this.api(this.questionForm.clues).then((res) => {
        console.log(res)
        this.questionForm.morerobotQuestion = res
      })
    },
    modifyQuestion(){
      this.selectedObj = {}
      this.splitQuestion()
      this.showmodifyQuestion = true
    },
    handleObjChange() {
      this.selectedObj = Object.assign({}, this.selectedObj);
    },
    saveQuestion(){
      this.questionForm.modifyQuestion = this.selectedObj.question
      this.questionForm.questionAnswer = this.selectedObj.answer
      this.saveModifyQuestion = true
      this.active = 3

    },
    cancelQuestion(){
      this.active = 3
      this.selectedObj = {}
      this.questionForm.robotRate = ''
      this.questionForm.modifyQuestion = ''
      this.questionForm.questionAnswer = ''
      this.questionForm.suggestionhelp = ''
      this.questionForm.supplementClues = ''
      this.showmorerobotQuestion = false
      this.showmodifyQuestion = false
      this.saveModifyQuestion = false
    },
    submitQuestion() {
      this.$confirm('是否结束泛读，进入精读阶段?（您将不能重新修改问题）', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.active++
        this.$emit('active', this.active)
        this.$emit('next')
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消提交'
        })
      })
    },
    // handleChange(val) {
    //   console.log(val)
    // },
    // robotHelp() {
    //   let data = {clue: this.questionForm.questionClues,answer:this.questionForm.questionAnswer}
    //   Axios.post("http://222.198.118.211:9091/autoquestion",data).then((res) => {
    //     this.questionForm.robotQuestion = res.data.question
    //     // alert("机器问题为："+res.data.question)
    //   });
    //   this.fullscreenLoading = true;
    //   setTimeout(() => {
    //     this.fullscreenLoading = false;
    //     this.dialogFormVisible = true
    //   }, 3000);
    // },
    toParent: function() {
      this.$emit('greet')
    },
    timeCount() {
      // 会议计时
      let tempTime = 0
      let second = 0
      let minute = 0
      this.timeData = ''
      if (this.active == 0) {
        this.timer = setInterval(() => {
          tempTime += 1
          minute = parseInt(tempTime % 3600 / 60) // 转分钟
          second = parseInt(tempTime % 3600 % 60) // 转秒
          // 格式化
          minute = (minute > 9 ? minute : '0' + minute) + ''
          second = (second > 9 ? second : '0' + second) + ''

          this.timeData = minute + ':' + second
          this.readTime=parseInt(minute)*60+parseInt(second)
        }, 1000)
      } else {
        window.clearInterval(this.timer)
      }
    },
    createQuestion() {
      this.active = 3
      this.$emit('requestion')
      this.resetQuestionForm()
    },
    saveRobatInfo(){
      this.questionForm.questionDescribe = this.questionForm.modifyQuestion
      this.dialogFormVisible = false
    },
    cancelRobatInfo(){
      this.questionForm.modifyQuestion = ''
      this.questionForm.suggestionhelp = ''
    }
  }
}
</script>
<style>
.el-scrollbar__wrap{
  overflow-x: hidden;
}
</style>

