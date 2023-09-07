package cn.ywrby.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.ywrby.domain.*;
import cn.ywrby.mapper.*;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.BookService;
import cn.ywrby.service.DeepQuestionService;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class DeepQuestionController {

    @Autowired
    BookService bookService;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    DeepQuestionService dqService;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ClassifyMapper classifyMapper;

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    GroupMapper groupMapper;

    @Autowired
    DeepQuestionMapper deepQuestionMapper;


    @GetMapping("/dq/getUserCourseAnswerForm")
    public ResultResponse getUserCourseAnswerForm (@RequestParam("token") String token,@RequestParam("courseId") Integer courseId){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            List<Classify> classifyList = classifyMapper.getClassifyListByCourseId(courseId);
            List<AnswerForm> answerFormList = new ArrayList<>();
            for(Classify cl : classifyList){
                List<Book> bookList = bookMapper.getBookByClassifyId(cl.getId());
                for(Book bl : bookList){
                    try{
                        List<AnswerForm> answerForms = deepQuestionMapper.getAnswerFormByUserId(user.getId(),bl.getId());
                        if(answerForms.size()!=0) {
                            answerFormList.addAll(answerForms);
                            System.out.println("回答问题表单："+answerFormList);
                        }
                    }catch(Exception e){
                        System.out.println("未获取到数据");
                    }
                }

            }
            System.out.println("回答问题表单总的表单："+answerFormList);
            res.setData(answerFormList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        } else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }

        return res;

    }

    @GetMapping("/dq/getQuestionFormByBookId")
    public ResultResponse getQuestionFormByBookId(@RequestParam("token") String token, @RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //获取用户登录账户（学生号）
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            Book book = bookService.getBookInfo(bookId);
            Classify classify = classifyMapper.findClassifyById(book.getClassifyId());
            Course c = courseMapper.getCourseById(classify.getCourseId());
            List<UserGroup> userGroups = groupMapper.getUserGroupList();
            List<QuestionForm> questionFormList = new ArrayList<>();
            System.out.println("所有课程组："+userGroups);
            for(UserGroup userGroup : userGroups){
                if(userGroup.getGroupname().contains(c.getCourseName()) && userGroup.getUsername().equals(user.getUsername())){
                    System.out.println("该学生所在课程组："+userGroup);
                    for(UserGroup ug : userGroups){
                        if(ug.getGroupname().equals(userGroup.getGroupname()) && !ug.getUsername().equals(username)){
                            User u = userService.findUserByUsername(ug.getUsername());
                            try{
                                List<QuestionForm> questionForms = deepQuestionMapper.getQuestionFormByBookAndUser(u.getId(),bookId);
                                if(questionForms.size()!=0) {
                                    questionFormList.addAll(questionForms);
                                }
                            }catch(Exception e){
                                System.out.println("未获取到数据");
                            }
                        }
                    }
                }
            }
            List<QuestionForm> questionList = new ArrayList<>();
            for(QuestionForm questionForm: questionFormList){
                questionForm.setFluency(0);
                questionForm.setImportance(0);
                questionForm.setComplexity(0);
                List<AnswerForm> youranswer = deepQuestionMapper.getAnswerFormByUserAndQuestion(user.getId(),questionForm.getId());
                if(youranswer.size()!=0){
                    questionForm.setYouranswer(youranswer.get(youranswer.size()-1));
                }else{
                    questionForm.setYouranswer(null);
                }
                questionList.add(questionForm);
            }
            System.out.println("问题列表："+questionList);
            System.out.println("问题列表长度："+questionList.size());
            res.setData(questionList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
        }

        return res;

    }

    @GetMapping("/dq/getQuestionsBybook")
    public ResultResponse getQuestionsBybook(@RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        List<QuestionForm> questionFormList=deepQuestionMapper.getQuestionsBybook(bookId);
        res.setData(questionFormList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/dq/getAnswersBybook")
    public ResultResponse getAnswersBybook(@RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        List<AnswerForm> answerFormList=deepQuestionMapper.getAnswersBybook(bookId);
        res.setData(answerFormList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;

    }

    @GetMapping("/dq/getHighlightsBybook")
    public ResultResponse getHighlightsBybook(@RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        List<SummaryQuestion> HighlightList=deepQuestionMapper.getHighlightsBybook(bookId);
        res.setData(HighlightList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/dq/getAllreadlog")
    public ResultResponse getReadlog(@RequestParam("userid") Integer userid){
        ResultResponse res=new ResultResponse();
        List<ReadLog> ReadlogList=deepQuestionMapper.getAllReadlog(userid);

        res.setData(ReadlogList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }
    @GetMapping("/dq/getAllReadlog")
    public ResultResponse getallReadlog(@RequestParam("userid") Integer userid){
        ResultResponse res=new ResultResponse();
//        try {
        List<ReadLog> ReadlogList = deepQuestionMapper.getAllReadlog(userid);
        System.out.println("ReadLogList:"+ReadlogList);

        List<FrontReadLog> readLogs = new ArrayList<>();
        for (ReadLog rl : ReadlogList) {
            Book book = bookMapper.getBookInfo(rl.getBookid());
            FrontReadLog frontReadLog = new FrontReadLog();
            frontReadLog.setBookname(book.getName());
            frontReadLog.setId(rl.getId());
            frontReadLog.setBehavior(rl.getBehavior());
            frontReadLog.setUserId(rl.getUserId());
            frontReadLog.setStamptime(rl.getStamptime());
            readLogs.add(frontReadLog);
        }
        res.setData(readLogs);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);

//        }catch(Exception e){
//            res.setData(null);
//            res.setCode(Constants.STATUS_OK);
//            res.setMessage(Constants.MESSAGE_OK);
//        }



        return res;
    }

    @GetMapping("/dq/getHighlightsByUser")
    public ResultResponse getHighlightsByUser(@RequestParam("courseid") Integer courseid){
        ResultResponse res=new ResultResponse();
        if(courseid!=0){
            List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);
            List<User> users = new ArrayList<>();
            for(UserCourse uc : userCourses){
                User u = userMapper.findUserById(uc.getUserId());
                users.add(u);
            }
            List<Integer> userHighlightCount = new ArrayList<>();
            for(User u: users){
                int count = 0;
                List<SummaryQuestion> HighlightList=deepQuestionMapper.getHighlightsByUser(u.getId());
                if(HighlightList.size()!=0){
                    for(SummaryQuestion sq : HighlightList){
                        JSONArray highlights = new JSONArray(sq.getReadfeedback());
                        JSONObject hl = (JSONObject) highlights.get(0);
                        JSONArray hls = (JSONArray) hl.get("feedback");
                        count += hls.size();
                    }
                    userHighlightCount.add(count);
                } else {
                    userHighlightCount.add(0);
                }
            }
            res.setData(userHighlightCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            List<User> users = userMapper.findAll();
            List<Integer> userHighlightCount = new ArrayList<>();
            for(User u: users){
                int count = 0;
                List<SummaryQuestion> HighlightList=deepQuestionMapper.getHighlightsByUser(u.getId());
                if(HighlightList.size()!=0){
                    for(SummaryQuestion sq : HighlightList){
                        JSONArray highlights = new JSONArray(sq.getReadfeedback());
                        JSONObject hl = (JSONObject) highlights.get(0);
                        JSONArray hls = (JSONArray) hl.get("feedback");
                        count += hls.size();
                    }
                    userHighlightCount.add(count);
                } else {
                    userHighlightCount.add(0);
                }

            }
            res.setData(userHighlightCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }

    @GetMapping("/dq/getLoginCount")
    public ResultResponse getLoginCount(){
        ResultResponse res=new ResultResponse();
        int loginCount=deepQuestionMapper.getLoginCount();
        res.setData(loginCount);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/dq/getLoginCountByUser")
    public ResultResponse getUserLoginCount(@RequestParam("courseid") Integer courseid){
        ResultResponse res=new ResultResponse();
        if(courseid !=0){
            List<UserCourse> userCourses = userMapper.getUserListByCourseid(courseid);
            List<User> users = new ArrayList<>();
            for(UserCourse uc : userCourses){
                User u = userMapper.findUserById(uc.getUserId());
                users.add(u);
            }
            List<Integer> loginCount = new ArrayList<>();
            for(User u: users){
                int lc=deepQuestionMapper.getLoginCountByUser(u.getId());
                loginCount.add(lc);
            }

            res.setData(loginCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else{
            List<User> users = userMapper.findAll();
            List<Integer> loginCount = new ArrayList<>();
            for(User u: users){
                int lc=deepQuestionMapper.getLoginCountByUser(u.getId());
                loginCount.add(lc);
            }

            res.setData(loginCount);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }
        return res;
    }

    @GetMapping("/dq/getAllQuestions")
    public ResultResponse getAllQuestions(){
        ResultResponse res=new ResultResponse();
        List<QuestionForm> questionFormList=deepQuestionMapper.getAllQuestions();
        res.setData(questionFormList);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;

    }

    @GetMapping("/dq/getAnswerFormByUserId")
    public ResultResponse getAnswerFormByUserId(@RequestParam("userId") int userId, @RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        List<AnswerForm> answerForms=dqService.getAnswerFormByUserId(userId, bookId);
        if(answerForms.size()!=0){
            res.setData(answerForms);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else{
            res.setData(null);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }
        return res;

    }

    @PostMapping("/dq/submitAnswer")
    public ResultResponse updateQuestionForm(@RequestParam("token") String token,@RequestParam("answerForm") String answerFormStr){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得回答表单对象
        AnswerForm answerForm = dqService.getAnswerForm(answerFormStr);
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //获取用户登录账户（学生号）
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            answerForm.setUserId(user.getId());
            answerForm.setFinish_time(new Date());
            boolean result = dqService.insertAnswerForm(answerForm);
            if(result){
                //更新用户练习状态
                userService.insertUserAnswer(user.getId(),answerForm.getBookId());
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

    @PostMapping("/dq/submitsummary")
    public ResultResponse submitSummary(@RequestBody String feedback){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(feedback);
        String token = (String) jsonObject.get("token");
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //获取用户登录账户（学生号）
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);

            JSONArray summarylist = (JSONArray) jsonObject.get("summarys");
            String strSummarys = null;
            if(summarylist!=null || summarylist.size()==0){
                strSummarys  = summarylist.toString();
            }
            int bookid = (int)jsonObject.get("bookid");
            int questionid = (int)jsonObject.get("questionid");
            System.out.println("问题编号："+questionid);
            String qf = (String)jsonObject.get("questionfeedback");
            String qbf = (String) jsonObject.get("questionfeedbackfeeling");
            String qbr = String.valueOf(jsonObject.get("questionfeedbackusefulrate"));
            String qbc = (String) jsonObject.get("questionfeedbackusefulcause");


            String abf = (String) jsonObject.get("annotationfeedbackfeeling");
            String abr = String.valueOf(jsonObject.get("annotationfeedbackusefulrate"));
            String abc = (String) jsonObject.get("annotationfeedbackusefulcause");
            JSONArray af = (JSONArray) jsonObject.get("readfeedback");
            String strAf = null;
            if(af!=null){
                strAf = af.toString();
            }
            deepQuestionMapper.insertQuestionfeedback(user.getId(),bookid,qf,qbf,qbr,qbc,new Date());
            deepQuestionMapper.insertAnnotationfeedback(user.getId(),bookid,strAf,abf,abr,abc,new Date());
            deepQuestionMapper.insertUserSummary(user.getId(),questionid,strSummarys,new Date());
            deepQuestionMapper.insertSummaryQuestion(user.getId(),questionid,qf,qbf,qbr,qbc,strAf,abf,abr,abc,new Date());
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        } else {
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    /**
     * Excel导出
     * @param response
     * @throws IOException
     */
    @GetMapping("/dq/hypothesis/{id}")
    public void downloadHypothesis(HttpServletResponse response, @PathVariable int id) throws IOException {
        List<Map<String, Object>> list = CollUtil.newArrayList();

        List<UserHighlight> userHighlights = deepQuestionMapper.getUserhighlight(id);
        for (UserHighlight uh : userHighlights) {
            Map<String, Object> row1 = new LinkedHashMap<>();
            row1.put("学号", uh.getUsername());
            row1.put("姓名",uh.getYourname());
            row1.put("文章名称", uh.getName());
            row1.put("文本反馈", uh.getReadfeedback());
            list.add(row1);
        }

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("全体学生文本标注", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }

    @GetMapping("/dq/getReadlog")
    public ResultResponse getReadlog2(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            List<ReadLog> ReadlogList=deepQuestionMapper.getAllReadlog(user.getId());
            res.setData(ReadlogList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return  res;
    }

    @GetMapping("/dq/getReadlogByBookId")
    public ResultResponse getReadlogByBookId(@RequestParam("token") String token,@RequestParam("bookid") Integer bookid){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue= JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            List<ReadLog> ReadlogList=deepQuestionMapper.getReadlogByBookId(user.getId(),bookid);
            res.setData(ReadlogList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return  res;
    }


}
