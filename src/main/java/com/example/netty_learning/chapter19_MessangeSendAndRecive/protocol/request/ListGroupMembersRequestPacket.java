package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import lombok.Data;

@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Constent.LIST_GROUP_MEMBERS_REQUEST;
    }
}
