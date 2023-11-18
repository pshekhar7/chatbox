package co.pshekhar.riyo.chatbox.domain;

import co.pshekhar.riyo.chatbox.util.PIIDataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(columnList = "receiver", name = "receiver_idx")})
public class ChatHistory {
    public ChatHistory() {
        read = Boolean.FALSE;
    }

    @Id
    @GeneratedValue
    private String id;

    @OneToOne
    private User receiver; // TODO: index this field

    @OneToOne
    private User sender;

    @Convert(converter = PIIDataConverter.class)
    private String message;

    Boolean read;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedOn;

}
