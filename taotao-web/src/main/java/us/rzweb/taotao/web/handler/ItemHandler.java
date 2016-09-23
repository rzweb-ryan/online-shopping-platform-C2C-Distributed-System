package us.rzweb.taotao.web.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import us.rzweb.taotao.common.service.RedisService;
import us.rzweb.taotao.web.service.ItemService;

import java.io.IOException;

/**
 * Created by RZ on 9/19/16.
 */
public class ItemHandler {
    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void executeMessage(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String key = ItemService.REDIS_KEY + jsonNode.get("itemId").asLong();
            this.redisService.del(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
