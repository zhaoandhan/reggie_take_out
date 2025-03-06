package reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reggie.common.R;
import reggie.entity.User;
import reggie.service.UserService;
import reggie.utils.SMSUtils;
import reggie.utils.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ClassName:UserController
 * Package:
 * Description:手机验证码登录过程：
 * 1、前端输入手机号，点击获取验证码，发送ajax请求，在服务端调用短信服务API给指定手机号发送验证码短信
 * 2、在登陆页面输入验证码，点击登录按钮，发送ajax请求，在服务端处理登录请求
 * @Author 赵琪
 * @Create 2025-03-05 11:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){//前端传来手机号码
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云提供的短信服务API完成发送短信
            SMSUtils.sendMessage("瑞吉外卖","",phone,code);//第一个参数是申请好的签名，第二个是模板，第三个是用户手机号，第四个是验证码
            //需要将生成的验证码存到Session
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }
    /**
     * 移动端用户登录,有验证码的情况，但是发验证码要前
     * @param map
     * @param session
     * @return
     */
//    @PostMapping("/login")
//    public R<User> login(@RequestBody Map map, HttpSession session){//前端传来手机号码
//        log.info(map.toString());
//        //获取手机号
//        String phone = map.get("phone").toString();
//        //获取验证码
//        String code = map.get("code").toString();
//        //从Session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);
//        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
//        if(codeInSession!=null&&codeInSession.equals(code)){
//            //如果能够比对成功，说明登陆成功
//            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getPhone,phone);
//            User user = userService.getOne(queryWrapper);
//            if(user==null) {//判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
//                user=new User();
//                user.setPhone(phone);
//                user.setStatus(1);
//                userService.save(user);
//            }
//            session.setAttribute("user",user.getId());
//            return R.success(user);
//        }
//        return R.error("登陆失败");
//    }

    /**
     * 移动端用户登录,可以直接登录的情况,没搞验证码
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){//前端传来手机号码
        String phone=map.get("phone").toString();
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(queryWrapper);
        if(user==null) {//判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            user=new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user",user.getId());
        return R.success(user);
    }
}
