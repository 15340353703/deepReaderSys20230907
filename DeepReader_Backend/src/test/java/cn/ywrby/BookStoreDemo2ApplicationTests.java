package cn.ywrby;


import cn.ywrby.domain.Heat;
import cn.ywrby.mapper.CourseMapper;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BookStoreDemo2ApplicationTests {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseService courseService;

    @Test
    public void testForHeat(){

    }

}
