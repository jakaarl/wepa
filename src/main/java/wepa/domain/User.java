package wepa.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "registered_user")
public class User extends AbstractPersistable<Long> implements UserDetails {
    // TODO Roles and different authorities(?)
    
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String salt;
    private String password;
    private String username;
    
//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
//    private List<AnimalPicture> animalPictures;

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

//    public List<AnimalPicture> getAnimalPictures() {
//        return animalPictures;
//    }
//
//    public void setAnimalPictures(List<AnimalPicture> animalPictures) {
//        this.animalPictures = animalPictures;
//    }

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
    
    
}
