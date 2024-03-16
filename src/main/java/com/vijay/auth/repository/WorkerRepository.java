package com.vijay.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.auth.entity.Worker;



@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>{

	
	Optional<Worker> findByEmail(String email);

    Optional<Worker> findByUsernameOrEmail(String username, String email);

    Optional<Worker> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
