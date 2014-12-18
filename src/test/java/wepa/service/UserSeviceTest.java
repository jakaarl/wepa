package wepa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static wepa.service.UserService.IVAN;
import static wepa.service.UserService.JANI;
import static wepa.service.UserService.KRISTIAN;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import wepa.Application;
import wepa.domain.Role;
import wepa.domain.User;
import wepa.repository.RoleRepository;
import wepa.repository.UserRepository;
import wepa.service.UserService.UserBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserSeviceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Test
    public void shouldInitializeDefaultRolesToDb() {
        Role[] defaultRoles = { userService.getAdminRole(), userService.getUserRole() };
        List<Role> rolesInDb = roleRepository.findAll();
        assertEquals(defaultRoles.length, rolesInDb.size());
        
        for (Role role : defaultRoles) {
            assertTrue(rolesInDb.contains(role));
        }
    }
    
    @Test
    public void shouldInitializeDefaultUsersToDb() {
        List<User> usersInDb = userRepository.findAll();
        User[] defaultUsers = { IVAN, JANI, KRISTIAN };
        assertEquals(defaultUsers.length, usersInDb.size());
        
        for (User user : defaultUsers) {
            assertTrue(usersInDb.contains(user));
        }
    }
    
    @Test
    @Transactional
    public void shouldAddUserRole() {
        User user = UserBuilder
                .newUser("montyp", "w0nd3RFULspaM")
                .setEmail("spam.spam.spam@montypython.co.uk")
                .setName("Monty", "Python")
                .build();
        userService.save(user);
        Role userRole = userService.getUserRole();
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(userRole.getName());
        assertTrue(user.getRoles().contains(userRole));
        assertTrue(user.getAuthorities().contains(userAuthority));
    }

}
