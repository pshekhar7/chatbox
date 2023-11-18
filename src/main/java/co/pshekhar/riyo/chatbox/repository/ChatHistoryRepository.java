package co.pshekhar.riyo.chatbox.repository;

import co.pshekhar.riyo.chatbox.domain.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, String> {
    List<ChatHistory> findAllByReceiverAndIsRead(String receiver, Boolean read);

    List<ChatHistory> findAllBySenderAndReceiver(String sender, String receiver);
}
