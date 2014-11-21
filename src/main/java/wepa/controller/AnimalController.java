/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wepa.helpers.Routes;
import wepa.service.AnimalService;

@Controller
@RequestMapping("/animals/")
public class AnimalController {
    @Autowired
    AnimalService animalService;
    
    // TODO: Show recent animals and 2 recent images added to them
    public String getRecentAnimals(Model model){
        model.addAttribute("animals", animalService.getRecentAnimals(10));
        
        return Routes.ANIMALS_TEMPLATE;
    }
}
