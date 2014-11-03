package wepa.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wepa.domain.AnimalPicture;
import wepa.repository.AnimalPictureRepository;

@Controller
@RequestMapping("/")
public class AnimalPictureController {
    
	static final String INDEX_TEMPLATE = "index";
	static final String INDEX_REDIRECT = "redirect:/";
	
	@Autowired
    private AnimalPictureRepository animalRepository;
    
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) {
		// TODO: add pics to model
		return INDEX_TEMPLATE; 
	}
	
    @RequestMapping(method = RequestMethod.POST)
    public String addNewAnimalPicture(@Valid @ModelAttribute AnimalPicture animalPicture){
        animalRepository.save(animalPicture);
        // TODO: validation errors?
        return INDEX_REDIRECT;
    }
}
