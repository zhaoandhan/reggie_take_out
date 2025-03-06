package reggie.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ClassName:RedisConfig
 * Package:
 * Description:用于配置Spring应用中与Redis交互的RedisTemplate
 *配置自定义的RedisTemplate
 * 覆盖Spring Boot默认的RedisTemplate配置，修改Key的序列化策略，避免默认的JDK序列化导致Redis中存储的Key不可读。
 * 代码核心作用：修改Redis的Key序列化方式为字符串，提升可读性。
 * 典型应用场景：需要直接通过Redis命令行或工具查看Key时，避免二进制乱码。
 * @Author 赵琪
 * @Create 2025-03-06 17:23
 * @Version 1.0
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
        //默认的Key序列化器为：JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //Key序列化器：从默认的JdkSerializationRedisSerializer（二进制序列化，这种难以阅读）改为StringRedisSerializer（字符串序列化）
        //指定Key的序列化方式为字符串，解决默认JDK序列化导致Key显示为乱码的问题
        redisTemplate.setConnectionFactory(connectionFactory);
        //将RedisConnectionFactory设置到RedisTemplate中，因为RedisTemplate需要知道如何连接到Redis服务器。连接工厂的配置是在application.yml中通过配置参数指定的，比如主机名、端口、密码等。Spring Boot会自动根据这些配置创建RedisConnectionFactory的Bean，然后在这里通过参数注入进来
        return redisTemplate;
    }
}
