package cn.ywrby.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UEditorFile {
    private String state;
    private String url;
    private String title;
    private String original;
}
