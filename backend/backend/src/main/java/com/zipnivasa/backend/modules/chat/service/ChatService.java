package com.zipnivasa.backend.modules.chat.service;

import com.zipnivasa.backend.modules.chat.dto.MessageResponse;
import com.zipnivasa.backend.modules.chat.dto.SendMessageRequest;
import com.zipnivasa.backend.modules.chat.model.Message;
import com.zipnivasa.backend.modules.chat.repository.MessageRepository;
import com.zipnivasa.backend.modules.user.model.User;
import com.zipnivasa.backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    // ------------------- SEND MESSAGE -----------------------
    public MessageResponse send(String senderId, SendMessageRequest req) {
        Message saved = messageRepository.save(
                Message.builder()
                        .sender(senderId)
                        .receiver(req.getReceiver())
                        .message(req.getMessage().trim())
                        .createdAt(Instant.now())
                        .readAt(null)
                        .build()
        );

        return toResponse(saved);
    }

    // ------------------- GET HISTORY ------------------------
    public List<MessageResponse> getHistory(String me, String other) {
        List<Message> messages1 = messageRepository.findBySenderAndReceiverOrderByCreatedAtAsc(me, other);
        List<Message> messages2 = messageRepository.findByReceiverAndSenderOrderByCreatedAtAsc(me, other);

        List<Message> merged = new ArrayList<>();
        merged.addAll(messages1);
        merged.addAll(messages2);

        merged.sort(Comparator.comparing(Message::getCreatedAt));

        return merged.stream().map(this::toResponse).toList();
    }

    // ------------------- GET CONVERSATIONS -------------------
    public List<Map<String, Object>> getConversations(String myId) {

        List<Message> msgs = messageRepository.findBySenderOrReceiverOrderByCreatedAtDesc(myId, myId);

        Map<String, Map<String, Object>> map = new LinkedHashMap<>();

        for (Message m : msgs) {
            String other = m.getSender().equals(myId) ? m.getReceiver() : m.getSender();

            map.putIfAbsent(other, new HashMap<>());

            Map<String, Object> entry = map.get(other);

            entry.put("lastMessage", toResponse(m));

            long unread = messageRepository
                    .findBySenderAndReceiverAndReadAtIsNull(other, myId)
                    .size();

            entry.put("unreadCount", unread);
        }

        // Add partner details
        for (String partnerId : map.keySet()) {
            Optional<User> u = userRepository.findById(partnerId);
            u.ifPresent(user ->
                    map.get(partnerId).put("user",
                            Map.of(
                                    "id", user.getId(),
                                    "name", user.getName(),
                                    "phone", user.getPhone(),
                                    "role", user.getRole()
                            )
                    )
            );
        }

        return new ArrayList<>(map.values());
    }

    // ------------------- MARK AS READ ------------------------
    public void markAsRead(String myId, String partnerId) {
        List<Message> unread = messageRepository
                .findBySenderAndReceiverAndReadAtIsNull(partnerId, myId);

        unread.forEach(m -> {
            m.setReadAt(Instant.now());
            messageRepository.save(m);
        });
    }

    // ------------------- Mapper ------------------------------
    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
                .id(m.getId())
                .sender(m.getSender())
                .receiver(m.getReceiver())
                .message(m.getMessage())
                .createdAt(m.getCreatedAt())
                .readAt(m.getReadAt())
                .build();
    }
}
