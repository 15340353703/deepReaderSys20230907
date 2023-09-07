package cn.ywrby.mapper;

import cn.ywrby.domain.Classify;
import cn.ywrby.domain.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getUserCourse(int userId);

    List<Course> getUserCourseBybookid(int bookid);

    List<Course> getCourse();

    void setHavegroupById(int courseid);

    int insertClassify(Classify classify);

    int findCourseNum(String courseNum);

    int insertCourse(Course course);

    List<Course> getCourseList();

    List<Course> getCourseNum(int userId);

    Course getCourseById(int courseid);

    void updateCourseInfo(Course course);

    void deleteCourseInfo(int id);
}
