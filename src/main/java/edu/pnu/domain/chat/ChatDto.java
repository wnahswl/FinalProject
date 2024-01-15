package edu.pnu.domain.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDto {
	
	// 방번호
	private Long roomId;
	// 보낸이
	private String sender; 
	// 메시지 내용
	private String message; 
	// 받는이
	private String receiver; 
	// 시간
	private LocalDateTime time; 
	//유저 목록을 표시할 리스트
	private List<String> userList;
	//접속상태
	private Boolean state;
	
	public String getFormattedTime() {
		// 시간을 "HH:mm" 형식의 문자열로 반환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return time.format(formatter);
	}

	public enum MessageType {
		CHAT, JOIN, LEAVE
	}
}
