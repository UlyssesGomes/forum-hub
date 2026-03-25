package br.com.forum_hub.domain.login;

import br.com.forum_hub.domain.user.User;

public record CreatedNewUserLoginDTO (String fullName,
                                      String email,
                                      String nickname,
                                      String biography,
                                      String shortBiography,
                                      String token){
    public CreatedNewUserLoginDTO (User user) {
        this(user.getFullName(), user.getEmail(), user.getNickname(), user.getBiography(), user.getShortBiography(), user.getToken());
    }
}
