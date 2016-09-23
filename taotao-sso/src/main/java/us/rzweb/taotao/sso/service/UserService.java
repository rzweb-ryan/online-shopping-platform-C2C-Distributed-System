package us.rzweb.taotao.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.rzweb.taotao.common.service.RedisService;
import us.rzweb.taotao.sso.mapper.UserMapper;
import us.rzweb.taotao.sso.pojo.User;

import java.io.IOException;
import java.util.Date;

/**
 * Created by RZ on 9/2/16.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private ObjectMapper mapper = new ObjectMapper();




    public Boolean check(String param, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;
            default:
                return null;
        }
        return userMapper.selectOne(record) == null;
    }

    public Boolean saveUser(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        return userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        if(null == user) {
            return null;
        }
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword()))
            return null;
        //write into redis
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);

        this.redisService.set("TOKEN_" + token, mapper.writeValueAsString(user), 60 * 30);

        return token;

    }

    public User queryByToken(String token) {
        String key = "TOKEN_" + token;
        String json = this.redisService.get(key);
        if (json ==  null) return null;
        try {
            this.redisService.expire(key, 30 * 60);
            User user = mapper.readValue(json, User.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
