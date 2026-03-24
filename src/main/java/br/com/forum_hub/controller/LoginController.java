package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.LoginDTO;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.infra.jwt.TokenManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenService;

    @PostMapping()
    public ResponseEntity<String> authenticate(@Valid @RequestBody LoginDTO loginDTO){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(accessToken);
    }
}
