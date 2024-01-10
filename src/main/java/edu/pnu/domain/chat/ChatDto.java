package edu.pnu.domain.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatDto {

	private String sender; // 보낸이
	private String message; // 메시지
	private String receiver; // 받는이
	private LocalDateTime time; // 시간

	public String getFormattedTime() {
		// 시간을 "HH:mm" 형식의 문자열로 반환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return time.format(formatter);
	}

	public enum MessageType {
		CHAT, JOIN, LEAVE
	}
}
