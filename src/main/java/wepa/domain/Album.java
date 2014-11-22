package wepa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Album extends AbstractPersistable<Long> {
    
    @OneToMany(mappedBy = "album", fetch= FetchType.EAGER)
    private List<AnimalPicture> animalPictures;
    @NotBlank
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    private String description;
    private int likes;
    
    @ManyToOne
    private User author;

    public Album() {
    }

    public Album(String name) {
        this.name = name;
        this.created = new Date();
    }

    public List<AnimalPicture> getAnimalPictures() {
        return animalPictures;
    }

    public void setAnimalPictures(List<AnimalPicture> animalPictures) {
        this.animalPictures = animalPictures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    
    public int getLikes(){
        return this.likes;
    }
    
    public void setLikes(int likes){
        this.likes = likes;
    }
    
    public List<AnimalPicture> getMax3AnimalPictures(){
        List<AnimalPicture> animalPictures = this.getAnimalPictures();
        
        if(animalPictures.size() > 3){
            List<AnimalPicture> newList = new ArrayList<AnimalPicture>();
            Random rand = new Random();
            newList.add(animalPictures.get(rand.nextInt(animalPictures.size())));
            newList.add(animalPictures.get(rand.nextInt(animalPictures.size())));
            newList.add(animalPictures.get(rand.nextInt(animalPictures.size())));
            
            return newList;
        } else {
            return this.getAnimalPictures();
        }
    }
}
