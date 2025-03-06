package reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import reggie.entity.Employee;
import reggie.mapper.EmployeeMapper;
import reggie.service.EmployeeService;

/**
 * ClassName:EmployeeServiceImpl
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 16:16
 * @Version 1.0
 */
@Service//将 EmployeeServiceImpl 类标记为 Spring 管理的 Bean，允许通过依赖注入（如 @Autowired）在其他组件（如 Controller）中使用该服务
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
//implements EmployeeService作用：实现自定义的业务接口 EmployeeService。
//如果 EmployeeService 中定义了额外的方法（如 findByDepartment），需要在 EmployeeServiceImpl 中实现这些方法。
//若无需自定义方法，接口可以为空（但为了代码规范性，建议显式定义）。
}
