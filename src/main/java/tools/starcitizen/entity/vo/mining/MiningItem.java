package tools.starcitizen.entity.vo.mining;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MiningItem {

    private String name;
    private Double price;
    private Double percentage = 0d;
    private String color;

}
