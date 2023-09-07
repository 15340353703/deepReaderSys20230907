package cn.ywrby.mapper;

import cn.ywrby.domain.AnswerForm;
import cn.ywrby.domain.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {

    public List<Book> getBookList();

    public List<Book> getSimplifyBookListByClassifyId(@Param("classifyId")int classifyId);


    public List<Book> getSimplifyBookListByCourseId(@Param("courseId")int courseId);

    public String getBookContent(@Param("bookId")int bookId);

    List<Book> getUserBookList(@Param("userId")int userId);

    Book getBookInfo(@Param("bookId")int bookId);

    List<Integer> getUserHaveReadList(@Param("userId")int userId);

    List<AnswerForm> getUserHaveAnswerList(@Param("userId")int userId, @Param("bookId")int bookId);

    List<Book> getBookByClassifyId(@Param("classifyId")int classifyId);

    int uploadBook(Book book);

    void updateBookInfo(Book book);

    void deleteBookInfo(int id);
}
