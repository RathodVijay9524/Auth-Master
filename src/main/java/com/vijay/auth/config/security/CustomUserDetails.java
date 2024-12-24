package com.vijay.auth.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vijay.auth.entity.Role;
import com.vijay.auth.entity.User;
import com.vijay.auth.entity.Worker;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String username;
    private String password;
    private String email;
    private Set<Role> roles;
    private List<Worker> workers;

    public CustomUserDetails(Long id,String name,String username, String password, String email, Set<Role> roles, List<Worker> workers) {
        this.id = id;
    	this.name = name;
    	this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.workers = workers;
    }

    public static CustomUserDetails build(User user) {
        return new CustomUserDetails(
        		user.getId(),
        		user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles(),
                user.getWorkers()
        );
    }
    
    public static CustomUserDetails build(Worker worker) {
        return new CustomUserDetails(
        		worker.getId(),
        		worker.getName(),
                worker.getUsername(),
                worker.getPassword(),
                worker.getEmail(),
                worker.getRoles(),
                null // Workers don't have associated workers, so set it to null
        );
    }
    
  

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }
    
   
   

    @Override
    public String getUsername() {
        return username;
    }

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
    // Implement other UserDetails methods as needed
}




