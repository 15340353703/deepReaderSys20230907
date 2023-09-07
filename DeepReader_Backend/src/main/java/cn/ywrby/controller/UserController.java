package cn.ywrby.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.*;
import cn.ywrby.mapper.DeepQuestionMapper;
import cn.ywrby.mapper.UserLoginMapper;
import cn.ywrby.mapper.UserMapper;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.service.BookService;
import cn.ywrby.service.UserService;
import cn.ywrby.utils.Constants;
import cn.ywrby.utils.JwtUtils;
import com.github.pagehelper.PageInfo;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
//import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import java.util.*;
import org.apache.http.HttpHost;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeepQuestionMapper deepQuestionMapper;

    @PostMapping("/user/login")
    public ResultResponse login(@RequestBody User user){
        System.out.println(user);
        ResultResponse res=new ResultResponse();
        //调用Service层的UserService完成对用户名和密码的验证
        try {
            User u=userService.verify(user);
            //根据验证结果，组成响应对象返回
            if (u!=null){
                //验证成功，创建一个Token，封装到res对象中
                String token = JwtUtils.sign(user.getUsername(), "-1");
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
                res.setData(new Token(token));
                userLoginMapper.insertUserLogin(u.getId(),new Date());
            }else {
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"用户名与密码不匹配");
                res.setData("fail");
            }
        }catch (Exception e){
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+e.getMessage());
            res.setData("fail");
            e.printStackTrace();
        }
        return res;
    }


    @GetMapping("/user/info")
    public ResultResponse info(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //如果合法则返回用户信息
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);
            user.setUsername(username);
            user.setAvatar("https://ywrbyimg.oss-cn-chengdu.aliyuncs.com/img/ywrby.png");

            res.setData(user);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }

        return res;
    }


    @PostMapping("/user/logout")
    public ResultResponse logout(@RequestHeader("X-Token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法性和有效性
        String tokenValue=JwtUtils.verify(token);
        //获取token中的用户名
        String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
        //移除Session中的登陆标记（或redis中的登录标记）

        res.setMessage("Logout success");
        res.setData("Logout success");
        res.setCode(Constants.STATUS_OK);
        return res;
    }

    @GetMapping("/user/booklist")
    public ResultResponse bookList(@RequestParam("token") String token,@RequestParam(required=true, defaultValue = "1")Integer page, @RequestParam(required=false,defaultValue="10")Integer pageSize){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            System.out.println("用户信息如下:"+user.getUsername()+user.getYourname());
            user.setUsername(username);
            //调用service层方法获得用户图书列表
            List<Book> list = bookService.getUserBookList(user, page, pageSize);
            //获取分页信息对象
            PageInfo<Book> info = new PageInfo<Book>(list);
            res.setData(info);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
            System.out.println(info);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/user/userQuestions")
    public ResultResponse questionList(@RequestParam("token") String token,@RequestParam(required=true, defaultValue = "1")Integer page,
                                       @RequestParam(required=false,defaultValue="10")Integer pageSize,
                                       @RequestParam("bookId")Integer bookId){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            //调用service层方法获得用户图书列表
            List<QuestionForm> list = userService.getUserQuestions(user.getId(), page, pageSize,bookId);
            for(QuestionForm questionForm:list){
                double fluency = 0.0;
                double importance = 0.0;
                double complexity = 0.0;
                List<AnswerForm> answerList = userService.getAnswerListByQuestionId(questionForm.getId());
                if(answerList.size()!=0){
                    for(AnswerForm answer:answerList){
                        fluency += answer.getFluency();
                        importance += answer.getImportance();
                        complexity += answer.getComplexity();
                    }
                    questionForm.setFluency(fluency / answerList.size());
                    questionForm.setImportance(importance / answerList.size());
                    questionForm.setComplexity(complexity / answerList.size());
                    String f = String.format("%.2f", fluency / answerList.size());
                    String i = String.format("%.2f", importance / answerList.size());
                    String c = String.format("%.2f", complexity / answerList.size());
                    deepQuestionMapper.updateQuestionForm(user.getId(), questionForm.getId() ,Double.parseDouble(f),Double.parseDouble(i),Double.parseDouble(c));
                    //获取分页信息对象
                    PageInfo<QuestionForm> info = new PageInfo<QuestionForm>(list);
                    System.out.print("用户问题111："+info);
                    res.setData(info);
                    res.setCode(Constants.STATUS_OK);
                    res.setMessage(Constants.MESSAGE_OK);
                    System.out.println(info);
                } else {
                    //获取分页信息对象
                    PageInfo<QuestionForm> info = new PageInfo<QuestionForm>(list);
                    res.setData(info);
                    res.setCode(Constants.STATUS_OK);
                    res.setMessage(Constants.MESSAGE_OK);
                }
            }

        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/user/userkwl")
    public ResultResponse bookList(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            List<KWL> userKWLs = userMapper.getKWLsByUserid(user.getId());
            System.out.println("UserKWLs"+userKWLs);
            res.setData(userKWLs);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @GetMapping("/user/booklistByCourse")
    public ResultResponse booklistByCourse(@RequestParam("token") String token,@RequestParam(required=true, defaultValue = "1")Integer page,
                                           @RequestParam(required=false,defaultValue="10")Integer pageSize, Integer courseId){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //调用service层方法获得用户图书列表(该方法下获取指定课程的图书)
            List<Book> list = bookService.getUserBookListByCourse(user, page, pageSize, courseId);
            //获取分页信息对象
            PageInfo<Book> info = new PageInfo<Book>(list);
            res.setData(info);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
            System.out.println(info);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }

    @PostMapping("/user/updateUserKWLForm")
    public ResultResponse updateUserKWLForm(@RequestParam("token") String token,@RequestParam("KWLForm") String KWLForm){
        ResultResponse res=new ResultResponse();

        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(KWLForm);
        JSONArray knows= (JSONArray) jsonObject.get("knows");
        String strKnows=knows.toString();
        JSONArray doubts = (JSONArray) jsonObject.get("doubts");
        String strDoubts=doubts.toString();
        int bookId= (int) jsonObject.get("bookId");
        int time=(int)jsonObject.get("time");
        JSONObject summary= (JSONObject) jsonObject.get("summary");
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)){
            //获取用户登录账户（学生号）
            String username=tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS,"");
            User user=userService.findUserByUsername(username);
            KWL kwl=new KWL();
            kwl.setBookId(bookId);
            kwl.setUserId(user.getId());
            kwl.setPreKnown(strKnows);
            kwl.setQA(strDoubts);
            kwl.setRate((Integer) summary.get("rate"));
            kwl.setSummary((String) summary.get("content"));
            kwl.setRead_time(time);
            kwl.setFinish_time(new Date());
            //插入用户KWL表格
            boolean result=userService.insertKWLForm(kwl);
            //boolean result=true;
            if(result){
                //更新用户阅读状态
                userMapper.insertUserRead(user.getId(),bookId);
                System.out.println("bookId:"+bookId+" Userid:"+user.getId());

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

    @PostMapping("/user/updateQuestionForm")
    public ResultResponse updateQuestionForm(@RequestParam("token") String token,@RequestParam("questionForm") String questionForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(questionForm);
        int bookId= (int) jsonObject.get("bookId");
        Object rateobj=jsonObject.get("robotRate");
        int rate = 0;
        if (rateobj instanceof Integer) {
            // 处理字符串类型的响应
            rate = (int) rateobj;
        } else if (rateobj instanceof String) {
            // 处理数组类型的响应
            rate  = 0;
        }

//        String robotQuestion=(String)jsonObject.get("robotQuestion");
        Object robotQuestionobj = jsonObject.get("robotQuestion");
        String robotQuestion = null;
        if (robotQuestionobj instanceof String) {
            // 处理字符串类型的响应
            robotQuestion = (String) robotQuestionobj;
        } else if (robotQuestionobj instanceof JSONArray) {
            // 处理数组类型的响应
            JSONArray robotQuestionList = (JSONArray) robotQuestionobj;
            robotQuestion  = robotQuestionList.toString();
        }
//        if(robotQuestionList!=null && robotQuestionList.size()!=0){
//            robotQuestion  = robotQuestionList.toString();
//        }

        String modifyQuestion=(String)jsonObject.get("modifyQuestion");

        Object suggestionhelpobj =  jsonObject.get("suggestionhelp");
        String suggestionhelp = null;
//        if(suggestionhelpList!=null && suggestionhelpList.size()!=0){
//            suggestionhelp  = suggestionhelpList.toString();
//        }
        if (suggestionhelpobj instanceof String) {
            // 处理字符串类型的响应
             suggestionhelp = (String) suggestionhelpobj;
        } else if (suggestionhelpobj instanceof JSONArray) {
            // 处理数组类型的响应
            JSONArray suggestionhelpList = (JSONArray) suggestionhelpobj;
            suggestionhelp  = suggestionhelpList.toString();
        }

//        String suggestionhelp=(String)jsonObject.get("suggestionhelp");  //新增
        String type=(String)jsonObject.get("questionType");
        String question=(String)jsonObject.get("questionDescribe");
        String clues=(String)jsonObject.get("questionClues");
        String answer=(String)jsonObject.get("questionAnswer");
        Object supplementCluesobj=jsonObject.get("supplementClues");
        String supplementClues = null;
        if (supplementCluesobj instanceof String) {
            // 处理字符串类型的响应
            supplementClues = (String) supplementCluesobj;
        } else if (supplementCluesobj instanceof JSONArray) {
            // 处理数组类型的响应
            JSONArray supplementCluesList = (JSONArray) supplementCluesobj;
            supplementClues  = supplementCluesList.toString();
        }

        Object summaryobj = jsonObject.get("summary");
        String strSummarys = null;
        if (summaryobj instanceof String) {
            // 处理字符串类型的响应
            strSummarys = (String) summaryobj;
        } else if (summaryobj instanceof JSONArray) {
            // 处理数组类型的响应
            JSONArray strSummarysList = (JSONArray) summaryobj;
            strSummarys  = strSummarysList.toString();
        }

        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //获取用户登录账户（学生号）
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            System.out.println("用户:+"+user.getId()+"书:"+bookId+"日期："+new Date());
            userMapper.insertUserRead(user.getId(),bookId);
            QuestionForm QF=new QuestionForm();
            QF.setQuestion(question);
            QF.setAnswer(answer);
            QF.setModifyQuestion(modifyQuestion);
            QF.setSuggestionhelp(suggestionhelp);  //新增
            QF.setClues(clues);
            QF.setRobotQuestion(robotQuestion);
            QF.setUserId(user.getId());
            QF.setBookId(bookId);
            QF.setRate(rate);
            QF.setType(type);
            QF.setSummary(strSummarys);
            QF.setFinish_time(new Date());
            QF.setComplexity(0.0);
            QF.setImportance(0.0);
            QF.setFluency(0.0);
            QF.setSupplementClues(supplementClues);

            int result = userService.insertQuestionForm(QF);
            if(result!=0){
                List<QuestionForm> questionFormList = deepQuestionMapper.getQuestionFormByUserId(user.getId());
                int questionid = questionFormList.get(questionFormList.size()-1).getId();
                res.setData(questionid);
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

    @GetMapping("/user/heat")
    public ResultResponse getUserHeat(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //调用service层方法获得用户图书列表
            List<Heat> heatList=userService.getUserHeat(user.getId());
            res.setData(heatList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL);
        }
        return res;
    }


    @PostMapping("/user/regist")
    public ResultResponse regist(@RequestParam("registForm") String registForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(registForm);

        //获取用户信息
        String courseNum= (String) jsonObject.get("classifyNum");
        String password= (String) jsonObject.get("password");
        String yourname = (String) jsonObject.get("yourname");
        String username=(String) jsonObject.get("username");
        String sex=(String) jsonObject.get("sex");

        //插入用户信息
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setYourname(yourname);
        user.setSex(sex);

        //查询用户表单是否已经存在
        User searchUser = userService.findUserByUsername(username);
        if(searchUser!=null){
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+": 学号已注册，请根据学号与密码进行登录");
        }else {
            boolean result=userService.insertUser(user,courseNum);
            if(result){
                //验证成功，创建一个Token，封装到res对象中
                String token = JwtUtils.sign(user.getUsername(), "-1");
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
                res.setData(new Token(token));
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+": 注册失败，请检查课程码是否输入正确。");
            }
        }
        return res;
    }

    @PostMapping("/user/addCourse")
    public ResultResponse addCourse(@RequestParam("token") String token,@RequestParam("courseNum") String courseNum){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //加入课程
            boolean result=userService.addCourse(user,courseNum);
            if (result) {
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
            }else {
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"课程码错误！");
            }
        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"请重新登录！");
        }
        return res;
    }

    @PostMapping("/user/updateUserHypothesis")
    public ResultResponse updateUserHypothesis(@RequestParam("token") String token,@RequestParam("hypothesisName") String hypothesisName){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            user.setUsername(username);
            //加入课程
            userMapper.updateUserHypothesis(user.getId(),hypothesisName);

            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);

        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"请重新登录！");
        }
        return res;
    }

    @PostMapping("/user/readlog")
    public ResultResponse readLog(@RequestBody String behaviorform){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(behaviorform);
        String token = (String) jsonObject.get("userid");
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            String behavior = (String) jsonObject.get("behavior");
            int bookid = 0;
            if(jsonObject.get("bookid") instanceof Integer){
                bookid = (int)jsonObject.get("bookid");
            }else{
                bookid = Integer.parseInt((String)jsonObject.get("bookid"));
            }

            //加入课程
            System.out.println (user.getId()+ behavior + bookid+"时间"+ new Date() );
            userMapper.insertReadLog(user.getId(),behavior,bookid,new Date());
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);

        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"请重新登录！");
        }
        return res;
    }

    @GetMapping("/user/getAllUsers")
    public ResultResponse getAllUsers(){
        ResultResponse res=new ResultResponse();
        List<User> allUsers = userMapper.findAll();
        res.setData(allUsers);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/user/getUsersByCourseId")
    public ResultResponse getUsersByCourseId(@RequestParam("courseId") Integer courseId){
        ResultResponse res=new ResultResponse();
        if(courseId!=0){
            List<UserCourse> users = userMapper.getUserListByCourseid(courseId);
            res.setData(users);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }else {
            res.setData(null);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);
        }

        return res;
    }
    @PostMapping("/user/sendChatGPT")
    public ResultResponse sendChatGPT(@RequestParam("token") String token,@RequestParam("questionForm") String questionForm){
        ResultResponse res=new ResultResponse();
        //解析JSON字符串，取得用户名与密码信息以及登录token
        JSONObject jsonObject= JSONUtil.parseObj(questionForm);
        String clues=(String)jsonObject.get("clues");
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);
        if( tokenValue != null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            System.out.println("clues:"+clues);
            RestTemplate restTemplate = new RestTemplate();
            HttpHost proxy = new HttpHost("127.0.0.1", 10809);
            HttpClient httpClient = HttpClientBuilder.create().setProxy(proxy).build();
            HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();
            httpFactory.setConnectTimeout(60000);
            httpFactory.setReadTimeout(60000);
            restTemplate.setRequestFactory(httpFactory);

// 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("Authorization", "Bearer " + "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");
            headers.add("Authorization", "Bearer " + "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");

// 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", clues);
            requestBody.put("temperature", 1);
            requestBody.put("max_tokens", 1000);

// 构建请求
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

// 发送请求
            String url = "https://api.openai.com/v1/completions";
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);

