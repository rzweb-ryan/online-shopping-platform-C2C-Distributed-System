package us.rzweb.taotao.manage.service;

import org.springframework.stereotype.Service;
import us.rzweb.taotao.manage.pojo.ContentCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RZ on 8/6/16.
 */
@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    @Override
    public Integer save(ContentCategory record) {
        Integer id = super.save(record);
        ContentCategory parent =  super.queryById(record.getParentId());
        if(!parent.getIsParent()) {
            parent.setIsParent(true);
            mapper.updateByPrimaryKeySelective(parent);
        }
        return id;
    }

    /**
     * delete category and sub categories
     * @param contentCategory
     */
    public void deleteByCategories(ContentCategory contentCategory) {
        List<Object> ids = new ArrayList<>();
        Long parentId = contentCategory.getParentId();
        ids.add(contentCategory.getId());
        findAllSubNode(contentCategory.getId(),ids);
        super.deleteByIds(ContentCategory.class,"id",ids);

        ContentCategory chs = new ContentCategory();
        chs.setParentId(parentId);
        List<ContentCategory> subs = super.queryListByWhere(chs);
        if(subs==null||subs.isEmpty()){
            ContentCategory parent = new ContentCategory();
            parent.setId(parentId);
            parent.setIsParent(false);
            super.updateSelective(parent);
        }

    }
    private void findAllSubNode(Long parentId, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        List<ContentCategory> list = super.queryListByWhere(record);
        for(ContentCategory it:list) {
            ids.add(it.getId());
            if(it.getIsParent()) findAllSubNode(it.getId(),ids);
        }
    }
}