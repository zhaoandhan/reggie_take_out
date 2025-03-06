package reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reggie.common.R;
import reggie.dto.DishDto;
import reggie.entity.Category;
import reggie.entity.Dish;
import reggie.entity.DishFlavor;
import reggie.service.CategoryService;
import reggie.service.DishFlavorService;
import reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName:DishController
 * Package:
 * Description:菜品管理
 *
 * @Author 赵琪
 * @Create 2025-03-03 16:30
 * @Version 1.0
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    //新增菜品时：1、先发送ajax请求，请求服务端获取菜品分类数据，展示到下拉框中（在CategoryController中实现）
    //2、发送请求进行图片上传，请求服务端将图片保存到服务器（在CommonController中实现）
    //3、页面发送请求进行图片下载，将上传的图片进行回显
    //4、点击保存按钮，发送ajax请求，将菜品相关数据以json形式提交到服务端

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){//ajax发出的请求中，数据包含Dish表的数据还有口味信息，Dish表中不包含口味信息
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息的分页,1、先发送ajax请求，将分页查询数据(page、pageSize、name)提交到服务端，获取分页数据 2、页面发送请求，请求服务端进行图片下载，用于页面图片展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name!=null,Dish::getName,name);//name不为空时才添加这个过滤条件，like模糊查询
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");//对象拷贝,拷贝除records属性外的其他属性
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{//遍历records
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);//属性拷贝
            Long categoryId = item.getCategoryId();//每个菜品的分类id
            Category category = categoryService.getById(categoryId);//根据id查询分类对象
            if(category!=null){
                String categoryName=category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());//把这些dishDto收集起来变成集合
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    //修改菜品时：
    //1、发送ajax请求，请求服务器获取分类数据，用于菜品分类下拉框中数据展示
    //2、发送ajax请求，请求服务器，根据id查询当前菜品信息，用于菜品信息回显
    //3、页面发送请求，请求服务器端进行图片下载，用于图片回显
    //4、点击保存按钮，发送ajax请求，将修改后的菜品相关数据以json形式提交到服务器

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){//ajax发出的请求中，数据包含Dish表的数据还有口味信息，Dish表中不包含口味信息
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据条件来查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){//虽然传来的数据只有categoryId,但是这样比较通用
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);//添加条件，查询状态为1，表示起售
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list=dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){//虽然传来的数据只有categoryId,但是这样比较通用
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);//添加条件，查询状态为1，表示起售
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list=dishService.list(queryWrapper);
        List<DishDto> dishDtoList=list.stream().map((item)->{//遍历records
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);//属性拷贝
            Long categoryId = item.getCategoryId();//每个菜品的分类id
            Category category = categoryService.getById(categoryId);//根据id查询分类对象
            if(category!=null){
                String categoryName=category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();//当前菜品id
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            //select * from dish_flavor where dish_id = ?
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());//把这些dishDto收集起来变成集合
        return R.success(dishDtoList);
    }
}
