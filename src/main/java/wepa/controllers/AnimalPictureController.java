package wepa.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.domain.AnimalPicture;
import wepa.service.AnimalPictureService;

@Controller
@RequestMapping("/")
public class AnimalPictureController {
    
	static final String INDEX_TEMPLATE = "index";
	static final String INDEX_REDIRECT = "redirect:/";
	
   
    
        @Autowired
        private AnimalPictureService animalPictureService;


        @RequestMapping(method = RequestMethod.GET)
        public String index(Model model) {
                // TODO: add pics to model
                return INDEX_TEMPLATE; 
        }

        
        @RequestMapping(method = RequestMethod.POST)
        public String addNewAnimalPicture(@RequestParam MultipartFile file, @RequestParam String description, 
                                                RedirectAttributes redirectAttributes, Model model) throws Exception{      
            try {
               
                AnimalPicture picture = animalPictureService.add(file,description);
                redirectAttributes.addFlashAttribute("message", "Your picture has been saved successfuly");
                redirectAttributes.addFlashAttribute("id", picture.getId());
                redirectAttributes.addFlashAttribute("description", picture.getDescription());
                redirectAttributes.addFlashAttribute("name", picture.getName());
                return INDEX_REDIRECT;
            } catch (Exception  ex) {
                model.addAttribute("errorMessage", ex.getMessage());
                return INDEX_TEMPLATE;
            }
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public ResponseEntity<byte[]> getImage(
                @PathVariable String id) {

            return animalPictureService.createResponseEntity(id);
        }
        
}
