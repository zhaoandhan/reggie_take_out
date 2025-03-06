package reggie.common;

/**
 * ClassName:MyMetaObjecthandler
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-03 9:05
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//客户端发送的每次http请求，对应的在服务端都会分配一个新的线程来处理，在处理过程中，
// 涉及到LoginCheckFilter类的dofilter方法、EmployeeController类的update方法、MyMetaObjecthandler类的updateFill方法都属于相同的一个线程
/**
 * 自定义元数据对象处理器
 */
@Component
//Component注解是一个通用注解，可以用在任何类上，包括普通的Java类、业务逻辑组件、持久化对象等。通过这个注解，Spring会自动去创建这个注解标识的类的实例对象注入到Spring的ioc容器中。
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {//在MyMetaObjecthandler类中是不能获得HttpSession对象的，就不能通过session获取用户id
    //ThreadLocal为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问
    //可以在loginCheckFilter的doFilter方法中获取当前登录用户id，并调用ThreadLocal的set方法来设置当前线程的线程局部变量的值（用户id），然后在本类的方法中调用ThreadLocal的get方法来获得当前线程所对应的线程局部变量的值（用户id）
    /**
     * 插入操作，自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }

    /**
     * 更新操作，自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
