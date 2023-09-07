package cn.ywrby.mapper;

import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserLoginMapper{
    public void insertUserLogin(@Param("userid")int userid,@Param("timestamp") Date timestamp);
}
