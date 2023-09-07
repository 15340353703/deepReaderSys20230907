package cn.ywrby.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course {
    private int id;
    private int userId;
    private String courseNum;
    private String courseName;
    private String instructorName;
    private String content;
    private String courseContent;
    private int havegroup;
    private String groupnameHP;
}
