import request from '@/utils/request'

export function getClassifyAndBookList() {
  return request({
    url: '/book/classifyAndBookList',
    method: 'get'
  })
}

export function getBookList(page, pageSize) {
  return request({
    url: '/book/list',
    method: 'get',
    params: { page, pageSize }
  })
}

export function getBookContent(bookId) {
  return request({
    url: '/book/content',
    method: 'get',
    params: { bookId }
  })
}

export function getBookInfo(bookId) {
  return request({
    url: '/book/info',
    method: 'get',
    params: { bookId }
  })
}

export function getClassify() {
  return request({
    url: '/book/classify',
    method: 'get'
  })
}

export function getCourse(page,pageSize) {
  return request({
    url: '/course/getCourse',
    method: 'get',
    params:{page,pageSize}
  })
}

export function getCourseBybookid(bookid) {
  return request({
    url: '/course/getCourseBybookid',
    method: 'get',
    params:{bookid}
  })
}

export function getCourseList() {
  return request({
    url: '/course/getCourseList',
    method: 'get',
  })
}

export function getBookByClassifyId(classifyId) {
  return request({
    url: '/book/getBookByClassify',
    method: 'get',
    params: { classifyId }
  })
}


export function uploadBook(token, bookForm) {
  return request({
    url: '/book/uploadBook',
    method: 'post',
    params: { token, bookForm }
  })
}


export function updateClassify(token, classForm) {
  return request({
    url: '/course/updateClassify',
    method: 'post',
    params: { token, classForm }
  })
}



export function updateCourse(token, courseForm) {
  return request({
    url: '/course/updateCourse',
    method: 'post',
    params: { token, courseForm }
  })
}

export function getBookInfoByClassifyId(token) {
  return request({
    url: '/book/getBookInfoByClassifyId',
    method: 'get',
    params: { token }
  })
}
export function updateBookInfo(token, bookForm) {
  return request({
    url: '/book/updateBookInfo',
    method: 'post',
    params: { token, bookForm }
  })
}

export function deleteBookInfo(token, id) {
  return request({
    url: '/book/deleteBookInfo',
    method: 'post',
    params: { token, id }
  })
}

export function getQuestionsBybook(bookId) {
  return request({
    url: '/dq/getQuestionsBybook',
    method: 'get',
    params: { bookId }
  })
}

export function getbookInfoById(bookId) {
  return request({
    url: '/book/info',
    method: 'get',
    params: { bookId }
  })
}
