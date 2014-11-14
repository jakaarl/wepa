package wepa.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private String albumName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    private String albumDescription;

    public Album() {
    }

    public Album(String albumName) {
        this.albumName = albumName;
        this.created = new Date();
    }

    public List<AnimalPicture> getAnimalPictures() {
        return animalPictures;
    }

    public void setAnimalPictures(List<AnimalPicture> animalPictures) {
        this.animalPictures = animalPictures;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String description) {
        this.albumDescription = description;
    }
    
    
    
}
