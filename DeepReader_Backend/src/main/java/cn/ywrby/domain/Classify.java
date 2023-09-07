package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Classify {
    private int id;
    private String name;
    private String work;
    private int courseId;
    private int displayMode;
    private List<Book> children;
    private Timestamp start_date;
    private Timestamp end_date;
}
