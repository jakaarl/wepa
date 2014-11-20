package wepa.service;

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
