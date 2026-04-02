package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.LoginTokenDTO;
import br.com.forum_hub.domain.login.external.AbstractExternalLoginService;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.infra.jwt.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

public abstract class AbstractExternalLoginController {

    @Autowired
    private TokenManager tokenService;

    @GetMapping
    public ResponseEntity<Void> redirectToGithub(){
        var url = getService().generateUrl();

        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/authorized")
    public ResponseEntity<LoginTokenDTO> oAuthUserAuthenticate(@RequestParam String code){

        var user = getService().verifyExternalLoginUserAndRegister(code);
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginTokenDTO(accessToken, refreshToken));
    }

    public abstract AbstractExternalLoginService getService();
}
