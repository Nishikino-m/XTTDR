package com.xttdr.cotroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.xttdr.common.Result;
import com.xttdr.entity.Account;
import com.xttdr.entity.User;
import com.xttdr.service.LoginServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {

    @Resource
    LoginServiceImpl loginService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Account account){
        System.out.println("#############Login Controller: Account="+account.toString());
        return loginService.login(account);
    }

    @PostMapping("/register")
        public Result<?> register(@RequestBody JsonNode jsonNode) {
        System.out.println("*******Register*******"+jsonNode.toString());

        Account account=new Account();
        account.setUserType(jsonNode.get("userType").asText());
        account.setId(jsonNode.get("id").asText());
        account.setPwd(jsonNode.get("pwd").asText());
        String work_id = jsonNode.get("work_id").asText();
        User user=new User();
        user.setWorkId(work_id);
        account.setUser(user);
        return loginService.register(account);
    }

    @GetMapping("/account/{id}")
    public Result<?> getAccountById(@PathVariable String id){
        return loginService.getAccountById(id);
    }

}
