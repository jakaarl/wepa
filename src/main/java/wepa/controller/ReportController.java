package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wepa.domain.CommentReport;
import wepa.helpers.Routes;
import wepa.service.ReportService;

@Controller
@RequestMapping("/reports/")
public class ReportController {
    @Autowired
    ReportService reportService;
    
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
    
    @ResponseBody
    @RequestMapping(value = "comments/{id}", method = RequestMethod.POST)
    public String postCommentReport(@PathVariable Long id, @RequestBody CommentReport commentReport){
        reportService.saveCommentReport(commentReport);
        
        return "saved";
    }
    
    @RequestMapping(value = "comments/{id}", method = RequestMethod.DELETE)
    public String postCommentReport(@PathVariable Long id){
        reportService.deleteCommentReport(id);
        
        return Routes.COMMENT_REPORTS_REDIRECT;
    }
}
