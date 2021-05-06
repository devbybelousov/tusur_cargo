package com.tusur.cargo.controller;

import com.tusur.cargo.dto.ChatRequest;
import com.tusur.cargo.model.ChatMessage;
import com.tusur.cargo.service.ChatMessageService;
import com.tusur.cargo.service.ChatRoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;
  private final ChatMessageService chatMessageService;


  @MessageMapping("/chat")
  public void processMessage(@Payload ChatRequest chatRequest) {


    ChatMessage saved = chatMessageService.save(chatRequest);
    messagingTemplate.convertAndSendToUser(
        String.valueOf(saved.getRecipient().getUserId()), "/queue/messages",
        saved);
  }

  @GetMapping("/messages/{senderId}/{recipientId}/count")
  public ResponseEntity<Long> countNewMessages(
      @PathVariable Long senderId,
      @PathVariable Long recipientId) {

    return ResponseEntity
        .ok(chatMessageService.countNewMessages(senderId, recipientId));
  }

  @GetMapping("/messages/{senderId}/{recipientId}")
  public ResponseEntity<?> findChatMessages(@PathVariable Long senderId,
      @PathVariable Long recipientId) {
    return ResponseEntity
        .ok(chatMessageService.findChatMessages(senderId, recipientId));
  }

  @GetMapping("/messages/{id}")
  public ResponseEntity<?> findMessage(@PathVariable Long id) {
    return ResponseEntity
        .ok(chatMessageService.findById(id));
  }
}
