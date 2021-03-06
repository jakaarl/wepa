package wepa.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
public class Animal extends AbstractPersistable<Long> {
	
    @Column(unique = true)
    private String name;
    private String[] synonyms;
    private Date created;
    @ManyToOne
    private User creator;
    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<AnimalPicture> animalPictures;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<AnimalPicture> getAnimalPictures() {
        return animalPictures;
    }

    public void setAnimalPictures(List<AnimalPicture> animalPictures) {
        this.animalPictures = animalPictures;
    }
}
