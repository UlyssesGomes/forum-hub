package br.com.forum_hub.domain.login.github;

import br.com.forum_hub.domain.user.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Service
public class LoginGithubService {

    @Value("${app.github.clientId}")
    private String clientId;
    @Value("${app.github.secret}")
    private String clientSecret;
    @Value("${app.github.callback}")
    private String redirectUri;

    @Value("${app.github.register.app.clientId}")
    private String registerAppClientId;
    @Value("${app.github.register.app.secret}")
    private String registerAppClientSecret;
    @Value("${app.github.register.app.callback}")
    private String registerAppRedirectUri;

    private final RestClient restClient;

    public LoginGithubService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String getEmail(String code){

        var token = getToken(code, clientId, redirectUri);
        var headers = new HttpHeaders();

        headers.setBearerAuth(token);

        return sendEmailRequest(headers);
    }

    public String generateUrlRegister() {

        return "https://github.com/login/oauth/authorize"+
                "?client_id="+registerAppClientId +
                "&redirect_uri="+registerAppRedirectUri +
                "&scope=read:user,user:email";
    }

    public String generateUrl(){
        return "https://github.com/login/oauth/authorize"+
                "?client_id="+clientId +
                "&redirect_uri="+redirectUri +
                "&scope=read:user,user:email";
    }

    public UserRegisterDTO getOAuthData(String code){
        var accessToken = getToken(code, registerAppClientId, registerAppRedirectUri, registerAppClientSecret);
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        var email = sendEmailRequest(headers);

        var response = restClient.get()
                .uri("https://api.github.com/user")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);

        var fullName = response.get("name").toString();
        var nickname = response.get("login").toString();

        var password = UUID.randomUUID().toString();

        return new UserRegisterDTO(email, password, fullName, nickname, null, null);
    }

    private String sendEmailRequest(HttpHeaders headers) {
        var response = restClient.get()
                .uri("https://api.github.com/user/emails")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(EmailDataDTO[].class);

        for(EmailDataDTO d: response){
            if(d.primary() && d.verified())
                return d.email();
        }

        return null;
    }

    private String getToken(String code, String id, String uri, String secret) {

        var response = restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Map.of("code", code, "client_id", id,
                        "client_secret", clientSecret, "redirect_uri", uri))
                .retrieve()
                .body(Map.class);

        return response.get("access_token").toString();
    }

    private String getToken(String code, String id, String uri) {
        return getToken(code, id, uri, clientSecret);
    }
}
