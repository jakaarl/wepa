package wepa.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.Album;
import wepa.domain.AlbumReport;
import wepa.domain.AnimalPicture;
import wepa.domain.AnimalPictureReport;
import wepa.domain.Comment;
import wepa.domain.CommentReport;
import wepa.helpers.Routes;
import wepa.service.AlbumService;
import wepa.service.AnimalPictureService;
import wepa.service.CommentService;
import wepa.service.ReportService;
import wepa.service.UserService;

@Controller
@RequestMapping("/reports/")
public class ReportController {
    @Autowired
    ReportService reportService;
    
    @Autowired
    CommentService commentService;
    
    @Autowired
    AlbumService albumService;
    
    @Autowired
    AnimalPictureService animalPictureService;
    
    @Autowired
    UserService userService;
    
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
    
    @RequestMapping(value = "comments/{id}", method = RequestMethod.POST)
    public String postCommentReport(@PathVariable Long id, @RequestParam String reason, RedirectAttributes redirectAttributes){
        Comment comment = commentService.getComment(id);
        if(comment != null){
            CommentReport commentReport = new CommentReport();
            commentReport.setReason(reason);
            commentReport.setComment(comment);
            commentReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveCommentReport(commentReport);
            if(comment.getComment().length() > 50){
                redirectAttributes.addFlashAttribute("message", "Comment \"" + comment.getComment().substring(0, 50) + "...\" reported");
            } else {
                redirectAttributes.addFlashAttribute("message", "Comment \"" + comment.getComment() + "\" reported");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Comment not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "comments/{id}/act", method = RequestMethod.POST)
    public String actOnCommentReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reportService.actOnCommentReport(id);
        redirectAttributes.addFlashAttribute("message", "Comment and the report has been successfully removed");
        
        return Routes.COMMENT_REPORTS_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "comments/{id}", method = RequestMethod.DELETE)
    public String deleteCommentReport(@PathVariable Long id){
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
    
    @RequestMapping(value = "albums/{id}", method = RequestMethod.POST)
    public String postAlbumReport(@PathVariable Long id, @RequestParam String reason, RedirectAttributes redirectAttributes){
        Album album = albumService.find(id);
        if(album != null){
            AlbumReport albumReport = new AlbumReport();
            albumReport.setReason(reason);
            albumReport.setAlbum(album);
            albumReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveAlbumReport(albumReport);
            redirectAttributes.addFlashAttribute("message", "Album \"" + album.getName() + "\" reported");
        } else {
            redirectAttributes.addFlashAttribute("error", "Album not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "albums/{id}/act", method = RequestMethod.POST)
    public String actOnAlbumReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reportService.actOnAlbumReport(id);
        redirectAttributes.addFlashAttribute("message", "Album and the report has been successfully removed");
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "albums/{id}", method = RequestMethod.DELETE)
    public String deleteAlbumReport(@PathVariable Long id){
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
    
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.POST)
    public String postAnimalPictureReport(@PathVariable Long id, @RequestParam String reason, RedirectAttributes redirectAttributes){
        AnimalPicture animalPicture = animalPictureService.getById(id);
        if(animalPicture != null){
            AnimalPictureReport animalPictureReport = new AnimalPictureReport();
            animalPictureReport.setReason(reason);
            animalPictureReport.setAnimalPicture(animalPicture);
            animalPictureReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveAnimalPictureReport(animalPictureReport);
            redirectAttributes.addFlashAttribute("message", "Animal Picture \"" + animalPicture.getTitle() + "\" reported");
        } else {
            redirectAttributes.addFlashAttribute("error", "Animal Picture not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "animalpictures/{id}/act", method = RequestMethod.POST)
    public String actOnAnimalPictureReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reportService.actOnAnimalPictureReport(id);
        redirectAttributes.addFlashAttribute("message", "AnimalPicture and the report has been successfully removed");
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.DELETE)
    public String deleteAnimalPictureReport(@PathVariable Long id){
        reportService.deleteAnimalPictureReport(id);
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
}
