package us.rzweb.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.rzweb.taotao.common.service.ApiService;
import us.rzweb.taotao.common.service.HttpResult;
import us.rzweb.taotao.web.bean.Order;
import us.rzweb.taotao.web.bean.User;
import us.rzweb.taotao.web.threadlocal.UserThreadLocal;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String submitOrder(Order order) {
        User user = UserThreadLocal.get(); // 从本地线程中获取User对象
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        try {
            String url = TAOTAO_ORDER_URL + "/order/create";
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if (httpResult.getCode().intValue() == 200) {
                String jsonData = httpResult.getData();
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                if (jsonNode.get("status").intValue() == 200) {
                    // 订单提交成功，返回订单号
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order queryOrderById(String orderId) {
        String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
