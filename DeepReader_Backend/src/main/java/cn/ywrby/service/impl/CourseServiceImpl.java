package cn.ywrby.service.impl;

import cn.ywrby.domain.Classify;
import cn.ywrby.domain.Course;
import cn.ywrby.domain.User;
import cn.ywrby.mapper.ClassifyMapper;
import cn.ywrby.mapper.CourseMapper;
import cn.ywrby.service.CourseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static cn.ywrby.utils.Constants.COURSE_NUM_LEN;
import static cn.ywrby.utils.CourseNumUtils.getRandomString;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    ClassifyMapper classifyMapper;

    @Override
    public List<Course> getUserCourse(User user) {
        List<Course> userCourse = courseMapper.getUserCourse(user.getId());
        return userCourse;
    }

    @Override
    public List<Course> getCourse(Integer page, Integer pageSize) {
        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);
        List<Course> courseList=courseMapper.getCourse();
        return courseList;
    }

    @Override
    public Integer insertClassify(String classifyName, int displayMode, int courseId, Timestamp start_date,Timestamp end_date) {
        int classifyId;
        Classify classify=new Classify();
        classify.setName(classifyName);
        classify.setCourseId(courseId);
        classify.setDisplayMode(displayMode);
        classify.setStart_date(start_date);
        classify.setEnd_date(end_date);


        if(courseId!=0){
            classifyId=courseMapper.insertClassify(classify);
        }else {
            classifyId=0;
        }
        return classifyId;
    }

    @Override
    public String createCourseNum() {
        int breakeNum=50;
        int i=0;
        String courseNum = getRandomString(COURSE_NUM_LEN);
        while (courseMapper.findCourseNum(courseNum)!=0){
            courseNum=getRandomString(COURSE_NUM_LEN);
            i++;
            if(i>=breakeNum){
                return null;
            }
        }
        return courseNum;
    }

    @Override
    public String insertCourse(User user, String courseName, String instructorName, String courseContent, String groupnameHP) {
        String courseNum=createCourseNum();
        if(courseNum!=null){
            Course course=new Course();
            course.setCourseName(courseName);
            course.setCourseNum(courseNum);
            course.setInstructorName(instructorName);
//            course.setContent(courseContent);
            course.setCourseContent(courseContent);
            course.setGroupnameHP(groupnameHP);
            course.setUserId(user.getId());
            int courseId=courseMapper.insertCourse(course);
            if(courseId!=0){
                return courseNum;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public List<Course> getCourseNum(User user) {
        List<Course> courseList=courseMapper.getCourseNum(user.getId());
        return courseList;
    }

    @Override
    public boolean updateCourseInfo(Course course) {
        if(course != null){
            courseMapper.updateCourseInfo(course);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteCourseInfo(int id) {
        if (id != 0){
            courseMapper.deleteCourseInfo(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Classify> getClassifyList(int userId) {
        List<Course> courseList = courseMapper.getCourseNum(userId);
//        List<Classify> classifyList = new List<Classify>();
        ArrayList list=new ArrayList();
        for(Course course:courseList){
            List <Classify> classify = classifyMapper.getClassifyListByCourseId(course.getId());
            for(int i = 0; i<classify.size(); i++){
                list.add(classify.get(i));
            }
            System.out.println(list);

        }
        return list;

    }

    @Override
    public List<Classify> getClassifyList2(int userId) {
        List<Course> courseList = courseMapper.getUserCourse(userId);
        System.out.print("courseList2:"+courseList);
        List<Classify> classifyList = new ArrayList();
        for(Course course:courseList){
            List<Classify> classifyList2= classifyMapper.getClassifyListByCourseId(course.getId());
            for(int i = 0; i<classifyList2.size(); i++){
                classifyList.add(classifyList2.get(i));
            }
            System.out.println(classifyList);
        }

        return classifyList;
    }

    @Override
    public boolean updateClassifyInfo(Classify classify) {
        if(classify != null){
            classifyMapper.updateClassifyInfo(classify);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteClassifyInfo(int id) {
        if (id != 0){
            classifyMapper.deleteClassifyInfo(id);
            return true;
        }else{
            return false;
        }
    }

}
