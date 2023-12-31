package cn.ywrby.mapper;

import cn.ywrby.domain.Classify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassifyMapper {
    public List<Classify> getClassifyList();

    Classify findClassifyById(int classifyId);

    List<Classify> getClassifyListByCourseId(int id);

    void updateClassifyInfo(Classify classify);

    void deleteClassifyInfo(int id);
}
