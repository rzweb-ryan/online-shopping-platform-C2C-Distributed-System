package us.rzweb.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import us.rzweb.taotao.manage.pojo.ItemCat;
import us.rzweb.taotao.manage.service.ItemCatService;

import java.util.List;

/**
 * Created by RZ on 8/6/16.
 */
@RequestMapping("item/cat")
@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> getItemCatByParentId(@RequestParam(value = "id",defaultValue = "0")Long parentId) {
        try {
            ItemCat record = new ItemCat();
            record.setParentId(parentId);
            List<ItemCat> list = itemCatService.queryListByWhere(record);
            if(null==list) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
