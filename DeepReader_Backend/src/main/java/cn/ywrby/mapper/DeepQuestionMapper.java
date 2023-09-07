package cn.ywrby.mapper;

import cn.ywrby.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DeepQuestionMapper {
//    public List<QuestionForm> getQuestionFormByBookId(Integer bookId);
    public List<QuestionForm> getQuestionFormByBookAndUser(int userId,int bookId);

    public List<QuestionForm> getQuestionsBybook(int bookid);

    public List<AnswerForm> getAnswersBybook(int bookid);

    public List<QuestionForm> getAllQuestions();

    public List<ReadLog> getAllReadlog(int userid);

    public List<ReadLog> getReadlogByBookId(int userid,int bookid);

    public List<KWL> getKWLList();

    public List<KWL> getKWLListBybookId(int bookid);

    public List<SummaryQuestion> getHighlightsBybook(int bookid);

    public List<SummaryQuestion> getHighlightsByUser(int userid);

    public int getLoginCount();

    public int getLoginCountByUser(int userid);

    public List<QuestionForm> getQuestionFormByUserId(int userId);

    public List<AnswerForm> getAnswerFormByUserId(int userId, Integer bookId);

    List<AnswerForm> getAnswerFormByUserAndQuestion(int userId,int questionId);

    public List<UserHighlight> getUserhighlight(int courseid);

    int insertAnswerForm(AnswerForm answerForm);

    void insertQuestionfeedback(int userid, int bookid, String questionfeedback, String feeling, String usefulrate, String usefulcause, Date datetime);

    void insertAnnotationfeedback(int userid,int bookid, String annotationfeedback,String feeling,String usefulrate,String usefulcause, Date datetime);

    void insertUserSummary(int userId,int questionid, String summary, Date datetime);

    void updateQuestionForm(int userid, int questionid,double fluency,double importance,double complexity);

    void insertSummaryQuestion(int userid, int questionid, String questionfeedback, String questionfeedbackfeeling,
                               String questionfeedbackusefulrate, String questionfeedbackusefulcause,
                               String annotationfeedback,String annotationfeedbackfeeling,String annotationfeedbackusefulrate,String annotationfeedbackusefulcause,Date timestamp);
}
