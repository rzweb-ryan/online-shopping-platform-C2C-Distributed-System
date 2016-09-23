package us.rzweb.taotao.manage.service;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import us.rzweb.taotao.manage.pojo.ItemParamItem;

import java.util.Date;

/**
 * Created by RZ on 8/6/16.
 */
@Service
public class ItemParamItemService extends BaseService<ItemParamItem>{
    public void updateParam(Long itemId, String itemParams) {
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setUpdated(new Date());
        itemParamItem.setParamData(itemParams);

        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId",itemId);
        this.mapper.updateByExample(itemParamItem,example);
    }
}
