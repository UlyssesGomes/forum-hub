package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.LoginDTO;
import br.com.forum_hub.domain.login.LoginTokenDTO;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.infra.jwt.TokenManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenService;

    @PostMapping()
    public ResponseEntity<LoginTokenDTO> authenticate(@Valid @RequestBody LoginDTO loginDTO){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((User) authentication.getPrincipal());
        LoginTokenDTO loginTokenDTO = new LoginTokenDTO(accessToken, refreshToken);

        return ResponseEntity.ok(loginTokenDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity userLogout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
