package com.lin.service;


import com.lin.domain.ResponseResult;
import com.lin.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    ResponseResult login(User user);
}
