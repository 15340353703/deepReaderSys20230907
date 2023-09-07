package cn.ywrby.service.impl;

import cn.ywrby.domain.*;
import cn.ywrby.mapper.*;
import cn.ywrby.service.BookService;
import cn.ywrby.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @Autowired
    ClassifyMapper classifyMapper;

    @Autowired
    DeepQuestionMapper deepQuestionMapper;


    @Override
    public List<Classify> getClassifyAndBookList() {
        List<Classify> classifyList = classifyMapper.getClassifyList();
        List<Classify> result=new LinkedList<>();
        for (Classify classify : classifyList) {
            List<Book> bookList = bookMapper.getSimplifyBookListByClassifyId(classify.getId());
            classify.setChildren(bookList);
            result.add(classify);
        }
        return result;
    }

    @Override
    public List<Book> getBookList(Integer page,Integer pageSize) {
        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);

        List<Book> bookList = bookMapper.getBookList();
        return bookList;
    }

    @Override
    public String getBookContent(Integer bookId) {
        String content = bookMapper.getBookContent(bookId);
        return content;
    }

    @Override
    public List<Book> getUserBookList(User user, Integer page, Integer pageSize) {

        int userid=user.getId();
        //获取该用户图书列表
        List<Book> bookList = bookMapper.getUserBookList(userid);
        //获取该用户已阅读图书ID列表
        List<Integer> haveReadList = bookMapper.getUserHaveReadList(userid);
        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);
        //获取用户ID
        List<Book> resultList = new ArrayList<>();
        //查询用户阅读过哪些图书并标记
        for (Book book: bookList){
            for(int bookId: haveReadList){
                if(book.getId()==bookId){
                    book.setHave_read(true);
                }
            }
            //获取该用户已进行过练习的图书ID列表
            List<AnswerForm> haveAnswerList=bookMapper.getUserHaveAnswerList(userid,book.getId());
            Classify classify = classifyMapper.findClassifyById(book.getClassifyId());
            Course c = courseMapper.getCourseById(classify.getCourseId());
            List<UserGroup> userGroups = groupMapper.getUserGroupList();
            List<QuestionForm> questionFormList = new ArrayList<>();
            for(UserGroup userGroup : userGroups){
                if(userGroup.getGroupname().contains(c.getCourseName()) && userGroup.getUsername().equals(user.getUsername())){
                    for(UserGroup ug : userGroups){
                        if(ug.getGroupname().equals(userGroup.getGroupname()) && !ug.getUsername().equals(user.getUsername())){
                            User u = userService.findUserByUsername(ug.getUsername());
                            try{
                                List<QuestionForm> questionForms = deepQuestionMapper.getQuestionFormByBookAndUser(u.getId(), book.getId());
                                questionFormList.addAll(questionForms);
                            }catch (Exception e){
                                System.out.println("未获取到数据");
                            }


                        }
                    }
                }
            }
            List<Integer> haveAnswerQuestionids = new ArrayList<>();
            for(AnswerForm answerForm:haveAnswerList){
                if(!haveAnswerQuestionids.contains(answerForm.getQuestionId()))
                    haveAnswerQuestionids.add(answerForm.getQuestionId());
            }
            System.out.println("图书名称："+book.getName());
            System.out.println("该图书中你的所有问题列表："+questionFormList.size());
            System.out.println("对该图书你已经回答的问题："+haveAnswerQuestionids.size());
            if(haveAnswerQuestionids.size()==questionFormList.size()){
                book.setHave_answer(true);
            }
            resultList.add(book);

        }
        return resultList;
    }

    @Override
    public List<Book> getUserBookListByCourse(User user, Integer page, Integer pageSize, Integer courseId) {
        //获取分页插件对象
        PageHelper pageHelper=new PageHelper();
        //开始分页，指定分页参数
        PageMethod.startPage(page,pageSize);
        //获取用户ID
        int userid=user.getId();
        //获取该用户图书列表
        List<Book> bookList = bookMapper.getUserBookList(userid);
        //获取该用户已阅读图书ID列表
        List<Integer> haveReadList = bookMapper.getUserHaveReadList(userid);

        //查询用户阅读过哪些图书并标记
        for (Book book: bookList){
            for(int bookId: haveReadList){
                if(book.getId()==bookId){
                    book.setHave_read(true);
                }
            }
            //获取该用户已进行过练习的图书ID列表
            List<AnswerForm> haveAnswerList=bookMapper.getUserHaveAnswerList(userid,book.getId());
            Classify classify = classifyMapper.findClassifyById(book.getClassifyId());
            Course c = courseMapper.getCourseById(classify.getCourseId());
            List<UserGroup> userGroups = groupMapper.getUserGroupList();
            List<QuestionForm> questionFormList = new ArrayList<>();
            for(UserGroup userGroup : userGroups){
                if(userGroup.getGroupname().contains(c.getCourseName()) && userGroup.getUsername().equals(user.getUsername())){
                    for(UserGroup ug : userGroups){
                        if(ug.getGroupname().equals(userGroup.getGroupname()) && !ug.getUsername().equals(user.getUsername())){
                            User u = userService.findUserByUsername(ug.getUsername());
                            List<QuestionForm> questionForms = deepQuestionMapper.getQuestionFormByUserId(u.getId());
                            questionFormList.addAll(questionForms);
                        }
                    }
                }
            }
            List<Integer> haveAnswerQuestionids = new ArrayList<>();
            for(AnswerForm answerForm:haveAnswerList){
                if(!haveAnswerQuestionids.contains(answerForm.getQuestionId()))
                    haveAnswerQuestionids.add(answerForm.getQuestionId());
            }
            if(haveAnswerQuestionids.size()==questionFormList.size()){
                book.setHave_answer(true);
            }

        }

        //筛选制定课程的图书列表
        List<Book> bookListByCourse=new ArrayList<>();
        for(Book book:bookList){
            if(book.getCourseId()==courseId){
                bookListByCourse.add(book);
            }
        }
        return bookListByCourse;
    }

    @Override
    public Book getBookInfo(int bookId) {
        Book book=bookMapper.getBookInfo(bookId);
        return book;
    }

    @Override
    public List<SimpleClassify>  getClassify() {
        List<Classify> classifyList = classifyMapper.getClassifyList();
        List<SimpleClassify> simpleClassifyList=new ArrayList<>();
        for(Classify classify:classifyList){
            boolean res=true;

            for (SimpleClassify simpleClassify:simpleClassifyList){
                if (classify.getName().equals(simpleClassify.getLabel())){
                    res=false;
                    simpleClassify.getChildren().add(new Work(classify.getId(),classify.getWork()));
                }
            }

            if(res){
                SimpleClassify simpleClassify=new SimpleClassify();
                simpleClassify.setLabel(classify.getName());
                simpleClassify.setChildren(new ArrayList<>());
                simpleClassify.getChildren().add(new Work(classify.getId(),classify.getWork()));
                simpleClassifyList.add(simpleClassify);
            }
        }
        System.out.println(simpleClassifyList);
        return simpleClassifyList;
    }

    @Override
    public List<Book> getBookByClassifyId(int classifyId) {
        List<Book> bookList = bookMapper.getBookByClassifyId(classifyId);
        return bookList;
    }

    @Override
    public int uploadBook(Book book) {
        int bookId=bookMapper.uploadBook(book);
        return bookId;
    }

    @Override
    public Boolean updateBookInfo(Book book) {
        if(book != null){
            bookMapper.updateBookInfo(book);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Boolean deleteBookInfo(int id) {
        if (id != 0){
            bookMapper.deleteBookInfo(id);
            return true;
        }else{
            return false;
        }
    }


}
