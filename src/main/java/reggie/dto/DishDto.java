package reggie.dto;

import reggie.entity.Dish;
import reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
//DTO全称为Data Transfer Object,数据传输对象，一般用于展示层与服务层之间的数据传输
@Data
public class DishDto extends Dish {//定义这个类是因为前端要求的数据与现有实体类不是完全的匹配，比如现有Dish中不包括口味信息

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
