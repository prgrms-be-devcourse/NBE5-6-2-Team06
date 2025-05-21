package com.grepp.matnam.app.controller.api.chat;

import com.grepp.matnam.app.model.chat.ChatService;
import com.grepp.matnam.app.model.chat.dto.MessageDto;
import com.grepp.matnam.infra.response.ApiResponse;
import com.grepp.matnam.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping("/history/{roomId}")
    @Operation(summary = "채팅 기록 조회", description = "팀의 이전 채팅 내용을 조회합니다.")
    public ApiResponse<List<MessageDto>> getChatHistory(@PathVariable Long roomId) {
        try{
            List<MessageDto> chatHistory = chatService.getChatHistory(roomId);
            return ApiResponse.success(chatHistory);
        }catch(IllegalArgumentException e){
            return ApiResponse.error(ResponseCode.BAD_REQUEST);
        }catch(Exception e){
            return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR);
        }

    }
}

