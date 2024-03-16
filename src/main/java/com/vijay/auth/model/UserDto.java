package com.vijay.auth.model;

import java.util.Set;

import com.vijay.auth.entity.Role;
import com.vijay.auth.response.RegistraonResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	
    private Long id;
    private String name;
    
    private String username;
    
    private String email;
   
    private String password;

    private Set<Role> roles;
    
    //private User user;
}
