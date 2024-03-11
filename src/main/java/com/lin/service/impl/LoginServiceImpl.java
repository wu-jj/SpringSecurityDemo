package com.lin.service.impl;

import com.lin.domain.ResponseResult;
import com.lin.service.LoginService;
import com.lin.domain.LoginUser;
import com.lin.domain.User;
import com.lin.utils.JwtUtil;
import com.lin.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authentication);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("认证失败");
        }
        //
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);

        redisCache.setCacheObject("login:"+userId,loginUser);

        return new ResponseResult(200,"登录成功",map);
    }
}
