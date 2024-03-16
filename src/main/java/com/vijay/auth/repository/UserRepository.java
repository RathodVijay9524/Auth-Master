package com.vijay.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.auth.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
   
    Optional<User> findByEmailAndPassword(String email,String password);

    List<User> findByNameContaining(String keywords);

    Optional<User> findByName(String username);

}
