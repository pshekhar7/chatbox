package co.pshekhar.riyo.chatbox.controller;

import co.pshekhar.riyo.chatbox.model.request.CreateUserRequest;
import co.pshekhar.riyo.chatbox.model.response.GenericResponse;
import co.pshekhar.riyo.chatbox.model.request.UserLoginRequest;
import co.pshekhar.riyo.chatbox.model.response.UserLoginResponse;
import co.pshekhar.riyo.chatbox.model.request.UserLogoutRequest;
import co.pshekhar.riyo.chatbox.service.UserService;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok().body(userService.createUser(request));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginRequest request) {
        Either<GenericResponse, UserLoginResponse> responseEither = userService.loginUser(request);
        return ResponseEntity.ok().body(responseEither.isLeft() ? responseEither.getLeft() : responseEither.get());
    }

    @GetMapping(value = "/get/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> logoutUser(@Valid @RequestBody UserLogoutRequest request) {
        return ResponseEntity.ok().body(userService.logoutUser(request));
    }
}
