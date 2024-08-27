package sockettest.websockettest.singlechatserver.test;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketChatHandler webSocketChatHandler;
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /**
         * 웹소켓 연결을 위한 설정이다.
         * 연결 엔드포인트는 ws://localhost:8080/chats
         * ws는 웹소켓할때 이렇게 쓰던데.. wss도
         * 위에 연결시 동작할 핸들러는 webSocketHandler
         * /chats url로 들어오면 동작하는건가
         *
         */
        registry.addHandler(webSocketChatHandler, "/chats")
                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*");
    }
}
