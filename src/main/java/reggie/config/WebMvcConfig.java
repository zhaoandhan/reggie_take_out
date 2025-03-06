package reggie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import reggie.common.JacksonObjectMapper;

import java.util.List;

/**
 * ClassName:WebMvcConfig
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-01 11:00
 * @Version 1.0
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {//继承WebMvcConfigurationSupport会完全接管Spring MVC的配置，导致默认的静态资源处理失效，导致默认的静态资源路径无法访问，所以如果有默认的静态资源，代码中要添加默认资源处理。
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {//设置静态资源映射，注册资源处理器，告诉Spring MVC如何将URL路径映射到具体的文件位置
        //在Spring Boot项目中，静态资源通常包括HTML、CSS、JavaScript、图片等文件.
        // 默认情况下，Spring Boot会自动处理这些资源，放在特定的目录下，比如classpath:/static、classpath:/public 等，若资源存放在非默认路径（如 classpath:/front），需手动指定访问路径，有时候用户可能需要自定义这些资源的路径，或者项目结构不符合默认约定，这时候就需要手动配置静态资源映射
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        //通过/backend/index.html来访问前端页面，分别将请求路径/backend/**和/front/**映射到类路径下的/backend和/front目录，/backend/** 表示所有以 /backend/ 开头的请求（如 /backend/js/app.js
        //addResourceLocations指定了对应的资源位置，资源实际存储路径。这里的classpath:表示从类路径（即resources目录）下查找/backend 文件夹
    }

    /**
     * 扩展mvc框架的消息转换器，在项目启动时就会调用，并把自己设置的对象转换器放到converters集合里，converters一开始就有有自带的几个转换器
     * @param converters the list of configured converters to extend
     */
    //消息转换器将后端controller的返回结果转成相应的json，再通过输出流的方式相应给页面
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器追加到mvc框架的转换器容器集合中
        converters.add(0,messageConverter);//把自己设置的对象转换器放在最前面，如果放在最后面，就会使用别人的转换器
    }
}
