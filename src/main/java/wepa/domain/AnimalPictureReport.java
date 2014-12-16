package wepa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class AnimalPictureReport extends AbstractReport {
	
    @ManyToOne
    private AnimalPicture animalPicture;
    
    public AnimalPictureReport() {
        super();
    }
    
    public AnimalPicture getAnimalPicture() {
        return animalPicture;
    }

    public void setAnimalPicture(AnimalPicture animalPicture) {
        this.animalPicture = animalPicture;
    }
    
}
