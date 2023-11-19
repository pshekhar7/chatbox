package co.pshekhar.riyo.chatbox.model.request;

import co.pshekhar.riyo.chatbox.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class SendGroupMsgRequest {
    @NotBlank(message = "from must not be blank")
    private String from;
    @NotBlank(message = "toList must not be blank")
    private List<String> toList;
    @NotBlank(message = "text must not be blank")
    private String text;

    //derived
    private User fromUser;

    @AssertTrue(message = "toList must not contain value in from")
    boolean isValid() {
        if (StringUtils.isBlank(from) || CollectionUtils.isEmpty(toList)) return true;
        return !toList.contains(from);
    }
}
