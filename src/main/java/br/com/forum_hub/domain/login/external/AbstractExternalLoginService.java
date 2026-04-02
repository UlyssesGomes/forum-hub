package br.com.forum_hub.domain.login.external;

import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.domain.user.UserRegisterDTO;
import br.com.forum_hub.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public abstract class AbstractExternalLoginService {

    protected final RestClient restClient;

    @Autowired
    private UserService userService;

    public AbstractExternalLoginService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public abstract String getClientId();
    public abstract String getClientSecret();
    public abstract String getRedirectUri();

    public abstract String getPlatformUrl();
    public abstract String getPlatformScope();

    public abstract String getPlatformTokenUri();
    public abstract Map<String, String> getPlatformBodyParams(String code);
    public abstract String getTokenName();

    public abstract UserRegisterDTO getExternalLoginUserOAuthData(String code);

    public String generateUrl(){
        return getPlatformUrl()+
                "?client_id="+getClientId() +
                "&redirect_uri="+getRedirectUri() +
                "&scope=" + getPlatformScope();
    }


    public User verifyExternalLoginUserAndRegister(String code) {
        var githubUser = getExternalLoginUserOAuthData(code);
        var user = userService.verifyUserByEmail(githubUser.email());

        if(user != null)
            return user;

        return userService.registerVerifiedNewUser(githubUser);
    }

    protected String getToken(String code) {
        var response = restClient.post()
                .uri(getPlatformTokenUri())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(getPlatformBodyParams(code))
                .retrieve()
                .body(Map.class);

        return response.get(getTokenName()).toString();
    }
}
