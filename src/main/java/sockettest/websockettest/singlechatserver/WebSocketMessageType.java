package sockettest.websockettest.singlechatserver;


public enum WebSocketMessageType {
    /**
     * 웹소켓 메세지 타입 정리
     */
    ENTER("ENTER"),
    JOIN("JOIN"),
    TALK("TALK"),
    EXIT("EXIT"),
    SUB("SUBSCRIBE"),
    PUB("PUBLISH");

    private final String type;

    WebSocketMessageType(String type) {
        this.type = type;
    }

    public String getValue(){
        return this.type;
    }
}

