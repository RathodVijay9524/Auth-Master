package com.vijay.auth.service;

import com.vijay.auth.entity.request.LoginRequest;
import com.vijay.auth.entity.request.RegistraonRequest;
import com.vijay.auth.entity.response.LoginJWTResponse;
import com.vijay.auth.entity.response.RegistraonResponse;

public interface AuthService {
	LoginJWTResponse login(LoginRequest req);

    String register(RegistraonRequest req);
    
    RegistraonResponse registerWorker(RegistraonRequest req);
}
