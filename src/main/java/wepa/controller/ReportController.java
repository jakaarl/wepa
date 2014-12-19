package wepa.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    
    /**
     * 
     *    CommentReports
     * 
     */
    
    @RequestMapping(value = "comments", method = RequestMethod.GET)
    public String getCommentReports(Model model){
        model.addAttribute("commentReports", reportService.getCommentReports());
        
        return Routes.COMMENT_REPORTS_TEMPLATE;
    }
    
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
    
    @RequestMapping(value = "comments/{id}", method = RequestMethod.POST)
    public String postCommentReport(@PathVariable Long id, @RequestBody CommentReport commentReport, RedirectAttributes redirectAttributes){
        Comment comment = commentService.getComment(id);
        if(comment != null){
            commentReport.setComment(comment);
            commentReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveCommentReport(commentReport);
            if(comment.getComment().length() > 50){
                redirectAttributes.addAttribute("message", "Comment \"" + comment.getComment().substring(0, 50) + "...\" reported");
            } else {
                redirectAttributes.addAttribute("message", "Comment \"" + comment.getComment() + "\" reported");
            }
        } else {
            redirectAttributes.addAttribute("error", "Comment not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "comments/{id}/act", method = RequestMethod.POST)
    public String actOnCommentReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            reportService.actOnCommentReport(id);
            redirectAttributes.addAttribute("message", "Comment and the report has been successfully removed");
        } catch (Exception ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
        }
        
        return Routes.COMMENT_REPORTS_REDIRECT;
    }
    
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
    
    @RequestMapping(value = "albums", method = RequestMethod.GET)
    public String getAlbumReports(Model model){
        model.addAttribute("albumReports", reportService.getAlbumReports());
        
        return Routes.ALBUM_REPORTS_TEMPLATE;
    }
    
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
    
    @ResponseBody
    @RequestMapping(value = "albums/{id}", method = RequestMethod.POST)
    public String postAlbumReport(@PathVariable Long id, @RequestBody AlbumReport albumReport, RedirectAttributes redirectAttributes){
        Album album = albumService.find(id);
        if(album != null){
            albumReport.setAlbum(album);
            albumReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveAlbumReport(albumReport);
            redirectAttributes.addAttribute("message", "Album \"" + album.getName() + "\" reported");
        } else {
            redirectAttributes.addAttribute("error", "Album not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "albums/{id}/act", method = RequestMethod.POST)
    public String actOnAlbumReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            reportService.actOnAlbumReport(id);
            redirectAttributes.addAttribute("message", "Album and the report has been successfully removed");
        } catch (Exception ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
        }
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
    
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
    
    @RequestMapping(value = "animalpictures", method = RequestMethod.GET)
    public String getAnimalPictureReports(Model model){
        model.addAttribute("animalPictureReports", reportService.getAnimalPictureReports());
        
        return Routes.ANIMALPICTURE_REPORTS_TEMPLATE;
    }
    
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
    
    @ResponseBody
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.POST)
    public String postAnimalPictureReport(@PathVariable Long id, @RequestBody AnimalPictureReport animalPictureReport, RedirectAttributes redirectAttributes){
        AnimalPicture animalPicture = animalPictureService.getById(id);
        if(animalPicture != null){
            animalPictureReport.setAnimalPicture(animalPicture);
            animalPictureReport.setSentBy(userService.getAuthenticatedPerson());
            reportService.saveAnimalPictureReport(animalPictureReport);
            redirectAttributes.addAttribute("message", "Animal Picture \"" + animalPicture.getTitle() + "\" reported");
        } else {
            redirectAttributes.addAttribute("error", "Animal Picture not found, so it has not been reported");
        }
        
        return Routes.INDEX_REDIRECT;
    }
    
    @RequestMapping(value = "animalpictures/{id}/act", method = RequestMethod.POST)
    public String actOnAnimalPictureReport(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            reportService.actOnAnimalPictureReport(id);
            redirectAttributes.addAttribute("message", "AnimalPicture and the report has been successfully removed");
        } catch (Exception ex) {
            redirectAttributes.addAttribute("error", ex.getMessage());
        }
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
    
    @RequestMapping(value = "animalpictures/{id}", method = RequestMethod.DELETE)
    public String deleteAnimalPictureReport(@PathVariable Long id){
        reportService.deleteAnimalPictureReport(id);
        
        return Routes.ANIMALPICTURE_REPORTS_REDIRECT;
    }
}
