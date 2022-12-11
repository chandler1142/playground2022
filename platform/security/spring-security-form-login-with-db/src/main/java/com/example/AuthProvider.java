package com.example;

import com.example.entities.Attempts;
import com.example.entities.User;
import com.example.repository.AttemptsRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthProvider implements AuthenticationProvider {

    private static final int ATTEMPTS_LIMIT = 30000;

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AttemptsRepository attemptsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("User is locked!");
        }

        Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(username);

        if(authentication.getCredentials() == null) {
            throw new BadCredentialsException("Password cannot bu empty!");
        }

        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            System.out.println("Username or password incorrect!");
            if (userAttempts.isPresent()) {
                Attempts attempts = userAttempts.get();
                int times = attempts.getAttempts() + 1;
                if (times < ATTEMPTS_LIMIT) {
                    attempts.setAttempts(times);
                    attemptsRepository.save(attempts);
                } else {
                    User currentUser = (User) userDetails;
                    currentUser.setAccountNonLocked(true);
                    userRepository.save(currentUser);
                    throw new LockedException("User is locked!");
                }
            } else {
                Attempts attempts = new Attempts();
                attempts.setUsername(username);
                attempts.setAttempts(1);
                attemptsRepository.save(attempts);
            }
            throw new BadCredentialsException("Password incorrect!");
        }

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, authentication.getCredentials().toString(), userDetails.getAuthorities());
        if (userAttempts.isPresent()) {
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(0);
            attemptsRepository.save(attempts);
        }

        return token;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
