package us.rzweb.taotao.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.rzweb.taotao.common.bean.EasyUIResult;
import us.rzweb.taotao.manage.mapper.ContentMapper;
import us.rzweb.taotao.manage.pojo.Content;

import java.util.List;

/**
 * Created by RZ on 8/6/16.
 */
@Service
public class ContentService extends BaseService<Content> {
    @Autowired
    private ContentMapper contentMapper;
    public EasyUIResult queryEasyUIResult(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> contents = contentMapper.queryListByCategoryId(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<>(contents);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
}