package co.pshekhar.riyo.chatbox.service;

import co.pshekhar.riyo.chatbox.domain.ChatHistory;
import co.pshekhar.riyo.chatbox.domain.User;
import co.pshekhar.riyo.chatbox.model.ChatHistoryRequest;
import co.pshekhar.riyo.chatbox.model.ChatHistoryResponse;
import co.pshekhar.riyo.chatbox.model.GenericResponse;
import co.pshekhar.riyo.chatbox.model.GetUnreadMsgRequest;
import co.pshekhar.riyo.chatbox.model.SendMsgRequest;
import co.pshekhar.riyo.chatbox.model.UnreadMsgResponse;
import co.pshekhar.riyo.chatbox.model.helper.UnreadMsg;
import co.pshekhar.riyo.chatbox.repository.ChatHistoryRepository;
import co.pshekhar.riyo.chatbox.repository.UserRepository;
import io.vavr.control.Either;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final UserRepository userRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatService(UserRepository userRepository, ChatHistoryRepository chatHistoryRepository) {
        this.userRepository = userRepository;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public GenericResponse sendMessage(SendMsgRequest request) {
        User toUser = userRepository.findByUsername(request.getTo()).orElse(null);
        if (null == toUser) {
            return GenericResponse.builder().status("failure").message("'to' user: [" + request.getTo() + "] not found").build();
        }
        ChatHistory chat = new ChatHistory();
        chat.setSender(request.getFromUser());
        chat.setReceiver(toUser);
        chat.setMessage(request.getText());
        chatHistoryRepository.save(chat);
        return GenericResponse.builder().status("success").build();
    }

    public UnreadMsgResponse getUnread(GetUnreadMsgRequest request) {
        List<ChatHistory> unreads = chatHistoryRepository.findAllByReceiverAndIsRead(request.getUser(), Boolean.FALSE);
        boolean hasUnread = CollectionUtils.isNotEmpty(unreads);
        return UnreadMsgResponse
                .builder()
                .status("success")
                .message(hasUnread ? "You have message(s)" : "No new messages")
                .data(hasUnread ? getUnreadResponse(unreads) : null)
                .build();
    }

    public Either<GenericResponse, ChatHistoryResponse> getChatHistory(ChatHistoryRequest request) {
        User friend = userRepository.findByUsername(request.getFriend()).orElse(null);
        if (null == friend) {
            return Either.left(GenericResponse.builder().status("failure").message("'friend' user: [" + request.getFriend() + "] not found").build());
        }
        List<ChatHistory> userMsgs = chatHistoryRepository.findAllBySenderAndReceiver(request.getForUser(), friend);
        List<ChatHistory> friendMsgs = chatHistoryRepository.findAllBySenderAndReceiver(friend, request.getForUser());
        userMsgs.addAll(friendMsgs);
        List<ChatHistory> chats = new ArrayList<>(userMsgs);
        final List<Map<String, String>> res = new ArrayList<>();
        chats
                .stream()
                .sorted((ch1, ch2) -> {
                    if (ch1.getCreatedOn().isBefore(ch2.getCreatedOn())) {
                        return -1;
                    } else if (ch1.getCreatedOn().isBefore(ch2.getCreatedOn())) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .map(ch -> res.add(Collections.singletonMap(ch.getSender().getUsername(), ch.getMessage())))
                .toList();
        return Either.right(ChatHistoryResponse
                .builder()
                .status("success")
                .message(chats.size() > 0 ? null : "No chat history")
                .texts(res)
                .build());
    }

    private List<UnreadMsg> getUnreadResponse(List<ChatHistory> unreads) {
        Map<String, List<String>> res = new HashMap<>();
        unreads
                .stream()
                .map(chatHistory -> {
                    final String sender = chatHistory.getSender().getUsername();
                    if (!res.containsKey(sender)) {
                        res.put(sender, new ArrayList<>());
                    }
                    res.get(sender).add(chatHistory.getMessage());
                    chatHistory.setIsRead(Boolean.TRUE);
                    return 1;
                })
                .toList();
        List<UnreadMsg> unreadMsgList = new ArrayList<>();
        res.forEach((k, v) -> {
            unreadMsgList.add(UnreadMsg
                    .builder()
                    .username(k)
                    .texts(v)
                    .build());
        });
        chatHistoryRepository.saveAll(unreads); // update 'read' change for all
        return unreadMsgList;
    }
}