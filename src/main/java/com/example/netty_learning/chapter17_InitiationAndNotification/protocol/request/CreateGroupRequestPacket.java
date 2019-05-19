package com.example.netty_learning.chapter17_InitiationAndNotification.protocol.request;

import com.example.netty_learning.chapter17_InitiationAndNotification.Constent;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.Packet;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequestPacket extends Packet {

    // 群聊的用户列表
    private List<String> userIdList;

    @Override
    public Byte getCommand() {

        return Constent.CREATE_GROUP_REQUEST;
    }
}
