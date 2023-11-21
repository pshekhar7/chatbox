package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.repository.SessionRepository;
import co.pshekhar.riyo.chatbox.repository.UserRepository;
import co.pshekhar.riyo.chatbox.util.Utilities;
import io.vavr.control.Either;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
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

    public Either<Void, User> hasValidSession(String username, String token) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (null == user) return Either.left(null);
        Session session;
        if (StringUtils.isBlank(token)) {
            session = sessionRepository.findByUser(user).orElse(null);
        } else {
            session = sessionRepository.findByUserAndToken(user, token).orElse(null);
        }
        if (null != session && !session.isExpired()) {
            return Either.right(user);
        } else {
            if (null != session) {
                // delete token
                sessionRepository.deleteById(session.getId());
            }
            return Either.left(null);
        }
    }

}
