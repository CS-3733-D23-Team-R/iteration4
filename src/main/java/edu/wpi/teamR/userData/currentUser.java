package edu.wpi.teamR.userData;

import edu.wpi.teamR.login.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter@Setter
public class currentUser {
    String username;
    private String password;
    AccessLevel accessLevel;
    String fullName;
    String email;
    String department;
    Date joinDate;
    int phoneNum;
    String jobTitle;
    public currentUser(String username, String password, AccessLevel accessLevel, String fullname, String email, String department, Date joinDate, int phoneNum, String jobTitle){ //TODO: update this with alton's new user class
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.fullName = fullname;
        this.email = email;
        this.department = department;
        this.joinDate = joinDate;
        this.phoneNum = phoneNum;
        this.jobTitle = jobTitle;
    }
    public boolean comparePass(String passwordAttempt){
        return this.password.equals(passwordAttempt);
    }
}
