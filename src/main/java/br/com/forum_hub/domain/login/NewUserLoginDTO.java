package br.com.forum_hub.domain.login;

import br.com.forum_hub.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserLoginDTO(
        @NotBlank
        String fullName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min=8, max=60)
        String password,
        @NotBlank
        String nickname,
        String biography,
        String shortBiography
) {
}
