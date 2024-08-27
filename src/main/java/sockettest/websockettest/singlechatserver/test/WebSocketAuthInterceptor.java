package sockettest.websockettest.singlechatserver.test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // instanceof 타입 확인 요청온게 ServletServerHttpRequest인지 아닌지
            if(request instanceof ServletServerHttpRequest){
                // 이게 뭐람? 이게 무슨 코드지?
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

                String userName = servletRequest.getHeader("Authorization");
                if(userName.startsWith("chatUser") && userName != null){
                    // JWT Token이나 다른 인증 로직을 사용하면 됨 지금은 연습이라 이렇게 해둔 상태
                    attributes.put("username", userName);
                    return true;
                }else{
                    servletResponse.setStatus(401);
                    return false;
                }
            }
            return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
