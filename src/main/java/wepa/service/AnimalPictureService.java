package wepa.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Album;
import wepa.domain.AnimalPicture;
import wepa.domain.User;
import wepa.repository.AnimalPictureRepository;

@Service
public class AnimalPictureService {
    
    @Autowired
    private AnimalPictureRepository pictureRepo;
    
    @Autowired
    private UserService userService;
    
    public AnimalPicture getById(Long id) {
        return pictureRepo.findOne(id);
    }
    
    public int likeAnimalPicture(Long id) {
        int size = 0;
        
        AnimalPicture animalPicture = pictureRepo.findOne(id);
        User user = userService.getAuthenticatedPerson();
        
        if(animalPicture != null && user != null){
            List<User> picLikes = animalPicture.getLikes();
            List<AnimalPicture> userLikes = user.getLikedPictures();
            
            if(!picLikes.contains(user)){
                picLikes.add(user);
            }
            
            if(!userLikes.contains(animalPicture)){
                userLikes.add(animalPicture);
            }
            
            animalPicture.setLikes(picLikes);
            user.setLikedPictures(userLikes);
            
            // Save
            pictureRepo.save(animalPicture);
            userService.save(user);
            
            size = picLikes.size();
        } else if(user == null){
            size = animalPicture.getLikes().size();
        }
        
        return size;
    }
    
    public int dislikeAnimalPicture(Long id) {
        int size = 0;
        
        AnimalPicture animalPicture = pictureRepo.findOne(id);
        User user = userService.getAuthenticatedPerson();
        
        if(animalPicture != null && user != null){
            List<User> picLikes = animalPicture.getLikes();
            List<AnimalPicture> userLikes = user.getLikedPictures();
            
            if(picLikes.contains(user)){
                picLikes.remove(user);
            }
            
            if(userLikes.contains(animalPicture)){
                userLikes.remove(animalPicture);
            }
            
            animalPicture.setLikes(picLikes);
            user.setLikedPictures(userLikes);
            
            // Save
            pictureRepo.save(animalPicture);
            userService.save(user);
            
            size = picLikes.size();
        } else if(user == null){
            size = animalPicture.getLikes().size();
        }
        
        return size;
    }
    
    /**
     * Gets <code>maxCount</code> latest animal pictures, newest first.
     * 
     * @param maxCount  maximum number to fetch.
     * 
     * @return  a list of latest animal pictures.
     */
    public List<AnimalPicture> getLatest(int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Direction.DESC, "added");
        return pictureRepo.findAll(limit).getContent();
    }
    
    public List<AnimalPicture> getLatestAnimalPictures(User user, int maxCount) {
        Pageable limit = new PageRequest(0, maxCount, Sort.Direction.DESC, "added");
        return pictureRepo.findByAuthor(user, limit).getContent();
    }
    
    public List<AnimalPicture> getAllAnimalPicturesByUser(User user) {
        return pictureRepo.findAllByAuthor(user);
    }
    
    public AnimalPicture add(MultipartFile file, String title, String description, Album album) throws IllegalArgumentException, IOException {
        validate(file);
        AnimalPicture pic = new AnimalPicture();
        pic.setAdded(new Date());
        if(title.isEmpty()){
            pic.setTitle(file.getOriginalFilename());
        } else {
            pic.setTitle(title);
        }
        User user = userService.getAuthenticatedPerson();
        if(user == null){
            throw new IllegalArgumentException("First you must login!");
        }
        pic.setAuthor(user);
        pic.setDescription(description);
        pic.setAlbum(album);
        pic.setContentType(file.getContentType());
        pic.setImage(file.getBytes());
        return pictureRepo.save(pic);
    }
    
    private void validate(MultipartFile file) throws IllegalArgumentException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        } else if (file.getSize() > (5*1024*1024)) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
        if (!file.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files allowed");
        }
    }
}
