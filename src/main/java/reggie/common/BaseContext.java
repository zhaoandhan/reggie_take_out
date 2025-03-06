package reggie.common;

/**
 * ClassName:BaseContext
 * Package:
 * Description:基于ThreadLocal封装的工具类，用于保存和获取当前登录用户id
 *
 * @Author 赵琪
 * @Create 2025-03-03 10:14
 * @Version 1.0
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
