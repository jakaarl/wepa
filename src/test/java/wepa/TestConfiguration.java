package wepa;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCrypt;

import wepa.auth.TestUser;
import wepa.domain.User;
import wepa.repository.UserRepository;

@Configuration
@Import(Application.class)
@Profile("test")
public class TestConfiguration {
    
    private static final User TEST_USER = new User();
    static {
        TEST_USER.setFirstName("Fox");
        TEST_USER.setLastName("Mulder");
        TEST_USER.setEmail("fox.mulder@fbi.gov");
        TEST_USER.setSalt(BCrypt.gensalt());
        TEST_USER.setPassword(BCrypt.hashpw("trustno1", TEST_USER.getSalt()));
        TEST_USER.setUsername("fmulder");
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Bean
    public TestUser testUser() {
        return new TestUser(TEST_USER);
    }
    
    @PostConstruct
    public void persistTestUser() {
        userRepository.save(TEST_USER); 
    }
}
