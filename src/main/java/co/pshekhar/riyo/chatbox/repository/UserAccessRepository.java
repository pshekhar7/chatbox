package co.pshekhar.riyo.chatbox.repository;

import co.pshekhar.riyo.chatbox.domain.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, String> {
    Optional<UserAccess> findByOwningUserAndTargetUser(String owningUser, String targetUser);

}
