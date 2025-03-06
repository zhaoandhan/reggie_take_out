package reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import reggie.entity.Employee;

/**
 * ClassName:EmployeeService
 * Package:reggie.service
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 15:57
 * @Version 1.0
 */
//BaseMapper<T>	数据访问层（DAO 层）	直接操作数据库，提供基本的 CRUD 方法（如 insert, selectById, update）。
//IService<T>	业务逻辑层（Service 层）	在 DAO 层基础上，封装业务逻辑，提供更高层次的通用方法（如批量操作、逻辑删除）
public interface EmployeeService extends IService<Employee> {
    //继承MyBatis-Plus 提供的通用服务接口 IService,该接口 自动继承了 MyBatis-Plus 提供的通用 CRUD 方法，无需手动实现基础操作（如新增、删除、分页查询等），从而简化服务层代码

    //之后就可以用这个接口调用以下方法，如：EmployeeService.save(employee)
    // save(T entity)	插入一条记录
    //removeById(Serializable id)	根据主键删除记录
    //updateById(T entity)	根据主键更新记录
    //getById(Serializable id)	根据主键查询记录
    //list()	查询所有记录
    //page(Page<T> page)	分页查询（需配合分页插件）
}
