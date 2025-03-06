package reggie.common;

/**
 * ClassName:CustomException
 * Package:
 * Description:自定义业务异常
 *
 * @Author 赵琪
 * @Create 2025-03-03 12:35
 * @Version 1.0
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
