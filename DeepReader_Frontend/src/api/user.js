import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function regist(registForm) {
  return request({
    url: '/user/regist',
    method: 'post',
    params: { registForm }
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function addCourse(token,courseNum) {
  return request({
    url: '/user/addCourse',
    method: 'post',
    params: { token,courseNum }
  })
}

export function getUserBookList(token, page, pageSize) {
  return request({
    url: '/user/booklist',
    method: 'get',
    params: { token, page, pageSize }
  })
}

export function getUserKWLs(token) {
  return request({
    url: '/user/userkwl',
    method: 'get',
    params: { token }
  })
}

export function getUserBookListByCourse(token, page, pageSize, courseId) {
  return request({
    url: '/user/booklistByCourse',
    method: 'get',
    params: { token, page, pageSize, courseId }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function updateUserKWLForm(token, KWLForm) {
  return request({
    url: '/user/updateUserKWLForm',
    method: 'post',
    params: { token, KWLForm }
  })
}

export function updateQuestionForm(token, questionForm) {
  return request({
    url: '/user/updateQuestionForm',
    method: 'post',
    params: { token, questionForm }
  })
}

export function userDataCount(token) {
  return request({
    url: '/log/userDataCount',
    method: 'get',
    params: { token }
  })
}
export function updateUserHypothesis(token,hypothesisName) {
  return request({
    url: '/user/updateUserHypothesis',
    method: 'post',
    params: { token,hypothesisName }
  })
}

export function getUserHeat(token) {
  return request({
    url: '/user/heat',
    method: 'get',
    params: { token }
  })
}

export function getUserCourse(token) {
  return request({
    url: '/course/userCourse',
    method: 'get',
    params: { token }
  })
}

export function getCourseNum(token) {
  return request({
    url: '/course/getCourseNum',
    method: 'get',
    params: { token }
  })
}

export function getUserQuestions(token, page, pageSize, bookId) {
  return request({
    url: '/user/userQuestions',
    method: 'get',
    params: { token, page, pageSize, bookId }
  })
}

export function updateCourseInfo(token, courseForm) {
  return request({
    url: '/course/updateCourseInfo',
    method: 'post',
    params: { token, courseForm }
  })
}

export function deleteCourseInfo(token, courseId) {
  return request({
    url: '/course/deleteCourseInfo',
    method: 'post',
    params: { token, courseId }
  })
}

export function getClassifyInfo(token) {
  return request({
    url: '/course/getClassifyList',
    method: 'get',
    params: { token }
  })
}

export function getBookInfoList(token) {
  return request({
    url: '/book/getBookInfoList',
    method: 'get',
    params: { token }
  })
}

export function updateClassifyInfo(token, classifyForm) {
  return request({
    url: '/course/updateClassifyInfo',
    method: 'post',
    params: { token, classifyForm }
  })
}

export function deleteClassifyInfo(token, id) {
  return request({
    url: '/course/deleteClassifyInfo',
    method: 'post',
    params: { token, id }
  })
}

export function sendChatGPT(token,questionForm) {
  return request({
    url: '/user/sendChatGPT',
    method: 'post',
    params: { token,questionForm }
  })
}

