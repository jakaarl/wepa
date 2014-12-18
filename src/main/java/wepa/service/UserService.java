package wepa.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import wepa.domain.Role;
import wepa.domain.User;
import wepa.repository.RoleRepository;
import wepa.repository.UserRepository;

@Service
public class UserService {
    
    static final String USER_ROLE_NAME = "USER";
    static final String ADMIN_ROLE_NAME = "ADMIN";
    static final String[] DEFAULT_ROLES = { USER_ROLE_NAME, ADMIN_ROLE_NAME };
    static final User IVAN = UserBuilder
            .newUser("IvanTadic", "admin123")
            .setEmail("ivantadic014@gmail.com")
            .setName("Ivan", "Tadic")
            .build();
    static final User JANI = UserBuilder
            .newUser("JaniKaarela", "h3l3pp0PASSU!")
            .setEmail("jani.kaarela@gmail.com")
            .setName("Jani", "Kaarela")
            .build();
    static final User KRISTIAN = UserBuilder
            .newUser("KristianLauttamus", "admin123")
            .setEmail("kristian.lauttamus@gmail.com")
            .setName("Kristian", "Lauttamus")
            .build();
    private Role userRole;
    private Role adminRole;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
    }
    
    private void initRoles() {
        for (String name : DEFAULT_ROLES) {
            Role role = roleRepository.findByName(name);
            if (role == null) {
                role = new Role();
                role.setName(name);
                roleRepository.save(role);
            }
            if (USER_ROLE_NAME.equals(name)) {
                userRole = role;
            } else if (ADMIN_ROLE_NAME.equals(name)) {
                adminRole = role;
            }
        }
    }
    
    private void initUsers() {
        List<Role> roles = Arrays.asList(userRole, adminRole);
        User[] defaultUsers = { IVAN, JANI, KRISTIAN };
        for (User user : defaultUsers) {
            if (userRepository.findByEmail(user.getEmail()) == null) {
                user.setRoles(roles);
                save(user);
            }
        }
    }
    
    public Role getAdminRole() {
        return adminRole;
    }
    
    public Role getUserRole() {
        return userRole;
    }
    
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
    
    public User save(User user) {
        if (user.getId() == null && !user.getRoles().contains(userRole)) {
            user.getRoles().add(userRole); // everyone's a user!
        }
        return userRepository.save(user);
    }
    
    public User getAuthenticatedPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(((User) authentication.getPrincipal()).getEmail());
    }
    
    public User findUser(Long id){
        return userRepository.findOne(id);
    }

    public User getTestUser() {
       User user =  userRepository.findByEmail("testUser@mail.com");
       if (user == null){ 
            user = UserBuilder
                    .newUser("testUser", "testPassword")
                    .setEmail("testUser@mail.com")
                    .setName("Test", "User")
                    .build();
            return save(user);
       }
       return user;
    }
    
    static class UserBuilder {
        
        private User user = new User();
        
        static UserBuilder newUser(String username, String password) {
            UserBuilder builder = new UserBuilder();
            builder.user.setUsername(username);
            builder.user.setSalt(BCrypt.gensalt());
            builder.user.setPassword(BCrypt.hashpw(password, builder.user.getSalt()));
            return builder;
        }
        
        UserBuilder setEmail(String email) {
            this.user.setEmail(email);
            return this;
        }
        
        UserBuilder setName(String first, String last) {
            this.user.setFirstName(first);
            this.user.setLastName(last);
            return this;
        }
        
        User build() {
            return this.user;
        }
    }
}
