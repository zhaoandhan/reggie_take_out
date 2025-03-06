package reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reggie.dto.DishDto;
import reggie.entity.Dish;
import reggie.entity.DishFlavor;
import reggie.mapper.DishMapper;
import reggie.service.DishFlavorService;
import reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:DishServiceImpl
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-03 12:07
 * @Version 1.0
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);//因为DishDto继承Dish
        Long dishId=dishDto.getId();//菜品Id
        List<DishFlavor> flavors=dishDto.getFlavors();//菜品口味
        flavors=flavors.stream().map((item)->{
                item.setDishId(dishId);//给falvors中的每个口味加上菜品id
                return item;
        }).collect(Collectors.toList());//把数据重新转成list形式
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);//saveBatch批量保存
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从Dish表查询
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);//拷贝
        //查询当前菜品对应口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        log.info(dishDto.toString());
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据————dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        List<DishFlavor> flavors=dishDto.getFlavors();//菜品口味
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());//给falvors中的每个口味加上菜品id
            return item;
        }).collect(Collectors.toList());//把数据重新转成list形式
        //添加当前提交过来的口味数据————dish_flavor表的insert操作
        dishFlavorService.saveBatch(flavors);
    }
}
