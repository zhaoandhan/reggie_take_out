package reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * ClassName:GlobalExceptionHandler
 * Package:
 * Description:全局异常处理
 *
 * @Author 赵琪
 * @Create 2025-03-02 14:20
 * @Version 1.0
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//限定该全局异常处理器仅处理标注了 @RestController 或 @Controller 的控制器类抛出的异常
@ResponseBody//将方法的返回值直接写入 HTTP 响应体（如 JSON 格式）
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//捕获JDBC 抛出的异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){//检查异常信息是否包含 "Duplicate entry"
            //用sql语句往emplyee表插入一条新的数据，username重复的时候会报这种错误，例如：Duplicate entry 'admin' for key 'idx_username'
            String[] split=ex.getMessage().split(" ");//根据空格分割
            String msg=split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)//捕获自己定义的这个抛出的异常
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
