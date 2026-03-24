package br.com.forum_hub.domain.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(@NotBlank @Email String email, @NotBlank @Size(min=5) String password) {
}
