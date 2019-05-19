package com.example.netty_learning.chapter18_MemberManagement.protocol.request;

import com.example.netty_learning.chapter18_MemberManagement.Constent;
import com.example.netty_learning.chapter18_MemberManagement.protocol.Packet;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {
    private String userName;

    private String password;

    @Override
    public Byte getCommand() {

        return Constent.LOGIN_REQUEST;
    }
}
