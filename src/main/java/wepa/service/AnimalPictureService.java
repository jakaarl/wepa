package wepa.service;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.AnimalPicture;
import wepa.repository.AnimalPictureRepository;

@Service
public class AnimalPictureService {
    @Autowired
    private AnimalPictureRepository pictureRepo;
    
    public AnimalPicture add(MultipartFile file, String description) throws IOException{
        if (isValid(file)){
            AnimalPicture pic = new AnimalPicture();
            pic.setAdded(new Date());
            pic.setDescription(description);
            pic.setContentType(file.getContentType());
            pic.setName(file.getOriginalFilename());
            pic.setImage(file.getBytes());
            
            return pictureRepo.save(pic);
        }
        return null;
    }

    private boolean isValid(MultipartFile file) throws InvalidObjectException{
        if (!file.getContentType().contains("image")){
            throw new InvalidObjectException("You can upload only an image file");
        }
        if (file.isEmpty() || file.getSize()>2000000){
            throw new InvalidObjectException("File size must be greather than 0B and less than 2MB");
        }
        return true;
    }
    
    public ResponseEntity<byte[]> createResponseEntity(String id) {
        AnimalPicture pic = pictureRepo.findOne(Long.parseLong(id));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pic.getContentType()));
        headers.setContentLength(pic.getImage().length);
        headers.setCacheControl("public");
        headers.setExpires(Long.MAX_VALUE);

        return new ResponseEntity<>(pic.getImage(), headers, HttpStatus.CREATED);
    }
    
}
