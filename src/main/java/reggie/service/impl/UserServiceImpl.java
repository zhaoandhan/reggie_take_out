package reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.entity.User;
import reggie.mapper.UserMapper;
import reggie.service.UserService;

/**
 * ClassName:UserServiceImpl
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-05 10:56
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
