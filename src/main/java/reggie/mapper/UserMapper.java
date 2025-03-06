package reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import reggie.entity.User;

/**
 * ClassName:UserMapper
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-05 10:54
 * @Version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
