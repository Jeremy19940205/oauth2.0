package com.example.oauth.component.oauth;


import com.example.oauth.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AccountAuthenticationProvider(final CustomUserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        if (token.getCredentials() == null || userDetails.getPassword() == null) {
            throw new UsernameNotFoundException("Email or Password can't be null.");
        }
        if (!passwordEncoder.matches((String) token.getCredentials(), userDetails.getPassword())) {
             throw new UsernameNotFoundException("Password is incorrect.");
        }
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }
}
