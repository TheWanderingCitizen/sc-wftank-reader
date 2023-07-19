package tools.starcitizen.json;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
public class TypeIndex {

    private String typeKey;
    private String type;
    private String typeCn;
    private Set<Integer> sizeSet = new TreeSet<>();
    private Set<Integer> gradeSet = new TreeSet<>();

    public TypeIndex(String typeKey, String type, String typeCn) {
        this.typeKey = typeKey;
        this.type = type;
        this.typeCn = typeCn;
    }
}
