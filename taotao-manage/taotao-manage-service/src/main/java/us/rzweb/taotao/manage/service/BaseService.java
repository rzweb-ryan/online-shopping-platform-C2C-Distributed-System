package us.rzweb.taotao.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import us.rzweb.taotao.manage.pojo.BasePojo;

import java.util.Date;
import java.util.List;

/**
 * Created by RZ on 8/10/16.
 */
public class BaseService<T extends BasePojo> {

    @Autowired
    public Mapper<T> mapper;

    /**
     * queryById
     * @param id
     * @return
     */
    public T queryById(Long id){
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * select all
     * @return
     */
    public List<T> queryAll(){
        return mapper.selectAll();
    }

    /**
     * select one by example
     * @param record
     * @return
     */
    public T queryOne(T record){
        return mapper.selectOne(record);
    }

    /**
     * query by where
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record){
        return mapper.select(record);
    }
    /**
     * query by page info
     * @param page
     * @param rows
     * @param record
     */
    public PageInfo<T> queryPageListByWhere(Integer page,Integer rows, T record){
        PageHelper.startPage(page,rows);
        List<T> list = queryListByWhere(record);
        return new PageInfo<T>(list);
    }
    /**
     * insert new record
     */
    public Integer save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insert(record);
    }
    /**
     * update all property
     */
    public Integer upadate(T record){
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKey(record);
    }

    public Integer updateSelective(T record){
        record.setUpdated(new Date());
        return mapper.updateByPrimaryKeySelective(record);
    }
    /**
     * delete by Id
     * @param id
     */
    public Integer deleteById(Long id){
        return mapper.deleteByPrimaryKey(id);
    }
    /**
     * delete ids
     * @param clazz Class<T>
     * @param property String
     * @param  values List<Object>
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values){
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return mapper.deleteByExample(example);
    }

    /**
     * delete by where
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return mapper.delete(record);
    }
}
