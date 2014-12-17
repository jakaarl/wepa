package wepa.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity
@Table(name = "registered_user")
public class User extends AbstractPersistable<Long> implements UserDetails {
    
    @NotBlank @Length(min = 2, max = 64)
    private String firstName;
    @NotBlank @Length(min = 2, max = 64)
    private String lastName;
    @Column(unique = true) @NotBlank @Length(min = 5, max = 256)
    private String email;
    private String salt;
    private String password;
    @Column(unique = true) @NotBlank @Length(min = 2, max = 32)
    private String username;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Role> roles = new ArrayList<>();
    @Transient
    private List<GrantedAuthority> authorities = new ArrayList<>();
    
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Album> albums = new ArrayList<>();
    
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AnimalPicture> animalPictures = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<AnimalPicture> likedPictures = new ArrayList<>();

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
        this.password = password;
    }
    
    public List<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(List<Role> roles) {
        this.roles = roles;
        this.authorities = new ArrayList<>();
        for (Role role : roles) {
            this.authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
    }
    
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
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

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
    
}
