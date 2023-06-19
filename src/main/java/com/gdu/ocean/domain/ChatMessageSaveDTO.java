package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageSaveDTO {
	private String roomId;
	private String writer;
	private String message;
}
