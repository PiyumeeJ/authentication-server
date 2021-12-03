package com.authentication.authentication.repository;

import org.springframework.data.repository.CrudRepository;

import com.authentication.authentication.model.LoginUser;
import com.authentication.authentication.model.ManagedUserLogin;

public interface ManagedUserLoginRepository extends CrudRepository<ManagedUserLogin, Long> {

    ManagedUserLogin findByUserNameAndPasswordAndRole(String userName, String password, String role);

    ManagedUserLogin findByUserName(String userName);
}
