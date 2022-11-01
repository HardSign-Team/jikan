package com.hardsign.server.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
    private final static int workload = 12;

    public String Hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(workload));
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}