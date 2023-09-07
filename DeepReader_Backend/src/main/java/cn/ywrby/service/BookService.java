package cn.ywrby.service;


import cn.ywrby.domain.Book;
import cn.ywrby.domain.Classify;
import cn.ywrby.domain.SimpleClassify;
import cn.ywrby.domain.User;

import java.util.List;

public interface BookService {

    public List<Classify> getClassifyAndBookList();

    public List<Book> getBookList(Integer page,Integer pageSize);

    public String getBookContent(Integer bookId);

    public List<Book> getUserBookList(User user, Integer page, Integer pageSize);

    List<Book> getUserBookListByCourse(User user, Integer page, Integer pageSize, Integer courseId);

    public Book getBookInfo(int bookId);

    public List<SimpleClassify> getClassify();

    public List<Book> getBookByClassifyId(int classifyId);

    int uploadBook(Book book);

    Boolean updateBookInfo(Book book);

    Boolean deleteBookInfo(int id);

}
