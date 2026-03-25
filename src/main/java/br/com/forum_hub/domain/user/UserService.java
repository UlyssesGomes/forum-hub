package br.com.forum_hub.domain.user;

import br.com.forum_hub.domain.login.NewUserLoginDTO;
import br.com.forum_hub.infra.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Email or password incorrect.");
        });
    }

    public User registerNewUser(NewUserLoginDTO newUser) {
        User user = new User(null,
                newUser.fullName(),
                newUser.email(),
                encoder.encode(newUser.password()),
                newUser.nickname(),
                newUser.biography(),
                newUser.shortBiography(),
                false,
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(2));

        return repository.save(user);
    }

    @Transactional
    public void verifyAccount(String verificationCode) throws NotFoundException {
        User user = repository.findByTokenAndNotExpiredToken(verificationCode).orElseThrow(() -> new BusinessException("Falha na verificação do token."));
        user.setToken("");
        user.setExpirationToken(null);
        user.setVerified(true);
    }
}
