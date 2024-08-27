package sockettest.websockettest.singlechatserver.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sockettest.websockettest.singlechatserver.WebSocketMessage;
import sockettest.websockettest.singlechatserver.chat.ChatRoom;
import sockettest.websockettest.singlechatserver.dto.ChatDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler{

    private final Map<Long, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param session
     * @param message
     * @throws JsonProcessingException
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        System.out.println("session : " + session);
        System.out.println("message : "  + message.getPayload());
        String username = (String) session.getAttributes().get("username");

        // 정확하게 여기서 터지네 왜지?
        WebSocketMessage<ChatDto> webSocketMessage = objectMapper.readValue(
                message.getPayload(),
                new TypeReference<>() {
                }
        );
        // 아 ENTER메서드에 들어갔다는거임
        // 된게 아니라

        switch (webSocketMessage.getType().getValue()) {
            case "ENTER" -> enterChatRoom(webSocketMessage.getPayload(), session);
            case "TALK" -> sendMessage(username, webSocketMessage.getPayload());
            case "EXIT" -> exitChatRoom(username, webSocketMessage.getPayload());
        }
    }

    private void sendMessage(String userName, ChatDto chatDto){
        log.info("send chatDto : " + chatDto.toString());
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.sendMessage(userName, chatDto);
    }

    private void enterChatRoom(ChatDto chatDto, WebSocketSession session){
        log.info("enter chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUserName() + "님이 입장하셨습니다.");
        ChatRoom chatRoom = chatRoomMap.getOrDefault(chatDto.getChatRoomId(), new ChatRoom(objectMapper));
        chatRoom.enter(chatDto, session);
        chatRoomMap.put(chatDto.getChatRoomId(), chatRoom);
    }

    private void exitChatRoom(String userName, ChatDto chatDto){
        log.info("exit chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUserName() + "님이 퇴장하셨습니다.");
        ChatRoom chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.exit(userName, chatDto);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        /**
         * 웹소켓 연결 성공 시
         */
        try {
            session.sendMessage(
                    new TextMessage("연결 성공"));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
