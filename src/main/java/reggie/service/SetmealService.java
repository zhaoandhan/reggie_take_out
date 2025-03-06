package reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.dto.SetmealDto;
import reggie.entity.Setmeal;

import java.util.List;

/**
 * ClassName:SetmealService
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-03 12:07
 * @Version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
