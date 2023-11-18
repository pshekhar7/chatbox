package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.repository.SessionRepository;
import co.pshekhar.riyo.chatbox.util.Utilities;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String generateSessionToken(User user) {
        final String token = Utilities.generateRandomId();
        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        sessionRepository.save(session);
        return token;
    }

    public String expireAndGetSessionToken(User user, Session session) {
        if (session != null) {
            sessionRepository.deleteById(session.getId());
        }
        return generateSessionToken(user);
    }

    public boolean isValidToken(User user, String token) {
        return null != sessionRepository.findByUserAndToken(user, token).orElse(null);
    }

}
