package reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reggie.common.CustomException;
import reggie.dto.SetmealDto;
import reggie.entity.Setmeal;
import reggie.entity.SetmealDish;
import reggie.mapper.SetmealMapper;
import reggie.service.SetmealDishService;
import reggie.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:SetmealServiceImpl
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-03 12:09
 * @Version 1.0set
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional//操作这两张表要么全成功，要么全失败
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal,执行insert操作
        this.save(setmealDto);
        List<SetmealDish> setmealDishes=setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{//stream流在高数据量下效率比foreach高，同时并发性也比较好
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }
    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可用删除，只有停售的套餐才可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        //select count(*) from setmeal where id in (id1,id2,id3,...) and status = 1
        if(count>0){//如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，先删套餐表中的数据--setmeal
        this.removeByIds(ids);//批量删除
        //删除关系表中的数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //delete from setmeal_dish where setmeal_id in (id1,id2,id3,...)
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
