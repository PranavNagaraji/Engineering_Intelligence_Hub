package com.pranav.engineering_intelligence_hub.mapper;

import org.springframework.stereotype.Component;

import com.pranav.engineering_intelligence_hub.dto.request.UserRequest;
import com.pranav.engineering_intelligence_hub.dto.response.UserResponse;
import com.pranav.engineering_intelligence_hub.entity.User;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

    public User toEntity(UserRequest req){
        User user=new User();
        user.setUsername(req.username()); //since it is a record, the getters and setters are not same as a bean
        user.setEmail(req.email());
        return user;
    }
}
