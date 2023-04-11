package edu.wpi.teamR.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(lombok.AccessLevel.PACKAGE)
public class User {
    private String username, password;
    private AccessLevel accessLevel;
    public User(String username, String password, AccessLevel accessLevel){
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }
}
