package com.authentication.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.authentication.authentication.model.LoginUser;
import com.authentication.authentication.repository.LoginUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserService {

    private final LoginUserRepository loginUserRepository;

    public LoginUser getUserDetails(String userName, String password, String role) {
        return loginUserRepository.findByUserNameAndPasswordAndRole(userName, password, role);
    }

    public LoginUser getUserByUserName(String userName) {
        return loginUserRepository.findByUserName(userName);
    }
}
