package com.example.oauth.controller;


import com.example.oauth.util.UserHelper;
import com.example.oauth.util.UserPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class UserController {


    @UserPermissions(UserHelper.SCOPE_WRITE)
    @PostMapping("/write")
    public String bar(@RequestHeader("Authorization") String auth) {
        return "write";
    }

    @UserPermissions(UserHelper.SCOPE_READ)
    @PostMapping("/read")
    public String read(@RequestHeader("Authorization") String auth) {
        return "read";
    }
}
