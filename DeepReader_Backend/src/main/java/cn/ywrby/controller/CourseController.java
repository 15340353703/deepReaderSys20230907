package cn.ywrby.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.*;
import cn.ywrby.mapper.BookMapper;
import cn.ywrby.mapper.ClassifyMapper;
import cn.ywrby.mapper.CourseMapper;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.BookService;
import cn.ywrby.service.CourseService;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    BookService bookService;

    @Autowired
    ClassifyMapper classifyMapper;

    @Autowired
    BookMapper bookMapper;


    @PostMapping("/course/deleteClassifyInfo")
    public ResultResponse deleteClassifyInfo(@RequestParam("token") String token,@RequestParam("id") int id){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            boolean result = courseService.deleteClassifyInfo(id);
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"提交失败，请重新提交");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }



    @PostMapping("/course/updateClassifyInfo")
    public ResultResponse updateClassifyInfo(@RequestParam("token") String token,@RequestParam("classifyForm") Object classifyForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(classifyForm);
        int id = (int)jsonObject.get("id");
        String work = (String)jsonObject.get("work");
        int courseId = (int)jsonObject.get("courseId");
        int displayMode = (int)jsonObject.get("displayMode");
        JSONArray studyDate= (JSONArray) jsonObject.get("studyDate");
        Timestamp start_date;
        Timestamp end_date;
        try {
            start_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(0)).getTime());
            end_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(1)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            start_date=new Timestamp(new Date().getTime());
            end_date=new Timestamp(new Date().getTime());
        }


        Classify classify = new Classify();
        classify.setId(id);
        classify.setCourseId(courseId);
        classify.setWork(work);
        classify.setDisplayMode(displayMode);
        classify.setStart_date(start_date);
        classify.setEnd_date(end_date);

        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            boolean result = courseService.updateClassifyInfo(classify);
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"提交失败，请重新提交");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;

    }


    @GetMapping("/course/getClassifyList")
    public ResultResponse getClassifyList(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            //调用service层方法获得用户图书列表
            List<Classify> classifyList = courseService.getClassifyList(user.getId());

            res.setData(classifyList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return  res;
    }


    @PostMapping("/course/deleteCourseInfo")
    public ResultResponse deleteCourseInfo(@RequestParam("token") String token,@RequestParam("courseId") int courseId){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
//        JSONObject jsonObject= JSONUtil.parseObj(courseId);
//        int id = (int) jsonObject.get("id");

        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            boolean result = courseService.deleteCourseInfo(courseId);
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"提交失败，请重新提交");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }

    @PostMapping("/course/updateCourseInfo")
    public ResultResponse updateCourseInfo(@RequestParam("token") String token,@RequestParam("courseForm") Object courseForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(courseForm);
        int id = (int) jsonObject.get("id");
        int userId = (int) jsonObject.get("userId");
        String courseNum = (String)jsonObject.get("courseNum");
        String courseName = (String)jsonObject.get("courseName");
        String instructorName = (String)jsonObject.get("instructorName");
        String courseContent = (String)jsonObject.get("courseContent");
        String groupnameHP = (String )jsonObject.get("groupnameHP");

        Course course = new Course();
        course.setId(id);
        course.setUserId(userId);
        course.setCourseNum(courseNum);
        course.setCourseName(courseName);
        course.setInstructorName(instructorName);
        course.setCourseContent(courseContent);
        course.setGroupnameHP(groupnameHP);
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            boolean result = courseService. updateCourseInfo(course);
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"提交失败，请重新提交");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }

    @GetMapping("/course/userCourse")
    public ResultResponse getUserCourse(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //调用service层方法获得用户图书列表
            List<Course> userCourse = courseService.getUserCourse(user);
            res.setData(userCourse);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/course/getCourseBybookid")
    public ResultResponse getUserCourseBybookid(@RequestParam("bookid") Integer bookid){
        ResultResponse res=new ResultResponse();
        System.out.println("图书编号："+bookid);
        //调用service层方法获得用户图书列表
        List<Course> courseList = courseMapper.getUserCourseBybookid(bookid);
        System.out.println("课程："+courseList);
        res.setData(courseList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);

        return res;
    }

    @GetMapping("/course/getCourseNum")
    public ResultResponse getCourseNum(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //调用service层方法获得用户图书列表
            List<Course> courseList = courseService.getCourseNum(user);
            res.setData(courseList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/course/getCourseList")
    public ResultResponse getCourseList(){
        ResultResponse res=new ResultResponse();
        List<Course> courseList=courseMapper.getCourseList();
        res.setData(courseList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/course/getCourse")
    public ResultResponse getCourse(@RequestParam(required=true, defaultValue = "1")Integer page, @RequestParam(required=false,defaultValue="10")Integer pageSize){
        ResultResponse res=new ResultResponse();
        List<Course> courseList=courseService.getCourse(page,pageSize);
        //获取分页信息对象
        PageInfo<Course> info=new PageInfo<Course>(courseList);
        System.out.println(info);
        res.setData(info);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @PostMapping("/course/updateClassify")
    public ResultResponse updateClassify(@RequestParam("token") String token,@RequestParam("classForm") String classForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(classForm);
        String classifyName= (String) jsonObject.get("classifyName");
        String displayModeStr= (String) jsonObject.get("displayMode");
        JSONArray studyDate= (JSONArray) jsonObject.get("studyDate");
        int displayMode;
        try {
            displayMode=Integer.parseInt(displayModeStr);
        }catch (NumberFormatException e){
            displayMode=1;
        }
        int courseId=(int) jsonObject.get("courseId");

        Timestamp start_date;
        Timestamp end_date;
        try {
            start_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(0)).getTime());
            end_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(1)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            start_date=new Timestamp(new Date().getTime());
            end_date=new Timestamp(new Date().getTime());
        }
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //获取用户登录账户
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);

            Integer classifyId=courseService.insertClassify(classifyName,displayMode,courseId,start_date,end_date);
            //boolean result=true;
            if(classifyId!=0){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"创建失败，请重新创建作业");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }


    @PostMapping("/course/updateCourse")
    public ResultResponse updateCourse(@RequestParam("token") String token,@RequestParam("courseForm") String courseForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(courseForm);
        String courseName= (String) jsonObject.get("name");
        String instructorName= (String) jsonObject.get("instructorName");
        String courseContent=(String) jsonObject.get("courseContent");
        String groupnameHP = (String) jsonObject.get("groupnameHP") ;


        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //获取用户登录账户
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);

            //Integer classifyId=courseService.insertClassify(classifyName,displayMode,courseId);
            String courseNum=courseService.insertCourse(user,courseName,instructorName,courseContent,groupnameHP);
            //boolean result=true;
            if(courseNum!=null){
                res.setCode(Constants.STATUS_OK);
                //将生成的课程码返回给前段供学生教师使用
                res.setData(courseNum);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"创建失败，请重新创建作业");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }

//    2023年8月10日更新
    @GetMapping("/course/getClassifyListAndBook")
    public ResultResponse getClassifyListAndBook(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            //调用service层方法获得用户图书列表
            List<Classify> classifyList = courseService.getClassifyList2(user.getId());
            System.out.print("classifyList:"+classifyList);
            List<Book> bookList = new ArrayList<>();
            List<Classify> classifyAllList = new ArrayList<>();
            for(Classify classify : classifyList){
                bookList=bookService.getBookByClassifyId(classify.getId());
                classify.setChildren(bookList);
//                classify.setStart_date(classify.getStart_date());
                System.out.print("classify:"+classify);
                classifyAllList.add(classify);
                System.out.print("classifyAllList:"+classifyAllList);
            }
//            System.out.print("classifyAllList:"+classifyAllList);
            res.setData(classifyAllList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return  res;
    }

    @GetMapping("/course/getClassifyListAndBookByCourse")
    public ResultResponse getClassifyListAndBookByCourse(@RequestParam("courseId") Integer courseId) {
        ResultResponse res = new ResultResponse();
        System.out.println("课程Id："+courseId);
        List<Classify> classifyList = classifyMapper.getClassifyListByCourseId(courseId);
        List<Classify> classifyAllList = new ArrayList<>();
        for(Classify classify : classifyList) {
            List<Book> bookList = bookService.getBookByClassifyId(classify.getId());
            classify.setChildren(bookList);
            System.out.print("classify:" + classify);
            classifyAllList.add(classify);
            System.out.println(classifyList);
        }
        res.setData(classifyAllList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);

        return  res;
    }

    @GetMapping("/course/getUsersReadProgressByCourseId")
    public ResultResponse getUsersHaveReadByCourseId(@RequestParam("token") String token,@RequestParam("courseId") Integer courseId){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            System.out.println("课程Id："+courseId);
            if(courseId!=0) {
                List<Classify> classifyList = classifyMapper.getClassifyListByCourseId(courseId);
                List<Classify> classifyAllList = new ArrayList<>();
                //获取该用户已阅读图书ID列表
                List<Integer> haveReadList = bookMapper.getUserHaveReadList(user.getId());

                for (Classify classify : classifyList) {
                    List<Book> bookList = bookService.getBookByClassifyId(classify.getId());
                    //查询用户阅读过哪些图书并标记
                    for (Book book : bookList) {
                        for (int bookId : haveReadList) {
                            if (book.getId() == bookId) {
                                book.setHave_read(true);
                            }
                        }
                    }
                    classify.setChildren(bookList);
                    classifyAllList.add(classify);
                    System.out.println(classifyList);
                }
                System.out.print("classifyAllList.children:" + classifyAllList);
                res.setData(classifyAllList);
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
            }else {
                res.setData(null);
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
            }


        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;



    }



}