//        String responseBody = responseEntity.getBody();
            JSONObject jsonObj= JSONUtil.parseObj(responseEntity.getBody());
            JSONArray choices = jsonObj.getJSONArray("choices");
            String text = (String)choices.getJSONObject(0).get("text");
            System.out.println("text:"+text);
            if(text != null){
                res.setData(text);
                res.setCode(Constants.STATUS_OK);
                res.setMessage(Constants.MESSAGE_OK);
            }else{
                //否则返回错误状态码
                res.setCode(Constants.STATUS_FAIL);
                res.setMessage(Constants.MESSAGE_FAIL+"请求错误！");
            }


    }else {
        //否则返回错误状态码
        res.setCode(Constants.STATUS_FAIL);
        res.setMessage(Constants.MESSAGE_FAIL+"登录信息已失效，请重新登录后提交");
    }
        return res;

    }
    @GetMapping("/user/getUsersTestScore")
    public ResultResponse getUsersTestScore(@RequestParam("token") String token){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
//            User user = userService.findUserByUsername(username);
            System.out.print("username:"+token);
            List<TestScore> testscores = userMapper.getUserTestScore(username);
            System.out.print("testscores:"+testscores);
            res.setData(testscores);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);

        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"请重新登录！");
        }
        return res;
    }


        @GetMapping("/user/getUsersquestionById")
    public ResultResponse getUsersquestionById(@RequestParam("token") String token,@RequestParam("bookid")Integer bookid){
        ResultResponse res=new ResultResponse();
        //验证token的合法和有效性
        String tokenValue=JwtUtils.verify(token);

        if(tokenValue!=null && tokenValue.startsWith(JwtUtils.TOKEN_SUCCESS)) {
            //如果合法则返回用户信息
            String username = tokenValue.replaceFirst(JwtUtils.TOKEN_SUCCESS, "");
            User user = userService.findUserByUsername(username);
            List<QuestionForm> questionFormList = userMapper.getUserQuestions(user.getId(),bookid);

            System.out.print("questionFormList:"+questionFormList);
            res.setData(questionFormList);
            res.setCode(Constants.STATUS_OK);
            res.setMessage(Constants.MESSAGE_OK);

        }else {
            //否则返回错误状态码
            res.setCode(Constants.STATUS_FAIL);
            res.setMessage(Constants.MESSAGE_FAIL+"请重新登录！");
        }
        return res;
    }







}
