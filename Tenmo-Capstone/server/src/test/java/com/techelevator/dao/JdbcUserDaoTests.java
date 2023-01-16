package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{


    private static final User USER_1 = new User(1001,"bob", "plantlover", "admin");
    private static final User USER_2 = new User(1002,"john", "jellyfish", "USER");

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }
    @Test
    public void findIdByUsername(){
        int userId = sut.findIdByUsername("bob");
        Assert.assertEquals(userId, USER_1.getId());
    }

    @Test
    public void findAllUsers() {
        List<User> users = sut.findAll();
        Assert.assertEquals(2, users.size());
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    private void assertUserMatch(User expected, User actual) {
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getAuthorities(), actual.getAuthorities());

    }
}
