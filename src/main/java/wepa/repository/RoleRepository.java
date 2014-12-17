package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Role findByName(String name);

}
