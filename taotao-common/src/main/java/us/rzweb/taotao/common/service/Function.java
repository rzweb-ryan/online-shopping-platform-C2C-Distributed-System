package us.rzweb.taotao.common.service;

/**
 * Created by RZ on 8/23/16.
 */
public interface Function <T,E>{
    public T callback(E e);
}
