package us.rzweb.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import us.rzweb.taotao.manage.pojo.Item;
import us.rzweb.taotao.web.bean.Cart;
import us.rzweb.taotao.web.bean.Order;
import us.rzweb.taotao.web.bean.User;
import us.rzweb.taotao.web.service.CartService;
import us.rzweb.taotao.web.service.ItemService;
import us.rzweb.taotao.web.service.OrderService;
import us.rzweb.taotao.web.service.UserService;
import us.rzweb.taotao.web.threadlocal.UserThreadLocal;


@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    /**
     * 去订单确认页
     * 
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }

    /**
     * 基于购物车下单
     * 
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toCartOrder() {
        ModelAndView mv = new ModelAndView("order-cart");
        User user = UserThreadLocal.get();
        List<Cart> carts = this.cartService.queryCartListByUserId(user.getId());
        mv.addObject("carts", carts);
        return mv;
    }

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order) {
        Map<String, Object> result = new HashMap<String, Object>();
        String orderId = this.orderService.submitOrder(order);
        if (StringUtils.isEmpty(orderId)) {
            // 提交订单失败
            result.put("status", 300);
        } else {
            // 提交订单成功
            result.put("status", 200);
            result.put("data", orderId);
        }
        return result;
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId) {
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        // 当前时间向后推2天，格式化：01月26日
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
