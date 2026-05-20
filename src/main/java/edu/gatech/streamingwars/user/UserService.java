package edu.gatech.streamingwars.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    AppUser registerUser(AppUser appUser);

    List<Role> getRoles();
}


