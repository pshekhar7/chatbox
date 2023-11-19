package co.pshekhar.riyo.chatbox.model.response;

import co.pshekhar.riyo.chatbox.model.helper.UnreadMsg;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UnreadMsgResponse {
    private String status;
    private String message;
    private List<UnreadMsg> data;
}
