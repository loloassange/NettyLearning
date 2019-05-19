package com.example.netty_learning.chapter18_MemberManagement.protocol.request;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;

@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Constent.QUIT_GROUP_REQUEST;
    }
}
