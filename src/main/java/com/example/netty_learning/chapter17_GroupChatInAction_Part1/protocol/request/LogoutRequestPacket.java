package com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.Constent;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.Packet;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return Constent.LOGOUT_REQUEST;
    }
}
