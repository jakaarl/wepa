/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wepa.domain.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    //TODO: Custom query for searching
    public List<Animal> findByName(String name, Pageable pageable);
}
