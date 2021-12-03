package com.authentication.authentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.authentication.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
