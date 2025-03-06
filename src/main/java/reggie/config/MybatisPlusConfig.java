package reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:MybatisPlusConfig
 * Package:
 * Description:配置MP的分页插件
 *
 * @Author 赵琪
 * @Create 2025-03-02 17:19
 * @Version 1.0
 */
@Configuration//标记该类为 Spring 配置类，表示该类包含一个或多个被 @Bean 注解的方法，用于定义 Spring 容器管理的 Bean。
//替代传统的 XML 配置文件，通过 Java 代码配置 Bean。在应用启动时，Spring 会扫描并执行该类中的 @Bean 方法，将返回的对象注册为 Bean
public class MybatisPlusConfig {
    @Bean//标记一个方法，指示该方法返回的对象应由 Spring 容器管理。默认以方法名作为 Bean 的名称（如 mybatisPlusInterceptor）。Bean 的类型由方法返回类型决定（此处为 MybatisPlusInterceptor）
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();//创建 MybatisPlusInterceptor 实例,是 MyBatis-Plus 的核心拦截器容器,MyBatis-Plus 支持通过 MybatisPlusInterceptor 添加多种插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());//添加分页插件,MyBatis-Plus 通过拦截器（Interceptor）扩展功能，需将插件添加到拦截器链中才能生效
        //PaginationInnerInterceptor 的功能:
        // 自动分页处理,在 SQL 执行前，根据分页参数（当前页、每页条数）动态拼接 LIMIT 语句。自动查询总记录数（需配合 Page<T> 对象使用）。
        //支持多种数据库：根据数据库类型（如 MySQL、Oracle）自动适配分页语法
        return mybatisPlusInterceptor;
    }
}
