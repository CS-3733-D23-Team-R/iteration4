package edu.wpi.teamR.login;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class User implements Archivable {
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

    private User(String[] args) throws IndexOutOfBoundsException {
        this(args[0], args[1], args[2], args[3], args[4], args[5], args[6], Date.valueOf(args[7]), AccessLevel.valueOf(args[8]), args[9], Integer.parseInt(args[10]));
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{staffUsername, password, salt, name, email, jobTitle, phoneNum, joinDate.toString(), accessLevel.toString(), department, String.valueOf(imageID)};
    }

    @Override
    public String getCSVColumns() {
        return "staffUsername,password,salt,name,email,jobTitle,phoneNum,joinDate,accessLevel,department,imageID";
    }
}
