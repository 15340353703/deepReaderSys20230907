package cn.ywrby.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.*;
import cn.ywrby.mapper.CourseMapper;
import cn.ywrby.mapper.GroupMapper;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.BookService;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.metadata.CallParameterMetaData;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/stu/clsInfo")
    public ResultResponse clsInfo(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");

            UserClass userClass = userService.findClassByUsername(username);
            res.setData(userClass);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return res;
    }

    @PostMapping("/stu/updateStuInfo")
    public ResultResponse updateStuInfo(@RequestBody String jsonStr){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(jsonStr);
        String name= (String) jsonObject.get("name");
        String oldPassword= (String) jsonObject.get("oldPassword");
        String newPassword= (String) jsonObject.get("newPassword");
        String token= (String) jsonObject.get("token");

        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //获取用户登录账户（学生号）
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            boolean result = userService.updateUserInfo(username, name, oldPassword, newPassword);
            if(result){
                //密码修改成功，返回更新后的相关值
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
                res.setData(name);
            }else {
                //密码错误，修改失败
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"用户名与密码不匹配");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录");
        }
        return res;

    }

    @GetMapping("/stu/group")
    public ResultResponse group(@RequestParam(required=true, defaultValue = "1")Integer page,
                                @RequestParam(required=false,defaultValue="10")Integer pageSize,
                                @RequestParam("courseid") Integer courseid,
                                @RequestParam("groupsize") Integer groupsize){
        ResultResponse res=new ResultResponse();
        Course c = courseMapper.getCourseById(courseid);

//        for(UserGroup userGroup: userGroupList){
//            if(userGroup.getUsername().indexOf(c.getCourseName())!=-1){
//                creategroup = false;
//                break;
//            }
//        }
        if(c.getHavegroup()!=1){
            List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);
            Collections.shuffle(userCourses);
            int groupnum = (int)Math.ceil((double)userCourses.size() / groupsize);
            int number = 0;
            for(int i=0;i<groupnum;i++){
                int count = 0;
                groupMapper.insertGroup(c.getCourseName()+'第'+i+'组');
                for(int j=number;j<userCourses.size();j++){
                    if(count!=0 && count % groupsize == 0){
                        break;
                    }
                    User u = userMapper.findUserById(userCourses.get(j).getUserId());
                    groupMapper.insertUsergroup(c.getCourseName()+'第'+i+'组',u.getUsername());
                    count += 1;
                    number += 1;
                }
            }
            courseMapper.setHavegroupById(courseid);
            List<UserGroup> userGroups = groupMapper.getUserGroupList();
            //获取分页插件对象
            PageHelper pageHelper=new PageHelper();
            //开始分页，指定分页参数
            PageMethod.startPage(page,pageSize);
            List<GroupForm> result = new ArrayList<>();
            for(UserGroup usergroup : userGroups){
                if (usergroup.getGroupname().contains(c.getCourseName())) {
                    GroupForm gf = new GroupForm();
                    gf.setGroupname(usergroup.getGroupname());
                    User u = userService.findUserByUsername(usergroup.getUsername());
                    gf.setUsername(usergroup.getUsername());
                    gf.setName(u.getYourname());
                    result.add(gf);
                }
            }
            //获取分页信息对象
            PageInfo<GroupForm> info=new PageInfo<GroupForm>(result);
            res.setData(info);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);

        } else {
            List<UserGroup> userGroupList = groupMapper.getUserGroupList();
            //获取分页插件对象
            PageHelper pageHelper=new PageHelper();
            //开始分页，指定分页参数
            PageMethod.startPage(page,pageSize);
            List<GroupForm> result = new ArrayList<>();
            for(UserGroup userGroup: userGroupList) {
                if (userGroup.getGroupname().contains(c.getCourseName())) {
                    GroupForm gf = new GroupForm();
                    gf.setGroupname(userGroup.getGroupname());
                    User u = userService.findUserByUsername(userGroup.getUsername());
                    try{
                        gf.setUsername(userGroup.getUsername());
                        gf.setName(u.getYourname());
                    }catch (Exception e){
                        System.out.println("没有此人");
                    }

                    result.add(gf);
                }
            }
            //获取分页信息对象
            PageInfo<GroupForm> info=new PageInfo<GroupForm>(result);
            res.setData(info);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }
        return res;
    }

    @GetMapping("/stu/setusergroup")
    public ResultResponse getUserGroup(@RequestParam("courseid") Integer courseid,@RequestParam("groupsize") Integer groupsize){
        ResultResponse res=new ResultResponse();
        Course c = courseMapper.getCourseById(courseid);
        List<UserGroup> userGroups = groupMapper.getUserGroupList();

        int order = userGroups.size() / groupsize;
        if(userGroups.size() % groupsize != 0){
            order += 2;
        }
        List<User> ungroupusers = new ArrayList<>();
        List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);

        for(UserCourse usercourse:userCourses){
            User u = userMapper.findUserById(usercourse.getUserId());
            int count = 0;
            for(UserGroup userGroup:userGroups){
                count+=1;
                if(u.getUsername().equals(userGroup.getUsername()) && userGroup.getGroupname().contains(c.getCourseName())){
                    break;
                }
                if(count == userGroups.size()){
                    ungroupusers.add(u);
                }
            }

        }
        System.out.println("新成员信息："+ungroupusers);
        if(ungroupusers.size()!=0){
            int groupnum = (int)Math.ceil((double)ungroupusers.size() / groupsize);
            System.out.println("新成员的组数："+groupnum);
            int number = 0;
            for(int i=order;i<groupnum+order;i++){
                int count = 0;
                groupMapper.insertGroup(c.getCourseName()+'第'+i+'组');
                for(int j=number;j<ungroupusers.size();j++){
                    if(count!=0 && count % groupsize == 0){
                        break;
                    }
                    User u = userMapper.findUserById(ungroupusers.get(j).getId());
                    groupMapper.insertUsergroup(c.getCourseName()+'第'+i+'组',u.getUsername());
                    count += 1;
                    number += 1;
                }
            }
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        } else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/stu/getUsersByCourseId")
    public ResultResponse getUsersByCourse(@RequestParam("courseid") Integer courseid) {
        ResultResponse res=new ResultResponse();
        if(courseid != 0){
            List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);
            List<User> users = new ArrayList<>();
            for(UserCourse uc : userCourses){
                User u = userMapper.findUserById(uc.getUserId());
                users.add(u);
            }
            res.setData(users);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else{
            List<User> users = userMapper.findAll();
            res.setData(users);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }
        return res;
    }

    @GetMapping("/stu/getUserArticles")
    public ResultResponse getUserBooks(@RequestParam("courseid") Integer courseid) {
        ResultResponse res=new ResultResponse();
        if(courseid != 0){
            List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);
            List<User> users = new ArrayList<>();
            for(UserCourse uc : userCourses){
                User u = userMapper.findUserById(uc.getUserId());
                users.add(u);
            }
            List<Integer> userArticleCount = new ArrayList<>();
            for(User u: users){
                List<KWL> kwls = userMapper.getKWLsByUserid(u.getId());
                userArticleCount.add(kwls.size());
            }
            res.setData(userArticleCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else{
            List<Integer> userArticleCount = new ArrayList<>();
            List<User> users = userMapper.findAll();
            for(User u: users){
                List<KWL> kwls = userMapper.getKWLsByUserid(u.getId());
                userArticleCount.add(kwls.size());
            }
            res.setData(userArticleCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }


}
