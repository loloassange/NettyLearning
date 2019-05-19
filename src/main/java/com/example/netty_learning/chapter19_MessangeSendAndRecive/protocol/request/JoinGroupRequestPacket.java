package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import lombok.Data;


@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Constent.JOIN_GROUP_REQUEST;
    }
}
