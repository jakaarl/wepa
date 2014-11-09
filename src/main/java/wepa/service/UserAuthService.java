/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wepa.domain.User;
import wepa.repository.UserRepository;

public class UserAuthService implements UserDetailsService {
    
    @Autowired
    UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(string);
        
        if(user != null){
            return user;
        } else {
            throw new UsernameNotFoundException("User not found with the given email");
        }
    }
    
}
