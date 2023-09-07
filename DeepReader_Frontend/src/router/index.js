import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
import componentsRouter from './modules/components'
import chartsRouter from './modules/charts'
import tableRouter from './modules/table'
import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/regist',
    component: () => import('@/views/regist/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/dashboard/index'),
        name: '主页',
        meta: { title: '主页', icon: 'dashboard', noCache: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: 'Profile', icon: 'user', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true },

  {
    path: '/uploadFile',
    component: Layout,
    hidden: true,
    meta: { roles: ['teacher'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/uploadFile/index'),
        name: '上传文章',
        meta: { title: '上传文章', icon: 'pdf', noCache: true }
      }
    ]
  },
  {
    path: '/group',
    component: Layout,
    hidden: true,
    meta: { roles: ['teacher'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/group/index'),
        name: '课程',
        meta: { title: '课程', icon: 'info', noCache: true }
      }
    ]
  },

  {
    path: '/groupstudent',
    component: Layout,
    hidden: true,
    meta: { roles: ['teacher'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/groupstudent/index'),
        name: '学生分组',
        meta: { title: '学生分组', icon: 'info', noCache: true }
      }
    ]
  },

  {
    path: '/createCourse',
    component: Layout,
    meta: { roles: ['teacher'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/createCourse/index'),
        name: '创建课程',
        meta: { title: '创建课程', icon: 'pdf', noCache: true }
      }
    ]
  },
  { path: '/task',
    component: Layout,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/task/index'),
        name: '阅读任务',
        meta: { title: '阅读任务', icon: 'skill', noCache: true }
      }
    ]
  },

  {
    path: '/bookSelection',
    component: Layout,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/bookSelection/index'),
        name: '阅读文章',
        meta: { title: '阅读文章', icon: 'documentation', noCache: true }
      }
    ]
  },
  // {
  //   path: '/testForQuestion',
  //   component: Layout,
  //   meta: { roles: ['student'] },
  //   children: [
  //     {
  //       path: 'index',
  //       component: () => import('@/views/testForQuestion/index'),
  //       name: '测试样例',
  //       meta: { title: '测试样例', icon: 'documentation', noCache: true }
  //     }
  //   ]
  // },

  {
    path: '/study',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/study/index'),
        name: '教育技术资讯',
        meta: { title: '教育技术资讯', icon: 'icon', noCache: true }
      }
    ]
  },
  { path: '/study/components',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/study/components/index'),
        name: '开始阅读',
        meta: { title: '开始阅读', icon: 'documentation', noCache: true }
      }
    ]
  },
  // {
  //   path: '/study',
  //   component: Layout,
  //   hidden: true,
  //   meta: { roles: ['student'] },
  //   children: [
  //     {
  //       path: 'index',
  //       component: () => import('@/views/study/index/StartRead'),
  //       name: '开始阅读',
  //       meta: { title: '开始阅读', icon: 'icon', noCache: true }
  //     }
  //   ]
  // },
  {
    path: '/studyLook',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/studyLook/index'),
        name: '查看文章',
        meta: { title: '查看文章', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/studyEvaluation',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/studyEvaluation/index'),
        name: '同伴反馈评价',
        meta: { title: '同伴反馈评价', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/questionEvaluation',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/questionEvaluation/index'),
        name: '问题评价详情',
        meta: { title: '问题评价详情', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/studyQuestion',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/studyQuestion/index'),
        name: '问题反馈报告',
        meta: { title: '问题反馈报告', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/askQuestion',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/askQuestion/index'),
        name: '创建测试题',
        meta: { title: '创建测试题', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/claimQuestion',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/claimQuestion/index'),
        name: '考考同伴',
        meta: { title: '考考同伴', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/examSelection',
    component: Layout,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/examSelection/index'),
        name: '他问我答',
        meta: { title: '他问我答', icon: 'edit', noCache: true }
      }
    ]
  },
  {
    path: '/exam',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/exam/index'),
        name: '测试',
        meta: { title: '测试', icon: 'icon', noCache: true }
      }
    ]
  },
  {
    path: '/allQuestion',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/allQuestion/index'),
        name: '全部习题',
        meta: { title: '全部习题', icon: 'icon', noCache: true }
      }
    ]
  },

  {
    path: '/allEvaluation',
    component: Layout,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/allEvaluation/index'),
        name: '深度提问',
        meta: { title: '深度提问', icon: 'edit', noCache: true }
      }
    ]
  },

  {
    path: '/discussionBoard',
    component: Layout,
    meta: { roles: ['student', 'teacher'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/discussionBoard/index'),
        name: '讨论区',
        meta: { title: '讨论区', icon: 'education', noCache: true }
      }
    ]
  },

  {
    path: '/discussion',
    component: Layout,
    hidden: true,
    meta: { roles: ['student', 'teacher'] },
    children: [
      {
        path: 'index/:id',
        component: () => import('@/views/discussion/index/'),
        name: '讨论',
        meta: { title: '讨论', icon: 'icon', noCache: true }
      }
    ]
  },
  { path: '/pizhufankui',
    component: Layout,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/pizhufankui/index'),
        name: '批注反馈',
        meta: { title: '批注反馈', icon: 'guide', noCache: true }
      }
    ]
  },
  { path: '/pizhufankui/components',
    component: Layout,
    hidden: true,
    meta: { roles: ['student'] },
    children: [
      {
        path: 'index',
        component: () => import('@/views/pizhufankui/components/index'),
        name: '反馈内容',
        meta: { title: '反馈内容', icon: 'documentation', noCache: true }
      }
    ]
  }
  // {
  //   path: '/pdfdemo',
  //   component: Layout,
  //   // hidden: true,
  //   meta: { roles: ['student', 'teacher'] },
  //   hidden:true,
  //   children: [
  //     {
  //       path: 'index/:id',
  //       component: () => import('@/views/pdf-demo/HelloWorld'),
  //       name: 'pdf案例',
  //       meta: { title: 'pdf案例', icon: 'icon', noCache: true }
  //     }
  //   ]
  // },
]

/*
const User = {
  template: '<div>Hello World</div>'
}
export const myRoutes=[
  // 动态路径参数 以冒号开头
  { path: '/user/:id', component: User }
]
*/

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
