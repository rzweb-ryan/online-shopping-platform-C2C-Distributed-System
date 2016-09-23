package us.rzweb.taotao.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.rzweb.taotao.common.service.ApiService;
import us.rzweb.taotao.search.pojo.Item;

import java.io.IOException;

/**
 * Created by RZ on 9/19/16.
 */
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private ObjectMapper MAPPER = new ObjectMapper();
    @Value("${TAOTAO_MANAGE_URL}")
    public static String TAOTAO_MANAGE_URL;
    public Item queryItemById(Long itemId) {
        String url = "http://manage.taotao.rzweb.us/rest/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
