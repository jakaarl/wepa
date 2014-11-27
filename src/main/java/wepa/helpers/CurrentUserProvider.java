package wepa.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import wepa.domain.User;

public class CurrentUserProvider {
    
    /**
     * Gets the user id of the current user, or <code>null</code> if not logged in.
     * 
     * @return  current user id.
     */
    public Long getUserId() {
        User user = getUser();
        return (user != null ? user.getId() : null);
    }
    
    /**
     * Gets the current user, or <code>null</code> if not logged in.
     * 
     * @return  current user.
     */
    public User getUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            user = (User) authentication.getPrincipal();
        }
        return user;
    }

}
