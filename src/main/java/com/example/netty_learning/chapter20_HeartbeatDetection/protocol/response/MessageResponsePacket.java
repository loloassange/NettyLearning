package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.response;

import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;
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
