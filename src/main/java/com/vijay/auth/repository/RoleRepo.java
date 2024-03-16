package com.vijay.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.auth.entity.Role;



public interface RoleRepo extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String name);

}
