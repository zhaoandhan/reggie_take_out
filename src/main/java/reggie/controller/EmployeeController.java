package reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import reggie.common.R;
import reggie.entity.Employee;
import reggie.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * ClassName:EmployeeController
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 16:28
 * @Version 1.0
 */
@Slf4j
@RestController//Spring框架中的一个注解，用于标记一个类作为RESTful Web服务的控制器。它结合了@Controller和@ResponseBody注解，意味着类中的方法返回的数据会直接写入HTTP响应体中（如 JSON 或 XML），而不是渲染为视图。标记为REST控制器，自动处理返回数据的序列化
@RequestMapping("/employee")//定义请求的基本路径，用于将HTTP请求映射到控制器类或方法。在这里，表示该控制器处理所有以/employee开头的URL路径。例如，如果有一个方法@GetMapping("/list")，映射到/list，那么完整的路径就是`/employee/list
public class EmployeeController {//控制器类负责处理HTTP请求，通过EmployeeService调用服务层的方法，并返回RESTful格式的响应
    @Autowired//自动注入依赖的服务层Bean，Spring的依赖注入注解，用于自动装配Bean
    //在这里，它将EmployeeService的实例注入到employeeService变量中，使得控制器能够调用服务层的方法
    private EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {//客户端发送post请求的数据格式是json，用@RequestBody Employee employee封装成Employee对象，传入的json数据中的key要和Employee类中对应的属性名相同
        //HttpServletRequest request用于登陆成功后，把Employee对象的id存到session中，之后可通过request对象来get一个session,获取当前登录用户

        //1、将页面提交的密码password进行md5加密处理
        String password=employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());//使用了Lambda表达式来指定查询的字段和值，可以将这种表达式转换为SQL的WHERE子句
        //等价 SQL：SELECT * FROM employee WHERE username = '用户输入的username';
        //Employee::getUsername是方法引用，等价于 Lambda 表达式 (Employee e) -> e.getUsername()，指向Employee实体类的getUsername方法，作用是告诉 LambdaQueryWrapper，查询条件是 Employee 实体类的 username 字段（对应数据库表的 username 列）
        //添加查询条件：username 等于用户输入的 username
        //传统方式（硬编码字段名）是queryWrapper.eq("username", employee.getUsername())，字段名 "username" 是字符串，容易拼写错误，且代码重构时（如字段重命名），需要手动修改所有字符串
        Employee emp=employeeService.getOne(queryWrapper);//getOne是根据查询条件返回唯一结果，如果查询到多个结果会抛出异常，employee表中username是唯一的
        //调用 Service 层方法，根据条件查询一条记录
        //3、如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("用户名或密码错误");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){//这里直接比较字符串是否相等，前提是数据库存储的密码也是经过相同方式加密的
            return R.error("用户名或密码错误");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){//员工退出
        //清理session中保存的当前员工登录的id
        request.getSession().removeAttribute("employee");//之前保存的是什么名字，现在移除的还用那个名字
        return R.success("退出成功");
    }

    /**
     * 添加新的员工数据，前端通过ajax将数据传输到这里进行处理
     * @param employee
     * @return
     */
    @PostMapping//此处不用加路径，因为ajax发请求的url是/employ
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){//前端传入的数据是json格式的，@RequestBody Employee employee转成Employee格式
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));//设置初始密码123456，进行md5加密处理
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获得当前登录用户的id
//        Long empId=(Long) request.getSession().getAttribute("employee");//getAttribute返回object类型
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息的分页查询
     * @param page 第几页
     * @param pageSize 每页有几条信息
     * @param name 要查找的员工名字
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){//ajax发出的get请求中的数据不是json格式，所以这样子获取
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //添加一个过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);//StringUtils.isNotEmpty(name)用于判断name是否为空，为空则该条件相当于没有添加这个过滤条件
        //like模糊搜索
        //Employee::getName 是 方法引用（Method Reference）的写法，在 MyBatis-Plus 里，它相当于数据库中 Employee 表的 name 字段
        // 等价于queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);//查询的时候它会自己把结果赋好值，赋值到pageInfo里，所以不用专门搞个变量去接收
        //例子：SELECT * FROM employee WHERE name LIKE '%Tom%' ORDER BY update_time DESC LIMIT 5 OFFSET 0;
        //LIMIT 5 OFFSET 0 ：进行分页（查询第一页，返回 5 条数据）

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){//js处理数据时，只能保证前16位是精确的，employee表中的id长19位，多出来的几位数字会四舍五入，可以将数据类型转换成字符串进行处理防止精度损失
        //Long empId=(Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());公共字段的值由mybatis plus自动填充，不需要再自己写了
        //employee.setUpdateUser(empId);
        employeeService.updateById(employee);//employee中部分属性为null，这些不会被修改，只修改不是null的部分
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){//@PathVariable将URL中的路径参数绑定到控制器方法的参数上
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
