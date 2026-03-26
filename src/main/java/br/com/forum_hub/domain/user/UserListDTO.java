package br.com.forum_hub.domain.user;

import br.com.forum_hub.domain.role.RoleDTO.RoleDTO;

import java.util.List;

public record UserListDTO(Long id, String fullName, String email, String shortBiography, List<RoleDTO> roles, boolean isVerified) {

    public UserListDTO(User user) {
        this(user.getId(), user.getFullName(), user.getEmail(), user.getShortBiography(), user.getRoles().stream().map(RoleDTO::new).toList(), user.isVerified());
    }
}
