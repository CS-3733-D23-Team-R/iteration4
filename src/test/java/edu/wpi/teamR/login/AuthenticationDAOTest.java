package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationDAOTest {

    private static AuthenticationDAO authenticationDAO;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    @BeforeAll
    static void startup() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaToTest();
        authenticationDAO = new AuthenticationDAO();
    }
    @AfterAll
    static void end() throws SQLException, ClassNotFoundException {
        authenticationDAO.deleteAllUsers();
        Configuration.getConnection().close();
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        authenticationDAO.deleteAllUsers();
    }

    @Test
    void addUser() throws SQLException, ClassNotFoundException {
        ArrayList<User> users;
        User user, user2;

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());

        user = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());
        user2 = users.get(0);
        assertEquals(user.getStaffUsername(), user2.getStaffUsername());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getJobTitle(), user2.getJobTitle());
        assertEquals(user.getPhoneNum(), user2.getPhoneNum());
        assertEquals(fmt.format(user.getJoinDate()), fmt.format(user2.getJoinDate()));
        assertEquals(user.getAccessLevel(), user2.getAccessLevel());
        assertEquals(user.getDepartment(), user2.getDepartment());
    }

    @Test
    void modifyUserByUsername() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        ArrayList<User> users;
        User user, user2;

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());

        user = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());
        user2 = users.get(0);
        assertEquals(user.getStaffUsername(), user2.getStaffUsername());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getJobTitle(), user2.getJobTitle());
        assertEquals(user.getPhoneNum(), user2.getPhoneNum());
        assertEquals(fmt.format(user.getJoinDate()), fmt.format(user2.getJoinDate()));
        assertEquals(user.getAccessLevel(), user2.getAccessLevel());
        assertEquals(user.getDepartment(), user2.getDepartment());

        user = authenticationDAO.modifyUserByUsername("username1", "password2", "name2", "email2", "jobTitle2", "987654321", new Date(System.currentTimeMillis()-1000000000), AccessLevel.Staff, "dept2", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());
        user2 = users.get(0);
        assertEquals(user.getStaffUsername(), user2.getStaffUsername());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getJobTitle(), user2.getJobTitle());
        assertEquals(user.getPhoneNum(), user2.getPhoneNum());
        assertEquals(fmt.format(user.getJoinDate()), fmt.format(user2.getJoinDate()));
        assertEquals(user.getAccessLevel(), user2.getAccessLevel());
        assertEquals(user.getDepartment(), user2.getDepartment());
    }

    @Test
    void deleteALLUsers() throws SQLException, ClassNotFoundException {
        ArrayList<User> users;
        User user, user2;

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());

        user = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());

        user = authenticationDAO.addUser("username2", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(2, users.size());

        user = authenticationDAO.addUser("username3", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(3, users.size());

        authenticationDAO.deleteAllUsers();

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());
    }

    @Test
    void deleteUserByUsername() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        ArrayList<User> users;
        User user, user2;

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());

        user = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());

        user = authenticationDAO.addUser("username2", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(2, users.size());

        user = authenticationDAO.addUser("username3", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(3, users.size());

        authenticationDAO.deleteUserByUsername("username1");

        users = authenticationDAO.getUsers();
        assertEquals(2, users.size());

        authenticationDAO.deleteUserByUsername("username2");

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());
        user2 = users.get(0); //comparing username3 User to database user to ensure they are same
        assertEquals(user.getStaffUsername(), user2.getStaffUsername());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getJobTitle(), user2.getJobTitle());
        assertEquals(user.getPhoneNum(), user2.getPhoneNum());
        assertEquals(fmt.format(user.getJoinDate()), fmt.format(user2.getJoinDate()));
        assertEquals(user.getAccessLevel(), user2.getAccessLevel());
        assertEquals(user.getDepartment(), user2.getDepartment());
    }

    @Test
    void getUserByUsername() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        ArrayList<User> users;
        User user1, user2, user3, compareUser;

        users = authenticationDAO.getUsers();
        assertEquals(0, users.size());

        user1 = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);

        users = authenticationDAO.getUsers();
        assertEquals(1, users.size());

        user2 = authenticationDAO.addUser("username2", "password2", "name2", "email2", "jobTitle2", "1234567899", new Date(System.currentTimeMillis()-1000000), AccessLevel.Staff, "dept2", 0);

        users = authenticationDAO.getUsers();
        assertEquals(2, users.size());

        user3 = authenticationDAO.addUser("username3", "password3", "name3", "email3", "jobTitle3", "1234567898", new Date(System.currentTimeMillis()-2000000), AccessLevel.Admin, "dept3", 0);

        users = authenticationDAO.getUsers();
        assertEquals(3, users.size());

        compareUser = authenticationDAO.getUserByUsername("username1");
        assertEquals(user1.getStaffUsername(), compareUser.getStaffUsername());
        assertEquals(user1.getPassword(), compareUser.getPassword());
        assertEquals(user1.getName(), compareUser.getName());
        assertEquals(user1.getEmail(), compareUser.getEmail());
        assertEquals(user1.getJobTitle(), compareUser.getJobTitle());
        assertEquals(user1.getPhoneNum(), compareUser.getPhoneNum());
        assertEquals(fmt.format(user1.getJoinDate()), fmt.format(compareUser.getJoinDate()));
        assertEquals(user1.getAccessLevel(), compareUser.getAccessLevel());
        assertEquals(user1.getDepartment(), compareUser.getDepartment());

        compareUser = authenticationDAO.getUserByUsername("username2");
        assertEquals(user2.getStaffUsername(), compareUser.getStaffUsername());
        assertEquals(user2.getPassword(), compareUser.getPassword());
        assertEquals(user2.getName(), compareUser.getName());
        assertEquals(user2.getEmail(), compareUser.getEmail());
        assertEquals(user2.getJobTitle(), compareUser.getJobTitle());
        assertEquals(user2.getPhoneNum(), compareUser.getPhoneNum());
        assertEquals(fmt.format(user2.getJoinDate()), fmt.format(compareUser.getJoinDate()));
        assertEquals(user2.getAccessLevel(), compareUser.getAccessLevel());
        assertEquals(user2.getDepartment(), compareUser.getDepartment());

        compareUser = authenticationDAO.getUserByUsername("username3");
        assertEquals(user3.getStaffUsername(), compareUser.getStaffUsername());
        assertEquals(user3.getPassword(), compareUser.getPassword());
        assertEquals(user3.getName(), compareUser.getName());
        assertEquals(user3.getEmail(), compareUser.getEmail());
        assertEquals(user3.getJobTitle(), compareUser.getJobTitle());
        assertEquals(user3.getPhoneNum(), compareUser.getPhoneNum());
        assertEquals(fmt.format(user3.getJoinDate()), fmt.format(compareUser.getJoinDate()));
        assertEquals(user3.getAccessLevel(), compareUser.getAccessLevel());
        assertEquals(user3.getDepartment(), compareUser.getDepartment());
    }

    @Test
    void checkUserAcceptance() throws SQLException, ItemNotFoundException {
        User user1, user2, user3, user4, user5;
        user1 = authenticationDAO.addUser("username1", "password1", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);
        user2 = authenticationDAO.addUser("username2", "password2", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);
        user3 = authenticationDAO.addUser("username3", "password3", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);
        user4 = authenticationDAO.addUser("username4", "password4", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);
        user5 = authenticationDAO.addUser("username5", "password5", "name1", "email1", "jobTitle1", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "dept1", 0);
        System.out.println(user1.getPassword());
        System.out.println(user2.getPassword());
        System.out.println(user3.getPassword());
        System.out.println(user4.getPassword());
        System.out.println(user5.getPassword());

        boolean result;
        result = authenticationDAO.verifyUser("username1", "password1");
        assertTrue(result);
        result = authenticationDAO.verifyUser("username1", "password");
        assertFalse(result);
        assertThrows(ItemNotFoundException.class, () -> {
            authenticationDAO.verifyUser("username", "password");
        });
        result = authenticationDAO.verifyUser("username2", "password2");
        assertTrue(result);
        result = authenticationDAO.verifyUser("username3", "password3");
        assertTrue(result);
        result = authenticationDAO.verifyUser("username4", "password4");
        assertTrue(result);
        result = authenticationDAO.verifyUser("username5", "password5");
        assertTrue(result);
    }
}