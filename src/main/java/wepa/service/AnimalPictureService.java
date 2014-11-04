package wepa.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import wepa.domain.AnimalPicture;
import wepa.repository.AnimalPictureRepository;

@Service
public class AnimalPictureService {
    
    @Autowired
    private AnimalPictureRepository pictureRepo;
    
    public AnimalPicture getById(Long id) {
        return pictureRepo.findOne(id);
    }
    
    public AnimalPicture add(MultipartFile file, String description) throws IllegalArgumentException, IOException {
        validate(file);
        AnimalPicture pic = new AnimalPicture();
        pic.setAdded(new Date());
        pic.setDescription(description);
        pic.setContentType(file.getContentType());
        pic.setName(file.getOriginalFilename());
        pic.setImage(file.getBytes());
        return pictureRepo.save(pic);
    }
    
    private void validate(MultipartFile file) throws IllegalArgumentException {
        if (!file.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files allowed");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        } else if (file.getSize() > (5*1024*1024)) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
    }
    
}
