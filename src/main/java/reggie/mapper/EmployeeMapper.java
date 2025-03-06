package reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import reggie.entity.Employee;

/**
 * ClassName:EmployeeMapper
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 15:41
 * @Version 1.0
 */
@Mapper//标识该接口为 MyBatis 的 Mapper 接口，Spring 会通过动态代理生成其实现类，并注入到容器中,如果不想在每个 Mapper 接口上添加 @Mapper，可以在启动类上使用 @MapperScan("reggie.mapper") 批量扫描指定包下的 Mapper 接口
public interface EmployeeMapper extends BaseMapper<Employee> {//BaseMapper是MyBatis-Plus中的一个接口，里面定义了很多常用的数据库操作方法，比如selectById、insert、update等,当一个Mapper接口继承BaseMapper并指定泛型类型时，就可以直接使用这些方法，而不需要自己写SQL
}
