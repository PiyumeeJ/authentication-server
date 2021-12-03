package com.authentication.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authentication.authentication.model.CustomUserDetails;
import com.authentication.authentication.model.LoginUser;
import com.authentication.authentication.model.ManagedUserLogin;
import com.authentication.authentication.repository.LoginUserRepository;
import com.authentication.authentication.repository.ManagedUserLoginRepository;

@Service
public class JwtAdminUserDetailsService implements UserDetailsService {

    @Autowired
    ManagedUserLoginRepository managedUserLoginRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        ManagedUserLogin loginUser = managedUserLoginRepository.findByUserName(username);

        if (loginUser != null)
        {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(loginUser.getRole()));
            return new CustomUserDetails(loginUser.getUserName(), String.valueOf(loginUser.getId()), "",
                    authorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}