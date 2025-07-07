package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(OAuth2User oauth2User) {
        String oauth2Id = oauth2User.getAttribute("sub");

        return userRepository.findByOauthId(oauth2Id)
                .orElseThrow(() -> new RuntimeException("Cannot find user with : " + oauth2Id));
    }

    @Override
    public User createNewUser(OAuth2User oauth2User) {
        User user = User.builder()
                .oauthId(oauth2User.getAttribute("sub"))
                .email(oauth2User.getAttribute("email"))
                .firstName(oauth2User.getAttribute("given_name"))
                .lastName(oauth2User.getAttribute("family_name"))
                .picture(oauth2User.getAttribute("picture"))
                .build();
        return userRepository.save(user);
    }
}
