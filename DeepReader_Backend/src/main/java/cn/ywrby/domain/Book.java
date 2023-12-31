package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    private int id;
    private String name;
    private String author;
    private Timestamp start_date;
    private Timestamp end_date;
    private int pages;
    private String brief_introduction;
    private String content;
    private int reading_stage;
    private String classify_name;
    private int classifyId;
    private int courseId;
    private String work;
    private boolean have_read;
    private boolean have_answer;
    private String url;
    private int wordcount;
}
