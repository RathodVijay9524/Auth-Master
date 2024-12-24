package com.vijay.auth.config.security;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vijay.auth.entity.User;
import com.vijay.auth.entity.Worker;
import com.vijay.auth.repository.UserRepository;
import com.vijay.auth.repository.WorkerRepository;

import java.util.Optional;


@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    	 Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
         if (userOptional.isPresent()) {
             User user = userOptional.get();
             return CustomUserDetails.build(user);
         } else {
             Optional<Worker> workerOptional = workerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
             Worker worker = workerOptional.orElseThrow(() -> new UsernameNotFoundException("Worker not found with username: " + usernameOrEmail));
             return CustomUserDetails.build(worker);
         }
    }
}


















// three user configured

//@Override
//public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//    Optional<User> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//    if (userOptional.isPresent()) {
//        User user = userOptional.get();
//        return CustomUserDetails.build(user);
//    } else {
//        Optional<Worker> workerOptional = workerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//        if (workerOptional.isPresent()) {
//            Worker worker = workerOptional.get();
//            return CustomUserDetails.build(worker);
//        } else {
//            Optional<Employee> employeeOptional = employeeRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//            Employee employee = employeeOptional.orElseThrow(() -> new UsernameNotFoundException("Employee not found with username: " + usernameOrEmail));
//            return CustomUserDetails.build(employee);
//        }
//    }
//}
