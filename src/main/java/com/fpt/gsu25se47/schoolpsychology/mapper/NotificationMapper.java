package com.fpt.gsu25se47.schoolpsychology.mapper;


import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Notifications;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {

    @Autowired
    protected AccountMapper accountMapper;

    @Mappings({
            @Mapping(target = "receiver", ignore = true)
    })
    public abstract Notifications mapNotification(NotiRequest notification);


    @Mappings({
            @Mapping(target = "receiver", expression = "java(mapReceiver(notification.getReceiver()))"),
            @Mapping(target = "createdAt", source = "notification.createdDate"),
            @Mapping(target = "updatedAt", source = "notification.updatedDate")
    })
    public abstract NotiResponse mapToResponse(Notifications notification);


    protected AccountDto mapReceiver(Account receiver) {
        if (receiver == null) return null;
        return accountMapper.toDto(receiver);
    }

}
