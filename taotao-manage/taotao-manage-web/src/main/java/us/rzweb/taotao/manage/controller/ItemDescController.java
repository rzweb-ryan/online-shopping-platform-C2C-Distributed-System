package us.rzweb.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import us.rzweb.taotao.manage.pojo.ItemDesc;
import us.rzweb.taotao.manage.service.ItemDescService;

/**
 * Created by RZ on 8/6/16.
 */
@RequestMapping("item/desc")
@Controller
public class ItemDescController {
    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping(value ="{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> getItemCatByParentId(@PathVariable(value = "itemId") Long itemId) {
        try {
            ItemDesc itemDesc = itemDescService.queryById(itemId);
            if(itemDesc!=null){
                return ResponseEntity.ok(itemDesc);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
