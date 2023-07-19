package tools.starcitizen.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IndexEntity {

    private String id;
    private String name;
    private String nameCn;
    private String path;
    private String showTypeKey;
    private Integer size;
    private Integer grade;
}
