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
public class SummaryQuestion {
    private int id;
    private int userid;
    private int questionid;
    private String questionfeedbackfeeling;
    private int questionfeedbackusefulrate;
    private String questionfeedbackusefulcause;
    private String readfeedbackfeeling;
    private int readfeedbackusefulrate;
    private String readfeedbackusefulcause;
    private String questionfeedback;
    private String readfeedback;
    private Date timestamp;
}
