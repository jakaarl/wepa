package wepa.auth;

import java.util.Collections;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import wepa.domain.User;

public class TestUser {
 
    private final User testUser;
    private final Authentication testAuthentication;
    
    public TestUser(User testUser) {
        this.testUser = testUser;
        this.testAuthentication = new UsernamePasswordAuthenticationToken(
                testUser, testUser.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
    
    public User getUser() {
        return testUser;
    }
    
    public MockHttpSession login() {
        SecurityContextHolder.getContext().setAuthentication(testAuthentication);
        MockHttpSession session = new MockHttpSession();
        // black magic for Thymeleaf!
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return session;
    }
    
    public void logout() {
        SecurityContextHolder.clearContext();
    }
    
}
