package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.github.LoginGithubService;
import br.com.forum_hub.domain.login.LoginTokenDTO;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.domain.user.UserService;
import br.com.forum_hub.infra.jwt.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/login/github")
public class LoginGithubController {

    @Autowired
    private LoginGithubService loginGithubService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenService;

    @GetMapping
    public ResponseEntity<Void> redirectToGithub(){
        var url = loginGithubService.generateUrl();

        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/authorized")
    public ResponseEntity<LoginTokenDTO> oAuthUserAuthenticate(@RequestParam String code){

        var email = loginGithubService.getEmail(code);
        var user = (User) userService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginTokenDTO(accessToken, refreshToken));
    }

    @GetMapping("/register")
    public ResponseEntity<Void> redirectGithubRegister(){
        var url = loginGithubService.generateUrlRegister();

        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/register-authorized")
    public ResponseEntity<LoginTokenDTO> getToken(@RequestParam String code){
        var userData = loginGithubService.getOAuthData(code);
        var user = userService.registerVerifiedNewUser(userData);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginTokenDTO(accessToken, refreshToken));
    }
}