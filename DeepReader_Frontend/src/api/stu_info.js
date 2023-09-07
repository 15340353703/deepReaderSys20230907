import request from '@/utils/request'

export function getStuClsInfo(token) {
  return request({
    url: '/stu/clsInfo',
    method: 'get',
    params: { token }
  })
}

export function updateStuInfo(data) {
  return request({
    url: '/stu/updateStuInfo',
    method: 'post',
    data
  })
}

export function getQuestionFormByBookId(token, bookId) {
  return request({
    url: '/dq/getQuestionFormByBookId',
    method: 'get',
    params: { token, bookId }
  })
}

export function getAnswerForm(userId, bookId) {
  return request({
    url: '/dq/getAnswerFormByUserId',
    method: 'get',
    params: { userId, bookId }
  })
}

export function submitAnswer(token,answerForm) {
  return request({
    url: '/dq/submitAnswer',
    method: 'post',
    params: { token,answerForm }
  })
}

export function submitfeedback(token,feedbackForm) {
  return request({
    url: '/dq/submitfeedback',
    method: 'post',
    params: { token,feedbackForm }
  })
}

export function viewBook(token,bookId) {
  return request({
    url: '/log/viewBook',
    method: 'get',
    params: { token,bookId }
  })
}

export function viewBookAtExam(token,bookId) {
  return request({
    url: '/log/viewBookAtExam',
    method: 'get',
    params: { token,bookId }
  })
}

export function getCourseList(page, pageSize) {
  return request({
    url: '/course/getCourse',
    method: 'get',
    params: { page, pageSize }
  })
}

export function getUserGroupList(page, pageSize, courseid, groupsize) {
  return request({
    url: '/stu/group',
    method: 'get',
    params: { page, pageSize, courseid, groupsize }
  })
}

export function setUserGroup(courseid, groupsize) {
  return request({
    url: '/stu/setusergroup',
    method: 'get',
    params: { courseid, groupsize }
  })
}

export function getUserCourseAnswerForm(token, courseId) {
  return request({
    url: '/dq/getUserCourseAnswerForm',
    method: 'get',
    params: { token, courseId }
  })
}

