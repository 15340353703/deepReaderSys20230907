package cn.ywrby.service;


import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserService {

    /**
     * 验证成功，则返回VoUser对象
     * 验证失败，返回NULL或抛出报错
     * @param user
     * @return
     */
    public User verify(User user);

    /**
     * 根据校园码获取学生基本信息
     * @param username
     * @return
     */
    public User findUserByUsername(String username);

    public UserClass findClassByUsername(String username);

    /**
     * 更新学生用户基本信息
     * @param name 学生姓名
     * @param oldPassword 旧密码，用于验证学生身份
     * @param newPassword 新密码
     * @return
     */
    public boolean updateUserInfo(String username, String name, String oldPassword, String newPassword);

    boolean insertKWLForm(KWL kwl);

    int insertQuestionForm(QuestionForm question);

    void insertUserAnswer(int userId, int bookId);

    List<Heat> getUserHeat(int userId);

    boolean insertUser(User user,String courseNum);

    boolean addCourse(User user, String courseNum);

    public List<QuestionForm> getUserQuestions(int userid,Integer page,Integer pageSize, Integer bookId);

    public List<AnswerForm> getAnswerListByQuestionId(int questionid);
}
