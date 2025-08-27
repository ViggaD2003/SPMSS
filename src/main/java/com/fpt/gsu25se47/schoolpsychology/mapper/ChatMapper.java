package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.model.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ChatMapper {

//    @Mappings({
//            @Mapping(target = "createdAt", source = "chatMessageDto.timestamp"),
//            @Mapping(target = "content", source = "chatMessageDto.message"),
//            @Mapping(target = "sender", ignore = true)
//    })
//    ChatMessage toChatMessage(ChatMessageDto chatMessageDto);

    @Mappings({
            @Mapping(target = "sender", source = "chatMessage.email"),
            @Mapping(target = "message", source = "chatMessage.content"),
            @Mapping(target = "timestamp", source = "chatMessage.createdAt"),
            @Mapping(target = "type", constant = "CHAT")
    })
    ChatMessageDto toChatMessageDto(ChatMessage chatMessage);
}
