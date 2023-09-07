package cn.ywrby.mapper;

import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 获取所有用户的数据
     * @return 所有用户数据组成的集合
     */
    public List<User> findAll() ;

    /**
     * 通过用户的校园号与密码验证是否存在用户并返回，主要用于登录
     * @return
     */
    public User findUserByUserAndPwd(@Param("username") String username, @Param("pwd") String pwd);


    public User findUserByUsername(@Param("username") String username);

    /**
     * 利用校园号找到对应班级
     * @param username
     * @return
     */
    public UserClass findClassByUsername(String username);

    /**
     * 更新用户基本信息
     */
    public void updateUserInfo(String username, String name,String password);

    public void updateUserHypothesis(int userid,String hypothesisName);

    int insertKWLForm(KWL kwl);

    public List<KWL> getKWLsByUserid(@Param("userid")int userid);

    void insertUserRead(@Param("userid")int userid, @Param("bookid")int bookid);

    int insertQuestionForm(QuestionForm question);

    void insertUserAnswer(int userId, int bookId);

    User findUserById(int userId);

    List<Integer> getUserPermission(int userId);

    List<Heat> getUserHeat(int userId);

    int insertUser(User user);

    void insertReadLog(int userid,String behavior,int bookid,Date stamptime);

    void insertUserPermission(int userId, int permissionId);

    Integer getCourseId(String courseNum);

    void insertUserCourse(int userId,int courseId);

    public List<QuestionForm> getUserQuestions(@Param("userid")int userid, @Param("bookId")int bookId);

    public List<AnswerForm> getAnswerListByQuestionId(@Param("questionid")int questionid);

    public List<UserCourse> getUserListByCourseid(int courseid);

//2023/09/03新增学习仪表盘阅读成绩查询
    public List<TestScore> getUserTestScore(String  username);

}
