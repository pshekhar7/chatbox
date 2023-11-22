package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.Session;
import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.domain.UserAccess;
import co.pshekhar.riyo.chatbox.model.request.BlockUserRequest;
import co.pshekhar.riyo.chatbox.model.request.CreateUserRequest;
import co.pshekhar.riyo.chatbox.model.request.UserLoginRequest;
import co.pshekhar.riyo.chatbox.model.request.UserLogoutRequest;
import co.pshekhar.riyo.chatbox.model.response.GenericResponse;
import co.pshekhar.riyo.chatbox.model.response.GetUsersResponse;
import co.pshekhar.riyo.chatbox.model.response.UserLoginResponse;
import co.pshekhar.riyo.chatbox.repository.SessionRepository;
import co.pshekhar.riyo.chatbox.repository.UserAccessRepository;
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

    private final UserAccessRepository userAccessRepository;

    private final Environment env;

    public UserService(UserRepository userRepository, SessionRepository sessionRepository, SessionService sessionService, UserAccessRepository userAccessRepository, Environment env) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
        this.userAccessRepository = userAccessRepository;
        this.env = env;
    }

    public Either<GenericResponse, UserLoginResponse> loginUser(UserLoginRequest request) {
        User existingUser = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPasscode()).orElse(null);
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

    public GenericResponse logoutUser(UserLogoutRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (null == existingUser) {
            return GenericResponse
                    .builder()
                    .status("failure")
                    .message("User not found")
                    .build();
        }
        sessionRepository.findByUser(existingUser).ifPresent(sessionRepository::delete);
        return GenericResponse
                .builder()
                .status("success")
                .build();
    }

    public GenericResponse blockUser(BlockUserRequest request) {
        User existingUser = userRepository.findByUsername(request.getUserToBlock()).orElse(null);
        if (null == existingUser) {
            return GenericResponse
                    .builder()
                    .status("failure")
                    .message("User to be blocked not found")
                    .build();
        }

        userAccessRepository.findByOwningUserAndTargetUser(request.getUsername(), request.getUserToBlock()).ifPresentOrElse(ua -> {
        }, () -> {
            UserAccess userAccess = new UserAccess();
            userAccess.setOwningUser(request.getUsername());
            userAccess.setTargetUser(request.getUserToBlock());
            userAccess.setIsMessagingBlocked(Boolean.TRUE);
            userAccessRepository.save(userAccess);
        });

        return GenericResponse
                .builder()
                .status("success")
                .build();
    }

    public GenericResponse createUser(CreateUserRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (null != existingUser) {
            return GenericResponse
                    .builder()
                    .status("failure")
                    .message("User already exists")
                    .build();
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPasscode());
        userRepository.save(user);
        return GenericResponse
                .builder()
                .status("success")
                .build();
    }

    public GetUsersResponse getAllUsers() {
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
