package br.com.forum_hub.domain.login.external;

public record EmailDataDTO (String email, boolean primary, boolean verified, String visibility) {
}
