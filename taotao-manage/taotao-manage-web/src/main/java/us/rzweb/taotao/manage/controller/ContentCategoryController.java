package us.rzweb.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import us.rzweb.taotao.manage.pojo.ContentCategory;
import us.rzweb.taotao.manage.service.ContentCategoryService;

import java.util.Date;
import java.util.List;

/**
 * Created by RZ on 8/20/16.
 */
@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping(method = RequestMethod.GET)   //get list
    public ResponseEntity<List<ContentCategory>> queryCatByParentId(
            @RequestParam(value="id",defaultValue = "0") Long parentId) {
        try {
            ContentCategory contentCategory = new ContentCategory();
            contentCategory.setParentId(parentId);
            List<ContentCategory> contentCategoryList = this.contentCategoryService.queryListByWhere(contentCategory);
            if(null==contentCategory)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            return ResponseEntity.ok(contentCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * save new
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)//
    public ResponseEntity<Void> saveCat(ContentCategory contentCategory) {
        try {
            contentCategory.setStatus(1);
            contentCategory.setSortOrder(1);
            contentCategory.setIsParent(false);

            this.contentCategoryService.save(contentCategory);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * rename
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> updateCat(ContentCategory contentCategory) {
        try {
            this.contentCategoryService.updateSelective(contentCategory);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    /**
     * delete
     * @param contentCategory
     * @return
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCat(ContentCategory contentCategory) {

        try {
            this.contentCategoryService.deleteByCategories(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }



}
