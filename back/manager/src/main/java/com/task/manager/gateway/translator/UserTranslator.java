package com.task.manager.gateway.translator;

import com.task.manager.domain.User;
import com.task.manager.gateway.request.NewUserRequest;
import com.task.manager.gateway.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserTranslator {

    public User toDomain(NewUserRequest newUserRequest) {
        return User.builder()
                   .firstName(newUserRequest.firstName())
                   .lastName(newUserRequest.lastName())
                   .email(newUserRequest.email())
                   .password(newUserRequest.password())
                   .role(newUserRequest.role())
                   .build();
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
