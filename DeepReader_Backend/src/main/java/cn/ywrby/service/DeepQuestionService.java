package cn.ywrby.service;

import cn.ywrby.domain.AnswerForm;
import cn.ywrby.domain.QuestionForm;

import java.util.List;

public interface DeepQuestionService {
//    List<QuestionForm> getQuestionFormByBookId(Integer bookId);

    List<AnswerForm> getAnswerFormByUserId(int userId, Integer bookId);
    AnswerForm getAnswerForm(String answerFormStr);

    boolean insertAnswerForm(AnswerForm answerForm);
}
