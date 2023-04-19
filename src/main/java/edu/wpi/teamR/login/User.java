package edu.wpi.teamR.login;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class User {
    private String staffUsername, password, name, email, jobTitle, department, phoneNum;
    private Date joinDate;
    private AccessLevel accessLevel;

    public User(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department){
        this.staffUsername = staffUsername;
        this.password = password;
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.phoneNum = phoneNum;
        this.joinDate = joinDate;
        this.accessLevel = accessLevel;
        this.department = department;
    }
}
