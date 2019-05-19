package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.request;

import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;
import lombok.Data;

@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Constent.JOIN_GROUP_REQUEST;
    }
}
