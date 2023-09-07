package cn.ywrby.mapper;

import cn.ywrby.domain.Classify;
import cn.ywrby.domain.UserGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    public void insertGroup(String groupname);

    public List<UserGroup> getUserGroupList();

    public void insertUsergroup(String groupname,String username);
}
