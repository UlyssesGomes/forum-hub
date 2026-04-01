package br.com.forum_hub.domain.login.github;

public record EmailDataDTO (String email, boolean primary, boolean verified, String visibility) {
}
