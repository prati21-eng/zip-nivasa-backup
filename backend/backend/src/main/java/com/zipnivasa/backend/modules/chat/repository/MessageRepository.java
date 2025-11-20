package com.zipnivasa.backend.modules.chat.repository;

import com.zipnivasa.backend.modules.chat.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findBySenderAndReceiverOrderByCreatedAtAsc(String sender, String receiver);
    List<Message> findByReceiverAndSenderOrderByCreatedAtAsc(String receiver, String sender);

    List<Message> findBySenderOrReceiverOrderByCreatedAtDesc(String sender, String receiver);

    List<Message> findBySenderAndReceiverAndReadAtIsNull(String sender, String receiver);
}
