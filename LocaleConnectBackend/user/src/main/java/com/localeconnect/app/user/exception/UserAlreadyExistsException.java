package com.localeconnect.app.user.exception;

import com.localeconnect.app.user.model.User;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
