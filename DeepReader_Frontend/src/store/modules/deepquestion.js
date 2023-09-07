import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import { getCourseNum, login } from '@/api/user'
import { submitfeedback } from '@/api/stu_info'

const state = {
  token: getToken(),
  questionfeedback: null,
  readfeedback: '',
  questionfeedbackfeeling: '',
  questionfeedbackusefulrate: null,
  questionfeedbackusefulcause: '',
  annotationfeedbackfeeling: '',
  annotationfeedbackusefulrate: null,
  annotationfeedbackusefulcause: '',
  summary: [],
  questionform: { type: '', question: '', answer: '', clue: '' }
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_QUESTIONFEEDBACK: (state, questionfeedback) => {
    state.questionfeedback = questionfeedback
  },
  SET_QUESTIONFEEDBACKFEELING: (state, questionfeedbackfeeling) => {
    state.questionfeedbackfeeling = questionfeedbackfeeling
  },
  SET_QUESTIONFEEDBACKUSEFULRATE: (state, questionfeedbackusefulrate) => {
    state.questionfeedbackusefulrate = questionfeedbackusefulrate
  },
  SET_QUESTIONFEEDBACKUSEFULCAUSE: (state, questionfeedbackusefulcause) => {
    state.questionfeedbackusefulcause = questionfeedbackusefulcause
  },
  SET_ANNOTATIONFEEDBACKFEELING: (state, questionfeedbackfeeling) => {
    state.questionfeedbackfeeling = questionfeedbackfeeling
  },
  SET_ANNOTATIONFEEDBACKUSEFULRATE: (state, questionfeedbackusefulrate) => {
    state.questionfeedbackusefulrate = questionfeedbackusefulrate
  },
  SET_ANNOTATIONFEEDBACKUSEFULCAUSE: (state, questionfeedbackusefulcause) => {
    state.questionfeedbackusefulcause = questionfeedbackusefulcause
  },
  SET_SUMMARY: (state, summary) => {
    state.summary= summary
  },
  SET_QUESTIONFORM: (state, questionform) => {
    state.questionform = questionform
  },
}

const actions = {
  submitfeedback({ state }) {
    return new Promise((resolve, reject) => {
      submitfeedback(state.token).then((response) => {
        const { data } = response
        if (!data) {
          reject('请求数据失败.')
        }
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
