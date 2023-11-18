package co.pshekhar.riyo.chatbox.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatHistoryResponse {
    private String status;
    private String message;
    private List<Map<String, String>> texts;
}
