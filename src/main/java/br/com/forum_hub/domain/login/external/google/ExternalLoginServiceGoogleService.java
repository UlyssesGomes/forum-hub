package br.com.forum_hub.domain.login.external.google;

import br.com.forum_hub.domain.login.external.AbstractExternalLoginService;
import br.com.forum_hub.domain.user.UserRegisterDTO;
import com.auth0.jwt.JWT;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Service
public class ExternalLoginServiceGoogleService extends AbstractExternalLoginService {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public ExternalLoginServiceGoogleService(RestClient.Builder restClientBuilder) {
        super(restClientBuilder);
    }

    @Override
    public UserRegisterDTO getExternalLoginUserOAuthData(String code){

        var accessToken = getToken(code);
        System.out.println(accessToken);
        var decodedJWT = JWT.decode(accessToken);
        System.out.println(decodedJWT.getClaims());

        var fullName = "ADICIONAR NOME";
        var nickname = "ADICIONAR NICKNAME";
        var email = decodedJWT.getClaim("email").asString();

        var password = UUID.randomUUID().toString();

        return new UserRegisterDTO(email, password, fullName, nickname, null, null);
    }

    @Override
    public String getClientId() {
        return "";
    }

    @Override
    public String getClientSecret() {
        return "";
    }

    @Override
    public String getRedirectUri() {
        return "";
    }

    @Override
    public String getPlatformUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    public String getPlatformScope() {
        return "https://www.googleapis.com/auth/userinfo.email&response_type=code";
    }

    @Override
    public String getPlatformTokenUri() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    public Map<String, String> getPlatformBodyParams(String code) {
        return Map.of("code", code, "client_id", clientId,
                        "client_secret", clientSecret, "redirect_uri", redirectUri,
                        "grant_type", "authorization_code");
    }

    @Override
    public String getTokenName() {
        return "id_token";
    }
}
