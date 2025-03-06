package reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import reggie.common.BaseContext;
import reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName:LoginCheckFilter
 * Package:
 * Description:检查用户是否完成登录
 *
 * @Author 赵琪
 * @Create 2025-03-02 9:13
 * @Version 1.0
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//WebFilter注解用于声明一个Servlet过滤器，指定过滤器的名称和要拦截的URL模式。在这里，urlPatterns = "/*"表示该过滤器会拦截所有的HTTP请求。
//每次客户端请求到达服务器时，该过滤器会优先执行，根据逻辑决定是否放行请求
//Spring Boot默认可能不会自动扫描并注册这个注解。需要@ServletComponentScan来告诉Spring Boot去哪里扫描这些注解的类
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();//路径匹配器，支持通配符
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI=request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        String[] urls=new String[]{//不需要过滤的请求路径
            "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登录
        };
        //当访问 http://localhost:8080/backend/index.html 时，虽然该页面的静态资源路径（如 HTML、CSS、JS）在过滤器的白名单中，但页面加载后，前端通过 Ajax 请求的 API 接口路径未在白名单中，这个页面右侧的数据就需要通过ajax请求来获取
        //2、判断本次请求是否需要处理
        boolean check=check(urls,requestURI);
        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1、判断登录状态，如果已登录，则直接放行，管理端
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long empId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);//获取当前用户id，存起来
            filterChain.doFilter(request,response);
            return;
        }
        //4-2、判断登录状态，如果已登录，则直接放行，用户端
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);//获取当前用户id，存起来
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据，返回 JSON 错误信息
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));//在backend/request.js中，描写了这种情况会返回登陆页面
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for(String url:urls){
            boolean match=PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
