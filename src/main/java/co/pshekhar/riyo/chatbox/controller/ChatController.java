package co.pshekhar.riyo.chatbox.controller;

import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.model.ChatHistoryRequest;
import co.pshekhar.riyo.chatbox.model.ChatHistoryResponse;
import co.pshekhar.riyo.chatbox.model.GenericResponse;
import co.pshekhar.riyo.chatbox.model.GetUnreadMsgRequest;
import co.pshekhar.riyo.chatbox.model.SendMsgRequest;
import co.pshekhar.riyo.chatbox.service.ChatService;
import co.pshekhar.riyo.chatbox.service.SessionService;
import io.vavr.control.Either;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ChatController {
    private final ChatService chatService;
    private final SessionService sessionService;

    public ChatController(ChatService chatService, SessionService sessionService) {
        this.chatService = chatService;
        this.sessionService = sessionService;
    }

    @GetMapping(value = "/get/unread", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getUnread(@RequestBody GetUnreadMsgRequest request) {
        Either<Void, User> validationEither = validateSession(request.getUsername());
        if (validationEither.isLeft()) {
            return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
        } else {
            request.setUser(validationEither.get());
        }
        return ResponseEntity.ok().body(chatService.getUnread(request));
    }

    @PostMapping(value = "/send/text/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> sendText(@RequestBody SendMsgRequest request) {
        Either<Void, User> validationEither = validateSession(request.getFrom());
        if (validationEither.isLeft()) {
            return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
        } else {
            request.setFromUser(validationEither.get());
        }
        return ResponseEntity.ok().body(chatService.sendMessage(request));
    }

    @GetMapping(value = "/get/history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> getHistory(@RequestBody ChatHistoryRequest request) {
        Either<Void, User> validationEither = validateSession(request.getUser());
        if (validationEither.isLeft()) {
            return ResponseEntity.status(403).body(GenericResponse.builder().status("failure").message("Invalid session. Please login and retry").build());
        } else {
            request.setForUser(validationEither.get());
        }
        Either<GenericResponse, ChatHistoryResponse> chatEither = chatService.getChatHistory(request);
        return ResponseEntity.ok().body(chatEither.isLeft() ? chatEither.getLeft() : chatEither.get());
    }

    Either<Void, User> validateSession(String username) {
        return sessionService.hasValidSession(username, null);
    }
}
