package fr.bgsoft.incredy.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseObjectDto {
	private Object data;

	@Builder.Default
	private List<ResponseMessageDto> messages = new ArrayList<>();
}
