package co.pshekhar.riyo.chatbox.controller;

import co.pshekhar.riyo.chatbox.model.ChatHistoryRequest;
import co.pshekhar.riyo.chatbox.model.ChatHistoryResponse;
import co.pshekhar.riyo.chatbox.model.GenericResponse;
import co.pshekhar.riyo.chatbox.model.GetUnreadMsgRequest;
import co.pshekhar.riyo.chatbox.model.SendMsgRequest;
import co.pshekhar.riyo.chatbox.service.ChatService;
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

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/get/unread", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(@RequestBody GetUnreadMsgRequest request) {
        return ResponseEntity.ok().body(chatService.getUnread(request));
    }

    @PostMapping(value = "/send/text/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(@RequestBody SendMsgRequest request) {
        return ResponseEntity.ok().body(chatService.sendMessage(request));
    }

    @GetMapping(value = "/get/history", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> createUser(@RequestBody ChatHistoryRequest request) {
        Either<GenericResponse, ChatHistoryResponse> chatEither = chatService.getChatHistory(request);
        return ResponseEntity.ok().body(chatEither.isLeft() ? chatEither.getLeft() : chatEither.get());
    }
}
