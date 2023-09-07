package cn.ywrby.controller;

import cn.ywrby.domain.Book;
import cn.ywrby.domain.KWL;
import cn.ywrby.mapper.BookMapper;
import cn.ywrby.mapper.DeepQuestionMapper;
import cn.ywrby.res.ResultResponse;
import cn.ywrby.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KWLController {

    @Autowired
    DeepQuestionMapper deepQuestionMapper;

    @GetMapping("/kwl/allkwls")
    public ResultResponse allbooks(){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<KWL> list=deepQuestionMapper.getKWLList();

        res.setData(list);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }

    @GetMapping("/kwl/getKWLBybookId")
    public ResultResponse getKWLBybookId(@RequestParam("bookId") Integer bookId){
        ResultResponse res=new ResultResponse();
        //调用service层方法获得用户列表
        List<KWL> list=deepQuestionMapper.getKWLListBybookId(bookId);
        res.setData(list);
        res.setCode(Constants.STATUS_OK);
        res.setMessage(Constants.MESSAGE_OK);
        return res;
    }
}
