package co.pshekhar.riyo.chatbox.repository;

import co.pshekhar.riyo.chatbox.domain.ChatHistory;
import co.pshekhar.riyo.chatbox.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, String> {
    List<ChatHistory> findAllByReceiverAndRead(User receiver, Boolean read);

    List<ChatHistory> findAllBySenderAndReceiver(User sender, User receiver);
}
