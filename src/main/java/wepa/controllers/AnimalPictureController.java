package wepa.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wepa.domain.AnimalPicture;
import wepa.repository.AnimalPictureRepository;

@Controller
@RequestMapping("/")
public class AnimalPictureController {
    @Autowired
    private AnimalPictureRepository animalRepository;
    
    @RequestMapping(value="/", method=RequestMethod.POST)
    public String addNewAnimalPicture(@Valid @ModelAttribute AnimalPicture animalPicture){
        animalRepository.save(animalPicture);
        
        return "redirect:/";
    }
}
