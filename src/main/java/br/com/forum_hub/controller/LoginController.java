package br.com.forum_hub.controller;

import br.com.forum_hub.domain.login.CreatedNewUserLoginDTO;
import br.com.forum_hub.domain.login.LoginDTO;
import br.com.forum_hub.domain.login.LoginTokenDTO;
import br.com.forum_hub.domain.login.NewUserLoginDTO;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.domain.user.UserService;
import br.com.forum_hub.infra.jwt.TokenManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<LoginTokenDTO> authenticate(@Valid @RequestBody LoginDTO loginDTO){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = tokenService.generateAccessToken((User) authentication.getPrincipal());
        String refreshToken = tokenService.generateRefreshToken((User) authentication.getPrincipal());
        LoginTokenDTO loginTokenDTO = new LoginTokenDTO(accessToken, refreshToken);

        return ResponseEntity.ok(loginTokenDTO);
    }

    @PostMapping("/new-user")
    public ResponseEntity<CreatedNewUserLoginDTO> registerNewUser(@RequestBody @Valid NewUserLoginDTO newUserLoginDTO, UriComponentsBuilder uriBuilder) {
        User user = userService.registerNewUser(newUserLoginDTO);
        CreatedNewUserLoginDTO createdUser = new CreatedNewUserLoginDTO(user);
        return ResponseEntity.created(null).body(createdUser);
    }

    @GetMapping("verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String verificationCode) throws ChangeSetPersister.NotFoundException {
        userService.verifyAccount(verificationCode);
        return ResponseEntity.ok("Account verified with success.");
    }

    @GetMapping("/logout")
    public ResponseEntity userLogout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
