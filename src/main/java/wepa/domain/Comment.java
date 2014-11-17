package wepa.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Comment extends AbstractPersistable<Long> {
    private String comment;
    private Date timestamp;
    @ManyToOne
    private User author;
    @ManyToOne
    private AnimalPicture picture;
    
    public Comment(){
        timestamp = new Date();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public AnimalPicture getPicture() {
        return picture;
    }

    public void setPicture(AnimalPicture picture) {
        this.picture = picture;
    }
}
