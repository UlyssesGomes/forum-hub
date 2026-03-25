package br.com.forum_hub.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u WHERE u.token = :token AND u.expirationToken > CURRENT_TIMESTAMP")
    Optional<User> findByTokenAndNotExpiredToken(String token);
}
