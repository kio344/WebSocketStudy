package sockettest.websockettest.singlechatserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChatDto {
    private Long chatRoomId;
    private String userName;
    @Setter
    private String message;

    //왜 해줬을까; TODO
    @JsonCreator
    public ChatDto(@JsonProperty("chatRoomId") Long chatRoomId,
                   @JsonProperty("username") String userName,
                   @JsonProperty("message") String message) {
        this.chatRoomId = chatRoomId;
        this.userName = userName;
        this.message = message;
    }
}
