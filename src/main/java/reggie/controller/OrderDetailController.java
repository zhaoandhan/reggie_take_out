package reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reggie.common.R;
import reggie.entity.Orders;
import reggie.service.OrderDetailService;
import reggie.service.OrderService;

/**
 * ClassName:OrderDetailController
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-06 11:19
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
}
