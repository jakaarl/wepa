package wepa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
    User findByEmail(String email);
    
    List<User> findByUsername(String username);
    
}