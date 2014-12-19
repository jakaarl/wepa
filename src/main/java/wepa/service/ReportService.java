package wepa.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wepa.domain.AlbumReport;
import wepa.domain.AnimalPictureReport;
import wepa.domain.CommentReport;
import wepa.repository.AlbumReportRepository;
import wepa.repository.AlbumRepository;
import wepa.repository.AnimalPictureReportRepository;
import wepa.repository.AnimalPictureRepository;
import wepa.repository.AnimalReportRepository;
import wepa.repository.AnimalRepository;
import wepa.repository.CommentReportRepository;

@Service
public class ReportService {
	
    @Autowired
    AlbumReportRepository albumReportRepository;
    
    @Autowired
    AlbumService albumService;
    
    @Autowired
    AnimalReportRepository animalReportRepository;
    
    @Autowired
    AnimalService animalService;
    
    @Autowired
    AnimalPictureReportRepository animalPictureReportRepository;
    
    @Autowired
    AnimalPictureService animalPictureService;
    
    @Autowired
    CommentReportRepository commentReportRepository;
    
    @Autowired
    CommentService commentService;
    
    public List<CommentReport> getCommentReports() {
        return commentReportRepository.findAll();
    }
    
    public void saveCommentReport(CommentReport commentReport) {
        commentReportRepository.save(commentReport);
    }
    
    public CommentReport getCommentReport(Long id) {
        return commentReportRepository.findOne(id);
    }
    
    public void actOnCommentReport(Long id) throws IllegalArgumentException {
        CommentReport commentReport = commentReportRepository.findOne(id);
        if(commentReport == null){
            throw new IllegalArgumentException("CommentReport not found");
        } else {
            commentService.delete(commentReport.getComment().getId());
            commentReportRepository.delete(id);
        }
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

    public void actOnAlbumReport(Long id) throws IllegalArgumentException {
        AlbumReport albumReport = albumReportRepository.findOne(id);
        if(albumReport == null){
            throw new IllegalArgumentException("AlbumReport not found");
        } else {
            albumService.delete(albumReport.getAlbum().getId());
            albumReportRepository.delete(id);
        }
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
    
    public void actOnAnimalPictureReport(Long id) throws IllegalArgumentException {
        AnimalPictureReport animalPictureReport = animalPictureReportRepository.findOne(id);
        if(animalPictureReport == null){
            throw new IllegalArgumentException("AnimalPictureReport not found");
        } else {
            animalPictureService.delete(animalPictureReport.getAnimalPicture().getId());
            animalPictureReportRepository.delete(id);
        }
    }
    
    public void deleteAnimalPictureReport(Long id) {
        animalPictureReportRepository.delete(id);
    }
}
