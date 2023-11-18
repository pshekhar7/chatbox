package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.model.CreateUserRequest;
import co.pshekhar.riyo.chatbox.model.GenericResponse;
import co.pshekhar.riyo.chatbox.model.GetUsersResponse;
import co.pshekhar.riyo.chatbox.model.UserLoginRequest;
import co.pshekhar.riyo.chatbox.model.UserLoginResponse;
import co.pshekhar.riyo.chatbox.model.UserLogoutRequest;
import co.pshekhar.riyo.chatbox.repository.SessionRepository;
import co.pshekhar.riyo.chatbox.repository.UserRepository;
import co.pshekhar.riyo.chatbox.util.Constants;
import io.vavr.control.Either;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    private final Environment env;

    public UserService(UserRepository userRepository, SessionRepository sessionRepository, SessionService sessionService, Environment env) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
        this.env = env;
    }

    Either<GenericResponse, UserLoginResponse> loginUser(UserLoginRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (null == existingUser) {
            return Either.left(GenericResponse
                    .builder()
                    .status("failure")
                    .message("User not found")
                    .build());
        }
        Session session = sessionRepository.findByUser(existingUser).orElse(null);

        boolean sendToken = Boolean.TRUE.toString().equalsIgnoreCase(env.getProperty(Constants.SESSION_SECURITY_FLAG));
        final String sessionToken;
        if (null != session) {
            // expire current session and return
            sessionToken = sessionService.expireAndGetSessionToken(existingUser, session);
        } else {
            // return new session
            sessionToken = sessionService.generateSessionToken(existingUser);
        }
        return Either.right(UserLoginResponse.builder().status("success").sessionToken(sendToken ? sessionToken : null).build());
    }

    Either<GenericResponse, GenericResponse> logoutUser(UserLogoutRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (null == existingUser) {
            return Either.left(GenericResponse
                    .builder()
                    .status("failure")
                    .message("User not found")
                    .build());
        }
        sessionRepository.findByUser(existingUser).ifPresent(sessionRepository::delete);
        return Either.right(GenericResponse
                .builder()
                .status("success")
                .build());
    }

    Either<GenericResponse, User> createUser(CreateUserRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (null != existingUser) {
            return Either.left(GenericResponse
                    .builder()
                    .status("failure")
                    .message("User already exists")
                    .build());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPasscode());
        User newUser = userRepository.save(user);
        return Either.right(newUser);
    }

    GetUsersResponse getAllUsers(CreateUserRequest request) {
        List<String> users = userRepository
                .findAll()
                .stream()
                .map(User::getUsername)
                .toList();
        return GetUsersResponse
                .builder()
                .status("success")
                .data(users)
                .build();
    }
}
