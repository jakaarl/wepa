/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wepa.domain.CommentReport;
import wepa.repository.AlbumReportRepository;
import wepa.repository.AnimalPictureReportRepository;
import wepa.repository.AnimalReportRepository;
import wepa.repository.CommentReportRepository;

@Service
public class ReportService {
    @Autowired
    AlbumReportRepository albumReportRepository;
    
    @Autowired
    AnimalReportRepository animalReportRepository;
    
    @Autowired
    AnimalPictureReportRepository animalPictureReportRepository;
    
    @Autowired
    CommentReportRepository commentReportRepository;
    
    public Object getCommentReports() {
        return commentReportRepository.findAll();
    }
    
    public void saveCommentReport(CommentReport commentReport) {
        commentReportRepository.save(commentReport);
    }
    
    public CommentReport getCommentReport(Long id) {
        return commentReportRepository.findOne(id);
    }

    public void deleteCommentReport(Long id) {
        commentReportRepository.delete(id);
    }
}
