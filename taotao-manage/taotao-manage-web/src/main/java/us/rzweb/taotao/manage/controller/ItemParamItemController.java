package us.rzweb.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import us.rzweb.taotao.manage.pojo.ItemParamItem;
import us.rzweb.taotao.manage.service.ItemParamItemService;

/**
 * Created by RZ on 8/11/16.
 */
@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {
    @Autowired
    private ItemParamItemService itemParamItemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemParamItemController.class);
    @RequestMapping(value="{itemId}",method= RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") Long itemId) {
        try {
            ItemParamItem record = new ItemParamItem();
            record = itemParamItemService.queryById(record.getId());
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("查询商品参数,itemId={}",itemId);
            }
            if(null == record) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("查询商品参数成功,itemId={}",itemId);
            }
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询商品参数失败,itemId={}",itemId);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}