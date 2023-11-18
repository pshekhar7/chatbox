package co.pshekhar.riyo.chatbox.repository;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findByToken(String token);

    Optional<Session> findByUser(User user);

    Optional<Session> findByUserAndToken(User user, String token);

    @Transactional
    @Modifying
    void deleteById(String id);
}
