package br.com.forum_hub.domain.user;

public record UserRegisterDTO(String email, String password, String fullName, String nickname, String shortBiography, String biography) {
}
