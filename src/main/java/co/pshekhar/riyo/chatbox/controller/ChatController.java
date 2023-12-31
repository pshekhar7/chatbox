package co.pshekhar.riyo.chatbox.controller;

import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.model.request.ChatHistoryRequest;
import co.pshekhar.riyo.chatbox.model.request.GetUnreadMsgRequest;
import co.pshekhar.riyo.chatbox.model.request.SendGroupMsgRequest;
import co.pshekhar.riyo.chatbox.model.request.SendMsgRequest;
import co.pshekhar.riyo.chatbox.model.response.ChatHistoryResponse;
import co.pshekhar.riyo.chatbox.model.response.GenericResponse;
import co.pshekhar.riyo.chatbox.repository.UserAccessRepository;
import co.pshekhar.riyo.chatbox.service.ChatService;
import co.pshekhar.riyo.chatbox.service.SessionService;
import co.pshekhar.riyo.chatbox.util.Constants;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ChatController {
    private final ChatService chatService;
    private final SessionService sessionService;

    private final UserAccessRepository userAccessRepository;

    public ChatController(ChatService chatService, SessionService sessionService, UserAccessRepository userAccessRepository) {
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.userAccessRepository = userAccessRepository;
    }

    @GetMapping(value = "/get/unread", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUnread(@Valid @RequestBody GetUnreadMsgRequest request,
                                     @RequestHeader(name = Constants.SESSION_TOKEN_HEADER_KEY, required = false) String sessionToken) {
        Either<Boolean, User> validationEither = validateSession(request.getUsername(), sessionToken, "");
        if (validationEither.isLeft()) {
            return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
        } else {
            request.setUser(validationEither.get());
        }
        return ResponseEntity.ok().body(chatService.getUnread(request));
    }

    @PostMapping(value = "/send/text/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> sendText(@Valid @RequestBody SendMsgRequest request,
                                    @RequestHeader(name = Constants.SESSION_TOKEN_HEADER_KEY, required = false) String sessionToken) {
        Either<Boolean, User> validationEither = validateSession(request.getFrom(), sessionToken, request.getTo());
        if (validationEither.isLeft()) {
            if (Boolean.TRUE == validationEither.getLeft()) {
                return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
            } else {
                return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("'to' is blocked. Can't send message").build());
            }
        } else {
            request.setFromUser(validationEither.get());
        }
        return ResponseEntity.ok().body(chatService.sendMessage(request));
    }

    @PostMapping(value = "/send/text/group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> sendTextGroup(@Valid @RequestBody SendGroupMsgRequest request,
                                         @RequestHeader(name = Constants.SESSION_TOKEN_HEADER_KEY, required = false) String sessionToken) {
        Either<Boolean, User> validationEither = validateSession(request.getFrom(), sessionToken, "");
        if (validationEither.isLeft()) {
            return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
        } else {
            request.setFromUser(validationEither.get());
        }
        return ResponseEntity.ok().body(chatService.sendGrpMessage(request));
    }

    @GetMapping(value = "/get/history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getHistory(@Valid @RequestBody ChatHistoryRequest request,
                                      @RequestHeader(name = Constants.SESSION_TOKEN_HEADER_KEY, required = false) String sessionToken) {
        Either<Boolean, User> validationEither = validateSession(request.getUser(), sessionToken, request.getFriend());
        if (validationEither.isLeft()) {
            if (Boolean.TRUE == validationEither.getLeft()) {
                return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
            }
        } else {
            request.setForUser(validationEither.get());
        }
        Either<GenericResponse, ChatHistoryResponse> chatEither = chatService.getChatHistory(request);
        return ResponseEntity.ok().body(chatEither.isLeft() ? chatEither.getLeft() : chatEither.get());
    }

    Either<Boolean, User> validateSession(String username, String sessionToken, String targetUser) {
        Either<Void, User> validation = sessionService.hasValidSession(username, sessionToken);
        if (validation.isRight()
                && userAccessRepository.findByOwningUserAndTargetUser(username, targetUser).isPresent()) {
            return Either.left(Boolean.FALSE);
        }
        return Either.left(Boolean.TRUE);
    }
}
