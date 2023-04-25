package edu.wpi.teamR.login;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class User {
    private String staffUsername, password, name, email, jobTitle, department, phoneNum, salt;
    private int imageID;
    private Date joinDate;
    private AccessLevel accessLevel;

    public User(String staffUsername, String password, String salt, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID){
        this.staffUsername = staffUsername;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.phoneNum = phoneNum;
        this.joinDate = joinDate;
        this.accessLevel = accessLevel;
        this.department = department;
        this.imageID = imageID;
    }
}
