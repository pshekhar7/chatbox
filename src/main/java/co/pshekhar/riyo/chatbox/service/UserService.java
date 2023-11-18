package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.model.CreateUserRequest;
import co.pshekhar.riyo.chatbox.model.GenericResponse;
import co.pshekhar.riyo.chatbox.model.GetUsersResponse;
import co.pshekhar.riyo.chatbox.repository.UserRepository;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
