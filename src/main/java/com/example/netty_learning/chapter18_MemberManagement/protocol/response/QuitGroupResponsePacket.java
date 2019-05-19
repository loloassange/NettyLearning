package com.example.netty_learning.chapter18_MemberManagement.protocol.response;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;

@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return Constent.QUIT_GROUP_RESPONSE;
    }
}
