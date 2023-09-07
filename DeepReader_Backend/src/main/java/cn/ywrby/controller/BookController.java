package cn.ywrby.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.*;
import cn.ywrby.mapper.BookMapper;
import cn.ywrby.mapper.ClassifyMapper;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.BookService;
import cn.ywrby.service.CourseService;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;
    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    @Autowired
    ClassifyMapper classifyMapper;

    @PostMapping("/book/deleteBookInfo")
    public ResultResponse deleteBookInfo(@RequestParam("token") String token,@RequestParam("id") int id){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            boolean result = bookService.deleteBookInfo(id);
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"提交失败，请重新提交");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }

    @PostMapping("/book/updateBookInfo")
    public ResultResponse updateBookInfo(@RequestParam("token") String token,@RequestParam("bookForm") Object bookForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(bookForm);
        int id = (int)jsonObject.get("id");
        String name = (String)jsonObject.get("name");
        String author = (String)jsonObject.get("author");
        JSONArray studyDate= (JSONArray) jsonObject.get("studyDate");
        String brief_introduction = (String)jsonObject.get("brief_introduction");
        int stage= (int) jsonObject.get("reading_stage");
        int classifyId = (int)jsonObject.get("classifyId");
        String wordcountstr = (String)jsonObject.get("wordcount");
        int wordcount;
        try {
            wordcount=Integer.parseInt(wordcountstr);
        }catch (NumberFormatException e){
            wordcount=0;
        }


        Book book=new Book();
        book.setId(id);
        book.setAuthor(author);
        book.setName(name);
        book.setBrief_introduction(brief_introduction);
        book.setClassifyId(classifyId);
        book.setReading_stage(stage);
        book.setWordcount(wordcount);


        Timestamp start_date;
        Timestamp end_date;
        try {
            start_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(0)).getTime());
            end_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(1)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            start_date=new Timestamp(new Date().getTime());
            end_date=new Timestamp(new Date().getTime());
        }
        book.setStart_date(start_date);
        book.setEnd_date(end_date);
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            boolean result = bookService.updateBookInfo(book);
            //boolean result=true;
            if(result){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"上传失败，请重新上传图书");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;

    }


    @GetMapping("/book/getBookInfoByClassifyId")
    public ResultResponse getBookInfoByClassifyId(@RequestParam(required = false,value = "token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);

            List<Classify> classifyList = courseService.getClassifyList(user.getId());

            ArrayList list=new ArrayList();
            for(Classify classify:classifyList){
                List<Book> bookInfoList=bookService.getBookByClassifyId(classify.getId());
                for(int i = 0; i<bookInfoList.size(); i++){
                    list.add(bookInfoList.get(i));
                }
            }

            res.setData(list);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
            return res;
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return  res;

    }

    /*
    @GetMapping("/book/classifyAndBookList")
    public ResultResponse getClassifyAndBookList(){
        ResultResponse res=new ResultResponse();
        try {
            List<Classify> classifyList = bookService.getClassifyAndBookList();
            res.setData(classifyList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }catch (Exception e){
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+e.getMessage());
            res.setData("fail");
            e.printStackTrace();
        }
        return res;
    }
     */

    @GetMapping("/book/list")
    public ResultResponse bookList(@RequestParam(required=true, defaultValue = "1")Integer page, @RequestParam(required=false,defaultValue="10")Integer pageSize){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<Book> list=bookService.getBookList(page,pageSize);
        //获取分页信息对象
        PageInfo<Book> info=new PageInfo<Book>(list);

        res.setData(info);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        System.out.println(info);
        return res;
    }

    @GetMapping("/book/allbooks")
    public ResultResponse allbooks(){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<Book> list=bookMapper.getBookList();

        res.setData(list);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/book/getBookByClassify")
    public ResultResponse getBookByClassifyId(@RequestParam(required=true)Integer classifyId){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<Book> list=bookService.getBookByClassifyId(classifyId);
        res.setData(list);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/book/getBookByCourseId")
    public ResultResponse getBookByCourseId(@RequestParam(required=true)Integer courseId){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<Classify> classifyList = classifyMapper.getClassifyListByCourseId(courseId);
        List<Book> bookList = new ArrayList<>();
        for(Classify classify : classifyList){
            bookList.addAll(bookService.getBookByClassifyId(classify.getId()));
        }
        res.setData(bookList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/book/content")
    public ResultResponse getBookContent(@RequestParam(required=true)Integer bookId){
        ResultResponse res=new ResultResponse();
        String content = bookService.getBookContent(bookId);
        res.setData(content);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/book/info")
    public ResultResponse getBookInfo(@RequestParam(required=true)Integer bookId){
        ResultResponse res=new ResultResponse();
        Book book = bookService.getBookInfo(bookId);
        System.out.print("book11:"+book);
        res.setData(book);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/book/classify")
    public ResultResponse getClassifyList(){
        ResultResponse res=new ResultResponse();
        List<SimpleClassify> simpleClassifyList=bookService.getClassify();
        res.setData(simpleClassifyList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @PostMapping("/book/uploadBook")
    public ResultResponse uploadBook(@RequestParam("token") String token,@RequestParam("bookForm") String bookForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(bookForm);
        JSONArray studyDate= (JSONArray) jsonObject.get("studyDate");
        String stageStr= (String) jsonObject.get("stage");
        int stage;
        try {
            stage=Integer.parseInt(stageStr);
        }catch (NumberFormatException e){
            stage=0;
        }
        int classifyId=(int)jsonObject.get("classifyId");
        String url=(String)jsonObject.get("url");
        String bookName=(String)jsonObject.get("bookName");
        String author=(String)jsonObject.get("author");
        String introduction=(String)jsonObject.get("brief_introduction");
        String content=(String)jsonObject.get("content");
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //获取用户登录账户
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);
            Book book=new Book();

            book.setAuthor(author);
            book.setName(bookName);
            book.setContent(content);
            book.setBrief_introduction(introduction);
            book.setClassifyId(classifyId);
            book.setReading_stage(stage);
            book.setUrl(url);

            Timestamp start_date;
            Timestamp end_date;
            try {
                start_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(0)).getTime());
                end_date=new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse((String) studyDate.get(1)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                start_date=new Timestamp(new Date().getTime());
                end_date=new Timestamp(new Date().getTime());
            }
            book.setStart_date(start_date);
            book.setEnd_date(end_date);
            //插入用户KWL表格
            Integer bookId=bookService.uploadBook(book);
            //boolean result=true;
            if(bookId!=null){
                res.setCode(Constants.STATUS_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"上传失败，请重新上传图书");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }
        return res;
    }
}
