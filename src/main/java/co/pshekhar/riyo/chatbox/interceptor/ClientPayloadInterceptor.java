package co.pshekhar.riyo.chatbox.interceptor;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.repository.SessionRepository;
import co.pshekhar.riyo.chatbox.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ClientPayloadInterceptor implements HandlerInterceptor {

    private final Environment env;
    private final SessionRepository sessionRepository;

    public ClientPayloadInterceptor(Environment env, SessionRepository sessionRepository) {
        this.env = env;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // check for security token flag and enforce token in API request headers only of it's true
        if (Boolean.TRUE.toString().equalsIgnoreCase(env.getProperty(Constants.SESSION_SECURITY_FLAG))) {
            final String sessionToken = request.getHeader(Constants.SESSION_TOKEN_HEADER_KEY);
            if (StringUtils.isBlank(sessionToken)) {
                String errorMessage = "Missing [" + Constants.SESSION_TOKEN_HEADER_KEY + "] in request header";
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
            }
            Session session = sessionRepository.findByToken(sessionToken).orElse(null);
            if (null == session) {
                String errorMessage = "Invalid [" + Constants.SESSION_TOKEN_HEADER_KEY + "] in request header";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
            } else if (session.isExpired()) {
                sessionRepository.deleteById(sessionToken); // soft delete
                String errorMessage = "Expired [" + Constants.SESSION_TOKEN_HEADER_KEY + "] token in request header. Please login again";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
            }
        }
        return true;
    }
}
