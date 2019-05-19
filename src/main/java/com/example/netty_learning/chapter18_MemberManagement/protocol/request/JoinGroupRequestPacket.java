package com.example.netty_learning.chapter18_MemberManagement.protocol.request;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;


@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Constent.JOIN_GROUP_REQUEST;
    }
}
