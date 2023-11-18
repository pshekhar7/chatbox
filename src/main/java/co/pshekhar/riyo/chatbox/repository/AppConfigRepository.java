package co.pshekhar.riyo.chatbox.repository;

import co.pshekhar.riyo.chatbox.domain.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, String> {
    AppConfig findByName(String name);
}
