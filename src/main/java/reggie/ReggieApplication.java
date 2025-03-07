package reggie;

import lombok.extern.slf4j.Slf4j;//用lombok提供的一些注解编写实体类的时候get,set方法就可以省略了
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName:ReggieApplication
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 10:11
 * @Version 1.0
 */
@Slf4j//这个注解可以输出日志
@SpringBootApplication//这个注解组合了三个注解，启动时会自动扫描组件，加载配置，并开启自动配置，这些都是SpringApplication.run()触发的一系列动作
//包含注解如下：
// @Configuration：标记该类为配置类，允许定义 Bean（如 @Bean 注解的方法）。
//@EnableAutoConfiguration：启用 Spring Boot 的自动配置机制，根据项目依赖（如 MySQL、Redis）自动创建所需 Bean。
//@ComponentScan：默认扫描主类所在包及其子包下的组件（如 @Controller、@Service），将它们注册为 Bean
@ServletComponentScan
//该注解在 Spring Boot 中用于启用对 Servlet 组件（如 @WebFilter、@WebServlet、@WebListener）的自动扫描和注册
//Spring Boot 的自动配置主要针对 Spring 管理的 Bean（如 @Component、@Service），而 Servlet 组件（如 @WebFilter）属于 Java EE 标准，Spring Boot 默认不扫描这些注解。
@EnableTransactionManagement
@EnableCaching//开启spring cache缓存注解功能
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);//springboot应用启动的入口代码，ReggieApplication.class指定主配置类，告诉spring从哪里开始加载配置，args可以指定端口号或其他配置，可以动态配置
        //这行代码会初始化 Spring 容器（ApplicationContext），加载所有配置和组件（如 Controller、Service、DAO 等），并启动内嵌的 Web 服务器（如 Tomcat），加载Bean
        //1.创建 SpringApplication 实例，加载 ReggieApplication.class 作为主配置类，加载 application.yml 或 application.properties 中的配置
        //执行自动配置（@EnableAutoConfiguration），根据依赖spring-boot-starter-*（如 spring-boot-starter-web）初始化组件（如 MVC、Tomcat）。检测到 spring-boot-starter-data-redis 依赖时，自动配置 Redis 连接池
        //扫描 @Component、@Service、@Repository 等注解标记的 Bean，并注入到容器。
        //启动内嵌的 Web 服务器，监听指定的端口（如 server.port=8080）。
        log.info("项目启动成功...");//加@Slf4j注解就能使用这句代码了
    }
}
