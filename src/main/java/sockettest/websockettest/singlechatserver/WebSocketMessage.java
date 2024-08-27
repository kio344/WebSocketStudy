package sockettest.websockettest.singlechatserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 웹소켓 메시지 프로토콜
 */
@Getter
@Setter
@RequiredArgsConstructor
public class WebSocketMessage <T> {
    private WebSocketMessageType type;
    private T payload;

    @JsonIgnore
    public WebSocketMessage(
            @JsonProperty("type") WebSocketMessageType type,
            @JsonProperty("payload") T payload){
        this.type = type;
        this.payload = payload;
    }

    public static <T> WebSocketMessage<T> of(WebSocketMessageType type, T payload) {
        return new WebSocketMessage<>(type, payload);
    }
}
