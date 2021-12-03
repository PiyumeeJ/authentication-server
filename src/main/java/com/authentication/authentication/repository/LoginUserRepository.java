package com.authentication.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.authentication.model.LoginUser;

public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

    public LoginUser findByUserName(String userName);

    public LoginUser findByUserNameAndPasswordAndRole(String userName, String password, String role);
}
