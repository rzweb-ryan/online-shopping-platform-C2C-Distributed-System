package us.rzweb.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by RZ on 8/23/16.
 */
@Service
public class RedisService {
    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private <T> T excute(Function<T,ShardedJedis> fun) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(shardedJedis!=null)
                shardedJedis.close();
        }
        return null;
    }
    /**
     * set into redis
     */
    public String set(final String key, final String value) {
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    /**
     * get key
     */
    public String get(final String key){
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    /**
     * del
     * @param key
     */
    public Long del(final String key) {
        return this.excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }

        });
    }

    public String set(final String key, final String value, final Integer ttl) {
        return this.excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                String res = shardedJedis.set(key, value);
                shardedJedis.expire(key, ttl);
                return res;
            }
        });
    }

    public Long expire(final String key,final Integer ttl) {
        return this.excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, ttl);
            }
        });
    }
}
