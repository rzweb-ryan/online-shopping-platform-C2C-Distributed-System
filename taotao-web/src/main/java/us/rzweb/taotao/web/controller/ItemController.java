package us.rzweb.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import us.rzweb.taotao.manage.pojo.ItemDesc;
import us.rzweb.taotao.web.bean.Item;
import us.rzweb.taotao.web.service.ItemService;


@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        //基本数据
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        
        //描述数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        mv.addObject("itemDesc", itemDesc);
        
        //规格参数数据
        String itemParam = this.itemService.queryItemParamByItemId(itemId);
        mv.addObject("itemParam", itemParam);
        return mv;
    }

}
