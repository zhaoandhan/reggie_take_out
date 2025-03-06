package reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reggie.service.OrderService;

/**
 * ClassName:OrderController
 * Package:
 * Description:
 *
 * @Author 赵琪
 * @Create 2025-03-06 11:14
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
}
