package cn.ywrby.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.ywrby.domain.AnswerForm;
import cn.ywrby.domain.QuestionForm;
import cn.ywrby.mapper.DeepQuestionMapper;
import cn.ywrby.service.DeepQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeepQuestionServiceImpl implements DeepQuestionService {

    @Autowired
    DeepQuestionMapper dqMapper;


//    @Override
//    public List<QuestionForm> getQuestionFormByBookId(Integer bookId) {
//        List<QuestionForm> questionForms=dqMapper.getQuestionFormByBookId(bookId);
//        return questionForms;
//    }

    @Override
    public List<AnswerForm> getAnswerFormByUserId(int userId, Integer bookId) {
        List<AnswerForm> answerForms=dqMapper.getAnswerFormByUserId(userId, bookId);
        return answerForms;
    }

    @Override
    public AnswerForm getAnswerForm(String answerFormStr) {
        AnswerForm answerForm=new AnswerForm();
        JSONObject jsonObject= JSONUtil.parseObj(answerFormStr);
        int bookId= (int) jsonObject.get("bookId");
        int questionId= (int) jsonObject.get("questionId");
        String answer= (String) jsonObject.get("answer");
        int fluency= (int) jsonObject.get("fluency");
//        int relevance= (int) jsonObject.get("relevance");
        int complexity= (int) jsonObject.get("complexity");
        int importance= (int) jsonObject.get("importance");
        String opinion= (String) jsonObject.get("opinion");
        String judge= (String) jsonObject.get("judge");
        answerForm.setBookId(bookId);
        answerForm.setQuestionId(questionId);
        answerForm.setAnswer(answer);
        answerForm.setFluency(fluency);
//        answerForm.setRelevance(relevance);
        answerForm.setComplexity(complexity);
        answerForm.setImportance(importance);
        answerForm.setOpinion(opinion);
        answerForm.setJudge(judge);
        return answerForm;
    }

    @Override
    public boolean insertAnswerForm(AnswerForm answerForm) {
        int answerId= dqMapper.insertAnswerForm(answerForm);
        System.out.println(answerId);
        if(answerId!=0){
            return true;
        }else {
            return false;
        }
    }
}
