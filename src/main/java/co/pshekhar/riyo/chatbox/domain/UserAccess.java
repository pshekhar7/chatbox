package co.pshekhar.riyo.chatbox.domain;

import co.pshekhar.riyo.chatbox.util.UniqueIdGenerator;
import jakarta.persistence.Column;
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
//@Table(indexes = {@Index(columnList = "owning_user", name = "owning_user_idx")})
public class UserAccess {
    public UserAccess() {
        isMessagingBlocked = Boolean.TRUE;
    }

    @Id
    @GenericGenerator(name = "user_access_id", type = UniqueIdGenerator.class)
    @GeneratedValue(generator = "user_access_id")
    @Setter(AccessLevel.NONE)
    private String id;

    private String owningUser;

    private String targetUser;

    // one of the access rights
    // in future can be add more if such usecases
    Boolean isMessagingBlocked;

    @CreationTimestamp
    @Column(updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastUpdatedOn;

}
