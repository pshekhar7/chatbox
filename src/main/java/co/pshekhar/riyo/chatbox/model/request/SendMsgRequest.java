package co.pshekhar.riyo.chatbox.model.request;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SendMsgRequest {
    @NotBlank(message = "from must not be blank")
    private String from;
    @NotBlank(message = "to must not be blank")
    private String to;
    @NotBlank(message = "text must not be blank")
    private String text;

    //derived
    private User fromUser;

    @AssertTrue(message = "from and to must be different")
    boolean isValid() {
        if (StringUtils.isBlank(to) || StringUtils.isBlank(from)) return true;
        return !to.equals(from);
    }
}
