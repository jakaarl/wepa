package wepa.service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import wepa.domain.User;
import wepa.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    //Add admin accounts and TODO: Add ADMIN roles
    @PostConstruct
    public void init(){
        // Ivan
        User ivan = new User();
        ivan.setEmail("ivantadic014@gmail.com");
        ivan.setFirstName("Ivan");
        ivan.setLastName("Tadic");
        ivan.setClearTextPassword("admin123");
        ivan.setUsername("IvanTadic");
        
        // Jani
        User jani = new User();
        jani.setEmail("jani.kaarela@gmail.com");
        jani.setFirstName("Jani");
        jani.setLastName("Kaarela");
        jani.setClearTextPassword("admin123");
        jani.setUsername("JaniKaarela");
        
        // Kristian
        User kristian = new User();
        kristian.setEmail("kristian.lauttamus@gmail.com");
        kristian.setFirstName("Kristian");
        kristian.setLastName("Lauttamus");
        kristian.setClearTextPassword("admin123");
        kristian.setUsername("KristianLauttamus");
        
        save(ivan);
        save(jani);
        save(kristian);
    }
    
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    
    public User save(User user) {
        if (user.getClearTextPassword() != null) {
            user.setSalt(BCrypt.gensalt());
            user.setPassword(BCrypt.hashpw(user.getClearTextPassword(), user.getSalt()));
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
}
