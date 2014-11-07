package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}