package br.com.forum_hub.domain.hierarchy;

import br.com.forum_hub.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HierarchyService {

    @Autowired
    private RoleHierarchy roleHierarchy;

    public boolean isntSameUserAndHaventPermission(User loggedUser, User author, String targetRole) {
        if(loggedUser.getId().equals(author.getId()))
            return false;
        return userHaventPermission(loggedUser, targetRole);
    }

    public boolean userHaventPermission(User user, String targetRole) {
        return user.getAuthorities().stream()
                .flatMap(authority -> roleHierarchy.getReachableGrantedAuthorities(List.of(authority)).stream())
                .noneMatch(role -> role.getAuthority().equals(targetRole) );
    }
}
