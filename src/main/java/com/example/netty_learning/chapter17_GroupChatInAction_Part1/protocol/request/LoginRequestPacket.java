package com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.Constent;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.Packet;
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
