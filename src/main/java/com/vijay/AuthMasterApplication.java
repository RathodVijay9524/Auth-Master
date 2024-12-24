package com.vijay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vijay.auth.entity.Role;
import com.vijay.auth.entity.User;
import com.vijay.auth.entity.Worker;
import com.vijay.auth.repository.UserRepository;
import com.vijay.auth.repository.WorkerRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class AuthMasterApplication {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;


    public static void main(String[] args) {
        SpringApplication.run(AuthMasterApplication.class, args);
    }

    @PostConstruct
    protected void init() {

        if (userRepository.count() == 0 && workerRepository.count() == 0) {

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(createRole("ROLE_ADMIN"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(createRole("ROLE_SUPER_USER"));

            Set<Role> normalRoles = new HashSet<>();
            normalRoles.add(createRole("ROLE_NORMAL"));

            Set<Role> workerRoles = new HashSet<>();
            workerRoles.add(createRole("ROLE_WORKER"));


            // Create users
            User admin = User.builder()
                    .name("Vimal Kumar")
                    .email("admin@gmail.com")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(adminRoles)
                    .build();

            User user = User.builder()
                    .name("Ajay Rawat")
                    .email("user@gmail.com")
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .roles(userRoles)
                    .build();


            User normalUser = User.builder()
                    .name("Vijay Rathod")
                    .email("normal@gmail.com")
                    .username("normal")
                    .password(passwordEncoder.encode("normal"))
                    .roles(normalRoles)
                    .build();


            // Save users
            userRepository.saveAll(Arrays.asList(admin, user, normalUser));

            Worker worker = Worker.builder()
                    .name("Salman Khan")
                    .email("worker@gmail.com")
                    .username("worker")
                    .password(passwordEncoder.encode("worker"))
                    .roles(workerRoles)
                    .user(admin)
                    .build();
            workerRepository.save(worker);
        }
    }
    private Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
