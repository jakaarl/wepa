package wepa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import wepa.domain.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    //TODO: Custom query for searching
    public List<Animal> findByName(String name, Pageable pageable);
}
