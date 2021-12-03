package com.authentication.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authentication.authentication.model.CustomUserDetails;
import com.authentication.authentication.model.LoginUser;
import com.authentication.authentication.model.ManagedUserLogin;
import com.authentication.authentication.repository.ManagedUserLoginRepository;

@Service
public class AuthenticateService {

    @Autowired
    private UserService userService;

    @Autowired
    private ManagedUserLoginRepository managedUserLoginRepository;

    public CustomUserDetails loadUser(String username, String password,  String role) {

        if(role.equals("user")) {
            LoginUser loginUser = userService.getUserDetails(username, password, role);
            if (loginUser != null) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(loginUser.getRole()));
                return new CustomUserDetails(loginUser.getUserName(), String.valueOf(loginUser.getId()), "", authorities);
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } else {
            ManagedUserLogin managedUserLogin= managedUserLoginRepository.findByUserNameAndPasswordAndRole(username, password, role);
            if (managedUserLogin != null && managedUserLogin.getActiveStatus().equals("active")) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(managedUserLogin.getRole()));
                return new CustomUserDetails(managedUserLogin.getUserName(), String.valueOf(managedUserLogin.getId()), "", authorities);
            } else if(managedUserLogin != null && managedUserLogin.getActiveStatus().equals("inactive")) {
                throw new DisabledException("Account is disabled now. Please contact Administrator to activate account");
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }
    }

    public boolean validateUserWithRole(String username, String password,  String role) {
        if(username.equals("admin") && role.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}
