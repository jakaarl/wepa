package wepa.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import wepa.domain.User;

public class UserIdProvider {
    
    /**
     * Gets the user id of the current user, or <code>null</code> if not logged in.
     * @return
     */
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        } else {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
    }

}
