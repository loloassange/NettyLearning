package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import lombok.Data;

@Data
public class JoinGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return Constent.JOIN_GROUP_RESPONSE;
    }
}
