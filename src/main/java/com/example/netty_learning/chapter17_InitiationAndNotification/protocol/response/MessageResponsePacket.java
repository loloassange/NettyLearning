package com.example.netty_learning.chapter17_InitiationAndNotification.protocol.response;

import com.example.netty_learning.chapter17_InitiationAndNotification.Constent;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.MESSAGE_RESPONSE;
    }
}
