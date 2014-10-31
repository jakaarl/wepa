package wepa.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class AnimalPicture extends AbstractPersistable<Long> {
    private String name;
    private String description;
    private int likes;
    @Temporal(TemporalType.DATE)
    private Date added;
    @Lob
    private byte[] image;
    // TODO: OneToMany edits - Image edits by other users
    // TODO: Author
    //private User author;
    
    public AnimalPicture(){
        this.added = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
