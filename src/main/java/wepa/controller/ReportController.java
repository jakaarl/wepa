package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import wepa.domain.AlbumReport;
import wepa.domain.AnimalPictureReport;
import wepa.domain.CommentReport;
import wepa.helpers.Routes;
import wepa.service.ReportService;

@Controller
@RequestMapping("/reports/")
public class ReportController {
    @Autowired
    ReportService reportService;
    
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("sentBy");
    }
    
    /**
     * 
     *    CommentReports
     * 
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "comments", method = RequestMethod.GET)
    public String getCommentReports(Model model){
        model.addAttribute("commentReports", reportService.getCommentReports());
        
        return Routes.COMMENT_REPORTS_TEMPLATE;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "comments/{id}", method = RequestMethod.GET)
    public String getCommentReport(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        CommentReport commentReport = reportService.getCommentReport(id);
        
        if(commentReport == null){
            redirectAttributes.addFlashAttribute("error", "CommentReport not found");
            
            return Routes.COMMENT_REPORTS_REDIRECT;
        }
        
        model.addAttribute("commentReport", commentReport);
        return Routes.COMMENT_REPORT_TEMPLATE;
    }
    
    @PreAuthorize("authenticated")
    @ResponseBody
    @RequestMapping(value = "comments/{id}", method = RequestMethod.POST)
    public String postCommentReport(@PathVariable Long id, @RequestBody CommentReport commentReport){
        reportService.saveCommentReport(commentReport);
        
        return "saved";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "comments/{id}", method = RequestMethod.DELETE)
    public String postCommentReport(@PathVariable Long id){
        reportService.deleteCommentReport(id);
        
        return Routes.COMMENT_REPORTS_REDIRECT;
    }
    
    /**
     * 
     *    AlbumReports
     * 
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "albums", method = RequestMethod.GET)
    public String getAlbumReports(Model model){
        model.addAttribute("albumReports", reportService.getAlbumReports());
        
        return Routes.ALBUM_REPORTS_TEMPLATE;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "albums/{id}", method = RequestMethod.GET)
    public String getAlbumReport(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        AlbumReport albumReport = reportService.getAlbumReport(id);
        
        if(albumReport == null){
            redirectAttributes.addFlashAttribute("error", "AlbumReport not found");
            
            return Routes.ALBUM_REPORTS_REDIRECT;
        }
        
        model.addAttribute("albumReport", albumReport);
        return Routes.ALBUM_REPORTS_TEMPLATE;
    }
    
    @PreAuthorize("authenticated")
    @ResponseBody
    @RequestMapping(value = "albums/{id}", method = RequestMethod.POST)
    public String postAlbumReport(@PathVariable Long id, @RequestBody AlbumReport albumReport){
        reportService.saveAlbumReport(albumReport);
        
        return "saved";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "albums/{id}", method = RequestMethod.DELETE)
    public String postAlbumReport(@PathVariable Long id){
        reportService.deleteAlbumReport(id);
        
        return Routes.ALBUM_REPORTS_REDIRECT;
    }
    
    /**
     * 
     *    AnimalPictureReports
     * 
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "animalpictures", method = RequestMethod.GET)
    public String getAnimalPictureReports(Model model){
        model.addAttribute("animalPictureReports", reportService.getAnimalPictureReports());
        
        return Routes.ANIMALPICTURE_REPORTS_TEMPLATE;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.GET)
    public String getAnimalPictureReport(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        AnimalPictureReport animalPictureReport = reportService.getAnimalPictureReport(id);
        
        if(animalPictureReport == null){
            redirectAttributes.addFlashAttribute("error", "AnimalPictureReport not found");
            
            return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
        }
        
        model.addAttribute("animalPictureReport", animalPictureReport);
        return Routes.ANIMALPICTURE_REPORTS_TEMPLATE;
    }
    
    @PreAuthorize("authenticated")
    @ResponseBody
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.POST)
    public String postAnimalPictureReport(@PathVariable Long id, @RequestBody AnimalPictureReport animalPictureReport){
        reportService.saveAnimalPictureReport(animalPictureReport);
        
        return "saved";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.DELETE)
    public String postAnimalPictureReport(@PathVariable Long id){
        reportService.deleteAnimalPictureReport(id);
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
}
