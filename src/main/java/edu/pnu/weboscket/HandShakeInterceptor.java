package edu.pnu.weboscket;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HandShakeInterceptor extends HttpSessionHandshakeInterceptor{
	//이 인터셉터는 HttpServletRequest에 접근할 수 있기 때문에 요청 파라미터에 포함된
	//이용자 아이디를 추룰하여 뒤이어 실행될 웹소켓 핸들러에게 전달할 수 있다.
	//https://micropilot.tistory.com/2768 출처
	
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler webSocketHandler,Map<String, Object> attributes) throws Exception  {
		//위의 파라미터 중, attributes에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
		log.info("Before HandShake");
		
		ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
		log.info("URI : {} ", request.getURI());
		
		HttpServletRequest req = httpRequest.getServletRequest();
		
		//HttpSession에 저장된 이용자의 아이디를 추출하는 경우 
		String id = (String) req.getSession().getAttribute("userInfo");
		attributes.put("userInfo", id);
		log.info("HttpSession에 저장된 id : {} " , id);
		
		return super.beforeHandshake(request, response, webSocketHandler, attributes);
		
	}
	
	public void afterHandShake(ServerHttpRequest request,ServerHttpResponse response,
			WebSocketHandler webSocketHandler,Exception e) {
		log.info("After Handshake");
		super.afterHandshake(request, response, webSocketHandler, e);
	}
	
	

}
