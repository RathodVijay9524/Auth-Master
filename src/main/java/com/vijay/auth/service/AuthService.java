package com.vijay.auth.service;

import com.vijay.auth.request.LoginRequest;
import com.vijay.auth.request.RegistraonRequest;
import com.vijay.auth.response.LoginJWTResponse;
import com.vijay.auth.response.RegistraonResponse;

public interface AuthService {
	LoginJWTResponse login(LoginRequest req);

    String register(RegistraonRequest req);
    
    RegistraonResponse registerWorker(RegistraonRequest req);
}
