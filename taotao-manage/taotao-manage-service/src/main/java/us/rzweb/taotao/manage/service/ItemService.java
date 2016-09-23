package us.rzweb.taotao.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import us.rzweb.taotao.common.service.ApiService;
import us.rzweb.taotao.manage.pojo.Item;
import us.rzweb.taotao.manage.pojo.ItemDesc;
import us.rzweb.taotao.manage.pojo.ItemParamItem;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RZ on 8/6/16.
 */
@Service
public class ItemService extends BaseService<Item>{
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemParamItemService itemParamitemService;
    @Autowired
    private ApiService apiService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public final ObjectMapper MAPPER = new ObjectMapper();
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;
    public void saveItem(Item item, String desc, String itemParams) {
        item.setStatus(1);
        item.setId(null);
        this.save(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDescService.save(itemDesc);
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        itemParamitemService.save(itemParamItem);
    }

    public PageInfo<Item> queryPageList(Integer page, Integer rows) {
        Example example = new Example(Item.class);
        example.setOrderByClause("updated desc");
        PageHelper.startPage(page,rows);
        List<Item> list= this.mapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    public void updateItem(Item item, String desc, String itemParams) {

        item.setStatus(null);
        item.setCreated(null);
        super.updateSelective(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);
        itemParamitemService.updateParam(item.getId(),itemParams);
        try {
            sendMsg(item.getId(), "update");
//          String url = TAOTAO_WEB_URL+"/item/cache/"+item.getId()+".html";
//          apiService.doPost(url,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(Item item) {

        item.setStatus(2);
        item.setUpdated(new Date());
        super.updateSelective(item);
        String url = TAOTAO_WEB_URL+"/item/cache/"+item.getId()+".html";
        try {
            apiService.doPost(url,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteItem(Item item) {

        item.setStatus(3);
        item.setUpdated(new Date());
        super.updateSelective(item);
        String url = TAOTAO_WEB_URL+"/item/cache/"+item.getId()+".html";
        try {
            apiService.doPost(url,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Long itemId, String type) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", type);
            msg.put("itemId", itemId);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
