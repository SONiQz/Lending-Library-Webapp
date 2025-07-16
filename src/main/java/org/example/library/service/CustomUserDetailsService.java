package org.example.library.service;

import org.example.library.model.CustomUserDetails;
import org.example.library.model.operational.User;
import org.example.library.repository.OperationalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Service for managing the user's existence.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Call User Repo
    @Autowired
    private OperationalUserRepository userRepo;

    // Validate that the email exists.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

}