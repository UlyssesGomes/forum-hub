package br.com.forum_hub.domain.login.github;

import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.domain.user.UserRegisterDTO;
import br.com.forum_hub.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RestClient restClient;

    @Autowired
    private UserService userService;

    public LoginGithubService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String generateUrl(){
        return "https://github.com/login/oauth/authorize"+
                "?client_id="+clientId +
                "&redirect_uri="+redirectUri +
                "&scope=read:user,user:email";
    }

    public UserRegisterDTO getGithubUserOAuthData(String code){
        var accessToken = getToken(code);
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        var response = restClient.get()
                .uri("https://api.github.com/user")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);

        var fullName = response.get("name").toString();
        var nickname = response.get("login").toString();
        var email = response.get("email").toString();

        var password = UUID.randomUUID().toString();

        return new UserRegisterDTO(email, password, fullName, nickname, null, null);
    }

    private String requestGithubEmail(HttpHeaders headers) {
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

    public User verifyGithubUserAndRegister(String code) {
        var githubUser = getGithubUserOAuthData(code);
        var user = userService.verifyUserByEmail(githubUser.email());

        if(user != null)
            return user;

        return userService.registerVerifiedNewUser(githubUser);
    }

    private String getToken(String code) {
        var response = restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Map.of("code", code, "client_id", clientId,
                        "client_secret", clientSecret, "redirect_uri", redirectUri))
                .retrieve()
                .body(Map.class);

        return response.get("access_token").toString();
    }
}
