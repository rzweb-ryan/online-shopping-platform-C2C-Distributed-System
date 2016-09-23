package us.rzweb.taotao.manage.controller;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import us.rzweb.taotao.common.bean.EasyUIResult;
import us.rzweb.taotao.manage.pojo.Item;
import us.rzweb.taotao.manage.service.ItemService;

/**
 * Created by RZ on 8/11/16.
 */
@RequestMapping("item")
@Controller
public class ItemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET,value = "{itemId}")
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId")Long itemId) {
        Item item = this.itemService.queryById(itemId);
        try {
            if(null==item)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }



    /**
     * add item
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addItem(Item item, @RequestParam(value = "desc")String desc,String itemParams){
        try {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品, item={}, desc={}",item,desc);
            }
            if(StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            itemService.saveItem(item,desc,itemParams);
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品成功,itemId={}",item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("新增商品失败!title={},cid={}",item.getTitle(),item.getCid());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * get item list
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> getItemList(
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value="rows",defaultValue = "30") Integer rows
            ){
        try {
            PageInfo<Item> pageInfo = itemService.queryPageList(page, rows);
            EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * update item
     * @param item
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
                                           @RequestParam("itemParams") String itemParams) {
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("修改商品， item = {}, desc = {}", item, desc);
            }
            if (StringUtils.isEmpty(item.getTitle())) {
                // 响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            this.itemService.updateItem(item, desc, itemParams);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("修改商品成功， itemId = {}", item.getId());
            }

            // 成功 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("修改商品失败! title = " + item.getTitle() + ", cid = " + item.getCid(), e);
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
