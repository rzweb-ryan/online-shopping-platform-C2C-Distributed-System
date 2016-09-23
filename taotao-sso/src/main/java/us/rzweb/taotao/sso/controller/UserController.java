package us.rzweb.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import us.rzweb.taotao.common.utils.CookieUtils;
import us.rzweb.taotao.sso.pojo.User;
import us.rzweb.taotao.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RZ on 9/2/16.
 */
@RequestMapping("user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public final String COOKIE_NAME = "TT_TOKEN";

    @RequestMapping(value = "query/{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryByToken(token);
            if (null == user) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }



    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam ("username") String username,
                                       @RequestParam ("password") String password,
                                       HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<>();
        try {
            String token = this.userService.doLogin(username, password);
            if(null == token) {
                ret.put("status", 400);
            }else {
                ret.put("status", 200);
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("status", 500);
        }
        return ret;

    }


    @RequestMapping(value="register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
                                         @PathVariable("type") Integer type) {
        try {
            Boolean ret = userService.check(param, type);
            if(null == ret) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            //和前端设计冲突,取反妥协处理
            return ResponseEntity.ok(!ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {

        Map<String, Object> ret = new HashMap<>();
        if(bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> msgs = new ArrayList<>();
            for(ObjectError objectError : errors) {
                msgs.add(objectError.getDefaultMessage());
            }
            ret.put("status", 400);
            ret.put("data", StringUtils.join(msgs, "|"));
            return ret;
        }
        Boolean bool = userService.saveUser(user);
        if (bool) {
            ret.put("status", 200);
        } else {
            ret.put("status",300);
            ret.put("data","是的!");
        }
        return ret;
    }

}
