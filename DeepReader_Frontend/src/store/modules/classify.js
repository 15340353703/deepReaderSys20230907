import { getClassifyAndBookList } from '@/api/books'
import { getToken } from '@/utils/auth'
import { getUserGroupList, setUserGroup } from '@/api/stu_info'
import { getCourse,getCourseBybookid } from '@/api/books'

const mutations = {
  SET_CLASSIFYID: (state, classifyId) => {
    state.classifyId = classifyId
  },
  SET_CLASSIFYNAME: (state, classifyName) => {
    state.classifyName = classifyName
  },
  SET_BOOKLIST: (state, bookList) => {
    state.bookList = bookList
  }
}

export default {
  namespaced: true,
  state: {
    token: getToken(),
    title: '图书类别列表',
    classifyId: '',
    classifyName: '',
    bookList: []
  },
  mutations,
  actions: {
    getClassifyAndBookList({ commit }) {
      return new Promise((resolve, reject) => {
        getClassifyAndBookList().then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          const { classifyId, classifyName, bookList } = data
          commit('SET_CLASSIFYID', classifyId)
          commit('SET_CLASSIFYNAME', classifyName)
          commit('SET_BOOKLIST', bookList)
          console.log(data)
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    getCourse({ state },{page,pageSize}) {
      return new Promise((resolve, reject) => {
        getCourse(page, pageSize).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
          // location.reload();
        }).catch(error => {
          reject(error)
        })
      })
    },

    getCourseBybookid({ state },bookid) {
      console.log('bookidddd:'+bookid)
      return new Promise((resolve, reject) => {
        getCourseBybookid(bookid).then((response) => {
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
          // location.reload();
        }).catch(error => {
          reject(error)
        })
      })
    },

    getUserGroupList({ state }, { page, pageSize, courseid, groupsize }) {
      return new Promise((resolve, reject) => {
        getUserGroupList(page, pageSize, courseid, groupsize).then((response) => {
          console.log('response:'+response.data.list)
          const { data } = response
          if (!data) {
            reject('请求数据失败.')
          }
          resolve(data)
          // location.reload();
        }).catch(error => {
          reject(error+'获取数据失败')
        })
      })
    },

    setUserGroup({ state }, {courseid, groupsize}) {
      return new Promise((resolve, reject) => {
        setUserGroup(courseid,groupsize).then((response) => {
            console.log('新成员分组成功！')
          // location.reload();
        }).catch(error => {
          reject(error+'获取数据失败')
        })
      })
    },
  }
}

