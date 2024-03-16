package com.vijay.auth.request;

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
public class RegistraonRequest {
	
    private String name;
    private String username;
    private String email;
    private String password;

}
