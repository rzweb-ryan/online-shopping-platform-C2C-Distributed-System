package us.rzweb.taotao.manage.mapper;

import tk.mybatis.mapper.common.Mapper;
import us.rzweb.taotao.manage.pojo.Content;

import java.util.List;

/**
 * Created by RZ on 8/20/16.
 */
public interface ContentMapper extends Mapper<Content> {
    List<Content> queryListByCategoryId(Long categoryId);
}
