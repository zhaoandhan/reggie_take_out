package reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.entity.Category;

/**
 * ClassName:CategoryService
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-03 10:48
 * @Version 1.0
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
