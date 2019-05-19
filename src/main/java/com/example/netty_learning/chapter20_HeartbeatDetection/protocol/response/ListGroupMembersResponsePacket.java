package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.response;

import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;
import com.example.netty_learning.chapter20_HeartbeatDetection.session.Session;
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
