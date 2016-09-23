package us.rzweb.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import us.rzweb.taotao.common.bean.EasyUIResult;
import us.rzweb.taotao.manage.pojo.Content;
import us.rzweb.taotao.manage.service.ContentService;

/**
 * Created by RZ on 8/20/16.
 */
@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Void> save(Content content) {
        try {
            content.setId(null);
            this.contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryList(@RequestParam(value="categoryId") Long categoryId,
                                                  @RequestParam("page") Integer page,
                                                  @RequestParam("rows") Integer rows) {
        try {
            EasyUIResult result = this.contentService.queryEasyUIResult(categoryId,page,rows);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
