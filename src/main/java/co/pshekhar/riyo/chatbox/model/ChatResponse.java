package co.pshekhar.riyo.chatbox.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatResponse {
    private String status;
    private List<String> data;
}
