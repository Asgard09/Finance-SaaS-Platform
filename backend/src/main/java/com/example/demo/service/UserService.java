package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findUser(OAuth2User oauth2User);
    User createNewUser(OAuth2User oauth2User);

}
