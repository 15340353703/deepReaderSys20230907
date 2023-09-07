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
public class FrontReadLog {
    private int id;
    private int userId;
    private String bookname;
    private String behavior;
    private Date stamptime;
}
