package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.BookMapper;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BookMapper bookMapper;


    @Override
    public User verify(User user) {
        String username=user.getUsername();
        String pwd= user.getPassword();
        user = userMapper.findUserByUserAndPwd(username, pwd);
        if(user!=null){
            return user;
        }else {
            return null;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userMapper.findUserByUsername(username);
        if(user!=null) {
            List<Integer> permissionList = userMapper.getUserPermission(user.getId());
            List<String> roles = new ArrayList<>();
            for (int i : permissionList) {
                if (i == 0) {
                    roles.add("student");
                } else if (i == 1) {
                    roles.add("teacher");
                }
            }
            //List<String> roles= Arrays.asList("admin","editor");
            user.setRoles(roles);
            return user;
        }
        return null;
    }

    @Override
    public UserClass findClassByUsername(String username) {
        UserClass userClass=userMapper.findClassByUsername(username);
        return userClass;
    }

    @Override
    public boolean updateUserInfo(String username, String name, String oldPassword, String newPassword) {
        //System.out.println(username+" : "+oldPassword);
        User user = userMapper.findUserByUserAndPwd(username, oldPassword);
        if(user!=null){
            //用户对象不为空，表示旧密码输入正确，允许用户更新密码
            userMapper.updateUserInfo(username,name,newPassword);
            return true;
        }else {
            //对象为空，密码输入错误
            return false;
        }
    }

    @Override
    public boolean insertKWLForm(KWL kwl) {
        int kwlId=userMapper.insertKWLForm(kwl);
        System.out.println(kwlId);
        if(kwlId!=0){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public int insertQuestionForm(QuestionForm question) {
        int questionId= userMapper.insertQuestionForm(question);
        System.out.println("questionid:"+questionId);
        return questionId;
    }

    @Override
    public void insertUserAnswer(int userId, int bookId) {
        userMapper.insertUserAnswer(userId,bookId);
    }

    @Override
    public List<Heat> getUserHeat(int userId) {
        List<Heat> userHeat = userMapper.getUserHeat(userId);
        return userHeat;
    }

    @Override
    public boolean insertUser(User user,String courseNum) {
        Integer courseId=userMapper.getCourseId(courseNum);
        if(courseId!=null || courseNum.isEmpty() ) {
            int id = userMapper.insertUser(user);
            if (id != 0) {
                //成功插入学生信息，继续插入学生权限信息
                int permissionId = 0;
                userMapper.insertUserPermission(user.getId(), permissionId);
                //如果学生号不为空，则继续将学生加入课程
                if(!courseNum.isEmpty()){
                    userMapper.insertUserCourse(user.getId(),courseId);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addCourse(User user, String courseNum) {
        Integer courseId=userMapper.getCourseId(courseNum);
        if (courseId!=null){
            userMapper.insertUserCourse(user.getId(),courseId);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<QuestionForm> getUserQuestions(int userid,Integer page,Integer pageSize, Integer bookId){
        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);
        List<QuestionForm> questionFormList = userMapper.getUserQuestions(userid,bookId);
        return questionFormList;
    }

    @Override
    public List<AnswerForm> getAnswerListByQuestionId(int questionid){

        List<AnswerForm> answerFormList = userMapper.getAnswerListByQuestionId(questionid);
        return answerFormList;
    }


}
