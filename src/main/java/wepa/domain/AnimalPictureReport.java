/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wepa.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class AnimalPictureReport extends AbstractPersistable<Long> {
    @ManyToOne
    private AnimalPicture animalPicture;
    private String reason;
    private Date created;
    @ManyToOne
    private User sentBy;
    
    public AnimalPictureReport() {
        this.created = new Date();
    }
    
    public AnimalPicture getAnimalPicture() {
        return animalPicture;
    }

    public void setAnimalPicture(AnimalPicture animalPicture) {
        this.animalPicture = animalPicture;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }
    
    
}
