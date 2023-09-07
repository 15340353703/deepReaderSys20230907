import { getUserBookList, getUserBookListByCourse } from '@/api/user'
import {getbookInfoById, getBookList, getBookContent, getBookInfo, getClassify, getBookByClassifyId, uploadBook,getCourse,updateClassify ,updateCourse,getCourseList, getBookInfoByClassifyId, updateBookInfo, deleteBookInfo, getQuestionsBybook} from '@/api/books'
import { getToken } from '@/utils/auth'

const mutations = {
  SET_BOOKID: (state, bookId) => {
    state.bookId = bookId
  },
  SET_BOOKNAME: (state, bookName) => {
    state.bookName = bookName
  },
  SET_AUTHOR: (state, author) => {
    state.author = author
  },
  SET_PUBLISHINGHOUSE: (state, publishingHouse) => {
    state.publishingHouse = publishingHouse
  },
  SET_PUBLISHDATE: (state, publishDate) => {
    state.publishDate = publishDate
  },
  SET_PAGES: (state, pages) => {
    state.pages = pages
  },
  SET_ISBN: (state, ISBN) => {
    state.ISBN = ISBN
  },
  SET_PRICE: (state, price) => {
    state.price = price
  },
  SET_BRIEFINTRODUCTION: (state, briefIntroduction) => {
    state.briefIntroduction = briefIntroduction
  },
  SET_AUTHORINTRODUCTION: (state, authorIntroduction) => {
    state.authorIntroduction = authorIntroduction
  },
  SET_CONTENT: (state, content) => {
    state.content = content
  }
}

export default {
  namespaced: true,
  state: {
    token: getToken(),
    title: '学生图书列表',
    bookId: '',
    bookName: '',
    author: '',
    publishingHouse: '',
    publishDate: '',
    pages: '',
    ISBN: '',
    price: '',
    briefIntroduction: '',
    authorIntroduction: '',
    content: ''
  },
  mutations,
  actions: {

    getUserBookList({ state }, { page, pageSize }) {
      return new Promise((resolve, reject) => {
        getUserBookList(state.token, page, pageSize).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getUserBookListByCourse({ state }, { page, pageSize, courseId }) {
      return new Promise((resolve, reject) => {
        getUserBookListByCourse(state.token, page, pageSize, courseId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getBookList({ commit }, { page, pageSize }) {
      return new Promise((resolve, reject) => {
        getBookList(page, pageSize).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          const { bookId, bookName, author, publishingHouse, publishDate, pages, ISBN, price, briefIntroduction, authorIntroduction } = data
          commit('SET_BOOKID', bookId)
          commit('SET_BOOKNAME', bookName)
          commit('SET_AUTHOR', author)
          commit('SET_PUBLISHINGHOUSE', publishingHouse)
          commit('SET_PUBLISHDATE', publishDate)
          commit('SET_PAGES', pages)
          commit('SET_ISBN', ISBN)
          commit('SET_PRICE', price)
          commit('SET_BRIEFINTRODUCTION', briefIntroduction)
          commit('SET_AUTHORINTRODUCTION', authorIntroduction)
          console.log('getBookList: ' + 'right-major')
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getBookContent({ commit }, bookId) {
      return new Promise((resolve, reject) => {
        getBookContent(bookId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          const { content } = data
          commit('SET_CONTENT', content)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getBookByClassifyId({ commit }, classifyId) {
      return new Promise((resolve, reject) => {
        getBookByClassifyId(classifyId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getBookInfo({ commit }, bookId) {
      return new Promise((resolve, reject) => {
        getBookInfo(bookId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          const { content } = data
          commit('SET_CONTENT', content)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getClassify({ commit }) {
      return new Promise((resolve, reject) => {
        getClassify().then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },


    getCourse({ commit },{page,pageSize}) {
      return new Promise((resolve, reject) => {
        getCourse(page,pageSize).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getCourseList({ commit }) {
      return new Promise((resolve, reject) => {
        getCourseList().then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    uploadBook({ commit, state }, bookForm) {
      return new Promise((resolve, reject) => {
        uploadBook(state.token, bookForm).then(response => {
          // alert("success: KWL表格提交成功!")
          // location.reload();
        }).catch(error => {
          reject(error)
        })
      })
    },


    updateClassify({ commit, state }, classForm) {
      return new Promise((resolve, reject) => {
        updateClassify(state.token, classForm).then(response => {
          // alert("success: KWL表格提交成功!")
          // location.reload();
        }).catch(error => {
          reject(error)
        })
      })
    },


    updateCourse({ commit, state }, courseForm) {
      return new Promise((resolve, reject) => {
        updateCourse(state.token, courseForm).then(response => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getBookInfoByClassifyId({ state }) {
      return new Promise((resolve, reject) => {
        getBookInfoByClassifyId(state.token).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    updateBookInfo({ commit, state }, bookForm) {
      return new Promise((resolve, reject) => {
        updateBookInfo(state.token, bookForm).then((response) => {
          // location.reload()
          const { code } = response
          if (!code) {
            reject('请求数据失败.')
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    deleteBookInfo({ state }, id) {
      return new Promise((resolve, reject) => {
        deleteBookInfo(state.token, id).then((response) => {
          const { code } = response
          if (!code) {
            reject('请求数据失败.')
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getQuestionsBybook({ commit }, bookId) {
      return new Promise((resolve, reject) => {
        getQuestionsBybook(bookId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getbookInfoById({ commit }, bookId) {
      return new Promise((resolve, reject) => {
        getbookInfoById(bookId).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

  }
}
