package wepa.service;

import org.springframework.beans.factory.annotation.Autowired;
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
        if (user.getEmail()==null || 
                user.getFirstName()==null || 
                user.getLastName()==null){
            throw new IllegalArgumentException("User email, first- or lastname must not be empty!");
        }
        return userRepository.save(user);
    }
}
