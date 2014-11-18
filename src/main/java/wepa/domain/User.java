package wepa.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "registered_user")
public class User extends AbstractPersistable<Long> implements UserDetails {
    // TODO Roles and different authorities(?)
    
    @NotBlank
	private String firstName;
    @NotBlank
    private String lastName;
    @Column(unique = true) @NotBlank
    private String email;
    private String salt;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<AnimalPicture> animalPictures;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<AnimalPicture> likedPictures;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSalt(){
        return salt;
    }
    
    public void setSalt(String salt){
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // Return true for now
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // Return true for now
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Return true for now
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Return true for now
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<AnimalPicture> getAnimalPictures() {
        return animalPictures;
    }

    public void setAnimalPictures(List<AnimalPicture> animalPictures) {
        this.animalPictures = animalPictures;
    }

    public List<AnimalPicture> getLikedPictures() {
        return likedPictures;
    }

    public void setLikedPictures(List<AnimalPicture> likedPictures) {
        this.likedPictures = likedPictures;
    }
}
