package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.response;

import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;
import com.example.netty_learning.chapter20_HeartbeatDetection.session.Session;
import lombok.Data;


@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.GROUP_MESSAGE_RESPONSE;
    }
}
