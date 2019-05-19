package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.session.Session;
import lombok.Data;

import java.util.List;

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return Constent.LIST_GROUP_MEMBERS_RESPONSE;
    }
}
