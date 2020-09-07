package com.example.oauth.service;

import com.example.oauth.pojo.User;
import com.example.oauth.pojo.UserDetailsPrincipal;
import com.example.oauth.repository.ScopeRepository;
import com.example.oauth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;

    public CustomUserDetailsService(final UserRepository userRepository, final ScopeRepository scopeRepository) {
        this.userRepository = userRepository;
        this.scopeRepository = scopeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String name) throws UsernameNotFoundException {
        final User userDetail = userRepository.findByEmail(name);

        if (userDetail == null) {
            throw new BadCredentialsException("User " + name + " not found.");
        }
        final String scopes = scopeRepository.getScopes(userDetail.getRole());

        final User accountByUsername = userDetail;
        accountByUsername.setScopes(scopes);

        final User user = accountByUsername;
        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new UsernameNotFoundException("User not authorized.");
        }
        return new UserDetailsPrincipal(user);
    }
}