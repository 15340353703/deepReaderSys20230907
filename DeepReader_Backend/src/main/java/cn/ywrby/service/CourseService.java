package cn.ywrby.service;

import cn.ywrby.domain.Classify;
import cn.ywrby.domain.Course;
import cn.ywrby.domain.User;

import java.sql.Timestamp;
import java.util.List;

public interface CourseService {

    public List<Course> getUserCourse(User user);

    List<Course> getCourse(Integer page, Integer pageSize);

    Integer insertClassify(String classifyName, int displayMode, int courseId, Timestamp start_date, Timestamp end_date);

    String createCourseNum();

    String insertCourse(User user, String courseName, String instructorName, String courseContent, String groupnameHP);

    List<Course> getCourseNum(User user);

    boolean updateCourseInfo(Course course);

    boolean deleteCourseInfo(int id);

    List<Classify> getClassifyList(int userId);

    List<Classify> getClassifyList2(int userId);


    boolean updateClassifyInfo(Classify classify);

    boolean deleteClassifyInfo(int id);
}
