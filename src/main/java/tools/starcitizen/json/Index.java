package tools.starcitizen.json;

import lombok.Getter;
import lombok.Setter;
import tools.starcitizen.enums.ShowTypeEnum;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Index {

    private String version;
    private List<IndexEntity> index = new LinkedList<>();
    private List<TypeIndex> typeList = ShowTypeEnum.SHOW;

}
