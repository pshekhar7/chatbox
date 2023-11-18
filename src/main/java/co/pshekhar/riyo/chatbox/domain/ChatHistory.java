package co.pshekhar.riyo.chatbox.domain;

import co.pshekhar.riyo.chatbox.util.PIIDataConverter;
import co.pshekhar.riyo.chatbox.util.UniqueIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(columnList = "receiver", name = "receiver_idx")})
public class ChatHistory {
    public ChatHistory() {
        isRead = Boolean.FALSE;
    }

    @Id
    @GenericGenerator(name = "chat_history_id", type = UniqueIdGenerator.class)
    @GeneratedValue(generator = "chat_history_id")
    @Setter(AccessLevel.NONE)
    private String id;

    private String receiver; // TODO: index this field

    private String sender;

    @Convert(converter = PIIDataConverter.class)
    private String message;

    Boolean isRead;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedOn;

}
