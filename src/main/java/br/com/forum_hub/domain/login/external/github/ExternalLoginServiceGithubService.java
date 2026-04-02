package br.com.forum_hub.domain.login.external.github;

import br.com.forum_hub.domain.login.external.AbstractExternalLoginService;
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
public class ExternalLoginServiceGithubService extends AbstractExternalLoginService {

    @Value("${app.github.clientId}")
    private String clientId;
    @Value("${app.github.secret}")
    private String clientSecret;
    @Value("${app.github.callback}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    public ExternalLoginServiceGithubService(RestClient.Builder restClientBuilder) {
        super(restClientBuilder);
    }

    @Override
    public UserRegisterDTO getExternalLoginUserOAuthData(String code){
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

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public String getPlatformUrl() {
        return "https://github.com/login/oauth/authorize";
    }

    @Override
    public String getPlatformScope() {
        return "read:user,user:email";
    }

    @Override
    public String getPlatformTokenUri() {
        return "https://github.com/login/oauth/access_token";
    }

    @Override
    public Map<String, String> getPlatformBodyParams(String code) {
        return Map.of("code", code, "client_id", getClientId(),
                "client_secret", getClientSecret(), "redirect_uri", getRedirectUri());
    }

    @Override
    public String getTokenName() {
        return "access_token";
    }
}
