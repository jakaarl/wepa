package wepa.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class AnimalReport extends AbstractReport {
	
    @ManyToOne
    private Animal animal;
    
    public AnimalReport() {
        super();
    }
    
    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

}
