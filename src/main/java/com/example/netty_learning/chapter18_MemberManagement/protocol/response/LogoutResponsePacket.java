package com.example.netty_learning.chapter18_MemberManagement.protocol.response;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;

@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return Constent.LOGOUT_RESPONSE;
    }
}
