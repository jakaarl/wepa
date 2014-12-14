/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wepa.domain.AlbumReport;
import wepa.domain.AnimalPictureReport;
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
    
    public List<CommentReport> getCommentReports() {
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
    
    public List<AlbumReport> getAlbumReports() {
        return albumReportRepository.findAll();
    }
    
    public void saveAlbumReport(AlbumReport albumReport) {
        albumReportRepository.save(albumReport);
    }
    
    public AlbumReport getAlbumReport(Long id) {
        return albumReportRepository.findOne(id);
    }

    public void deleteAlbumReport(Long id) {
        albumReportRepository.delete(id);
    }
    
    public List<AnimalPictureReport> getAnimalPictureReports() {
        return animalPictureReportRepository.findAll();
    }
    
    public void saveAnimalPictureReport(AnimalPictureReport animalPictureReport) {
        animalPictureReportRepository.save(animalPictureReport);
    }
    
    public AnimalPictureReport getAnimalPictureReport(Long id) {
        return animalPictureReportRepository.findOne(id);
    }

    public void deleteAnimalPictureReport(Long id) {
        animalPictureReportRepository.delete(id);
    }
}
