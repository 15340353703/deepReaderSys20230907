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
public class AnswerForm {
    private int id;
    private int userId;
    private int bookId;
    private int questionId;
    private String answer;
    private int fluency;
//    private int relevance;
    private int complexity;
    private int importance;
    private String opinion;
    private Date finish_time;
    private String judge;
}
