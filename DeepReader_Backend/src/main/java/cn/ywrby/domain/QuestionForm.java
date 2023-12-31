package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionForm {
    private int id;
    private String question;
    private String type;
    private String answer;
    private String clues;
    private String summary;
    private String robotQuestion;
    private int rate;
    private String modifyQuestion;
    private int userId;
    private Integer bookId;
    private Date finish_time;
    private double fluency;
    private double importance;
    private double complexity;
    private AnswerForm youranswer;

    private String suggestionhelp;
    private String supplementClues;
}
