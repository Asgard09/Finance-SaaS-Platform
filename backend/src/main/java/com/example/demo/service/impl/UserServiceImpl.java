package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(OAuth2User oauth2User) {
        String oauth2Id = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");

        return userRepository.findByOauthId(oauth2Id)
                .map(user -> {
                    log.debug("Found existing user by OAuth ID: {}", user.getEmail());
                    return user;
                })
                .orElseGet(() -> {
                    // If not found by oauthId, check by email
                    return userRepository.findByEmail(email)
                            .map(user -> {
                                log.debug("Found existing user by email: {}", user.getEmail());
                                // If we found a user by email but with a different oauthId, we might want to update the oauthId
                                if (!oauth2Id.equals(user.getOauthId())) {
                                    log.info("Updating OAuth ID for user with email {}", email);
                                    user.setOauthId(oauth2Id);
                                    return userRepository.save(user);
                                }
                                return user;
                            })
                            .orElseGet(() -> {
                                log.info("Creating new user with email: {}", email);
                                return createNewUser(oauth2User);
                            });
                });
    }

    @Override
    public User createNewUser(OAuth2User oauth2User) {
        Objects.requireNonNull(oauth2User, "OAuth2User must not be null");

        String email = oauth2User.getAttribute("email");
        String oauth2Id = oauth2User.getAttribute("sub");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");
        String picture = oauth2User.getAttribute("picture");

        log.debug("Creating new user: email={}, oauth2Id={}, firstName={}, lastName={}",
                email, oauth2Id, firstName, lastName);
        try {
            User user = User.builder()
                    .oauthId(oauth2Id)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .picture(picture)
                    .build();
                    
            User savedUser = userRepository.save(user);
            log.info("Successfully created new user with ID: {} and email: {}", savedUser.getId(), email);
            return savedUser;
        } catch (Exception e) {
            log.error("Error creating new user: {}", e.getMessage(), e);
            return findExistingUserEmail(e, email);
        }
    }

    private User findExistingUserEmail(Exception e, String email) {
        if (email != null) {
            log.info("Attempting to find existing user by email after creation error: {}", email);
            return userRepository.findByEmail(email)
                    .map(existingUser -> {
                        log.info("Found existing user by email after creation failed: {}", existingUser.getEmail());
                        return existingUser;
                    })
                    .orElseThrow(() -> {
                        log.error("Failed to create or retrieve user by email: {}", email);
                        return new RuntimeException("Failed to create or retrieve user", e);
                    });
        }
        throw new RuntimeException("Failed to create user and no email to lookup", e);
    }
}
