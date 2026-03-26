package br.com.forum_hub.domain.user;

import br.com.forum_hub.domain.login.NewUserLoginDTO;
import br.com.forum_hub.domain.role.Role;
import br.com.forum_hub.domain.role.RoleDTO.RoleDTO;
import br.com.forum_hub.domain.role.RoleEnum;
import br.com.forum_hub.domain.role.RoleRepository;
import br.com.forum_hub.infra.exception.BusinessException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Email or password incorrect.");
        });
    }

    public User registerNewUser(NewUserLoginDTO newUser) {
        Role role = roleRepository.findByItsName(RoleEnum.PARTICIPANT).orElseThrow(() -> new BusinessException("Role não encontrado."));

        User user = new User(null,
                newUser.fullName(),
                newUser.email(),
                encoder.encode(newUser.password()),
                newUser.nickname(),
                newUser.biography(),
                newUser.shortBiography(),
                false,
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(2),
                List.of(role));

        return repository.save(user);
    }

    @Transactional
    public void verifyAccount(String verificationCode) throws NotFoundException {
        User user = repository.findByTokenAndNotExpiredToken(verificationCode).orElseThrow(() -> new BusinessException("Falha na verificação do token."));
        user.setToken("");
        user.setExpirationToken(null);
        user.setVerified(true);
    }

    @Transactional
    public void addRoleToUserById(RoleDTO roleDto, long id) {
        User user = repository.findById(id).orElseThrow(() -> new BusinessException("Id de usuário inválido."));
        Role role = roleRepository.findByItsName(roleDto.name()).orElseThrow(() -> new BusinessException("Role inválido."));

        user.getRoles().add(role);
    }

    @Transactional
    public void removeRoleFromUser(@Valid RoleDTO roleDto, long id) {
        User user = repository.findById(id).orElseThrow(() -> new BusinessException("Id de usuário inválido."));
        Role role = roleRepository.findByItsName(roleDto.name()).orElseThrow(() -> new BusinessException("Role inválido."));

        user.getRoles().remove(role);
    }
}
