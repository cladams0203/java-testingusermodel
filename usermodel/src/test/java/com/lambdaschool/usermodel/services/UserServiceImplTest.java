package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import net.bytebuddy.description.method.ParameterList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<User> userList = userService.findAll();
        for (User u : userList){
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
    }

    @Test
    public void findUserById() {
        assertEquals("admin", userService.findUserById(4).getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findUserByIdNotFound() {
        assertEquals("admin", userService.findUserById(100).getUsername());
    }

    @Test
    public void findByNameContaining() {
        assertEquals(1, userService.findByNameContaining("cin").size());
    }

    @Test
    public void findAll() {
        assertEquals(5, userService.findAll().size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete() {
        User userToDelete = new User("deleteMe", "taco", "test@test.com");
        userToDelete.getRoles().add(new UserRoles(userToDelete, roleService.findByName("admin")));
        userToDelete.getUseremails().add(new Useremail(userToDelete, "sure@aol.com"));
        userService.save(userToDelete);


        userService.delete(15);
        assertEquals(15, userService.findUserById(15).getUserid());
    }


    @Test
    public void findByName() {
        assertEquals("puttat", userService.findByName("puttat").getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByNameNotFound() {
        assertEquals("puttat", userService.findByName("putta").getUsername());
    }

    @Test
    public void save() {

        User userToAdd = new User("addme", "taco", "test@test.com");
        userToAdd.getRoles().add(new UserRoles(userToAdd, roleService.findByName("admin")));
        userToAdd.getUseremails().add(new Useremail(userToAdd, "sure@aol.com"));

        User addUser = userService.save(userToAdd);
        assertNotNull(addUser);
        assertEquals("addme", addUser.getUsername());

    }
    @Test(expected = EntityNotFoundException.class)
    public void putInvalidId(){
        User invalid = new User();
        invalid.setUserid(100);
        userService.save(invalid);
    }

    @Test
    public void update() {
        User updateUser = new User();
        updateUser.setUsername("misterkitty");
        updateUser.setUserid(14);
        updateUser.setPassword("taco2");
        updateUser.setPrimaryemail("testing@aol.com");
        updateUser.getUseremails().add(new Useremail(updateUser, "surething@aol.com"));
        User newUpdate = userService.update(updateUser, 14);
        assertEquals("misterkitty", userService.findUserById(14).getUsername());
    }

    @Test
    public void deleteAll() {
    }
}