package sockettest.websockettest.singlechatserver.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sockettest.websockettest.singlechatserver.WebSocketMessage;
import sockettest.websockettest.singlechatserver.WebSocketMessageType;
import sockettest.websockettest.singlechatserver.dto.ChatDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ChatRoom {
    private final Map<String, WebSocketSession> activeUserMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    /**
     * 채팅방 입장시 사용할 메서드
     *
     */
    public void enter(ChatDto chatDto, WebSocketSession session) {

        String userName = (String) session.getAttributes().get("username");
        activeUserMap.put(userName, session);
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try{
                if(!entry.getKey().equals(userName)) {
//                if(entry.getKey().equals(userName)) {
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.ENTER, chatDto));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 채팅방 퇴장할 때 사용될 메서드
     */
    public void exit(String userName, ChatDto chatDto){
        activeUserMap.remove(chatDto.getUserName());
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()){
            try {
                if(!entry.getKey().equals(userName))
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.EXIT, chatDto));
            }catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void sendMessage(String userName, ChatDto chatDto){
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()){
            try {
                if(!entry.getKey().equals(userName))
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.TALK, chatDto));
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
    }


    private TextMessage getTextMessage (WebSocketMessageType type, ChatDto chatDto){
        try{
            return new TextMessage(
                    objectMapper.writeValueAsString(
                            new WebSocketMessage<>(type, chatDto)
                    ));
        }catch(JsonProcessingException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
