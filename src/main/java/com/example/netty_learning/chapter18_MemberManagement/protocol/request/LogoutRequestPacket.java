package com.example.netty_learning.chapter18_MemberManagement.protocol.request;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return Constent.LOGOUT_REQUEST;
    }
}
