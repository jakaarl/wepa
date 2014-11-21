package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wepa.domain.Animal;
import wepa.repository.AnimalRepository;

@Service
public class AnimalService {
    @Autowired
    AnimalRepository animalRepository;
    
    public List<Animal> getRecentAnimals(int maxCount){
        Pageable pageable = new PageRequest(0, maxCount, Sort.Direction.DESC, "created");
        return animalRepository.findAll(pageable).getContent();
    }
    
    public List<Animal> searchForAnimal(String search){
        Pageable pageable = new PageRequest(0, 10);
        return animalRepository.findByName(search, pageable);
    }
}
