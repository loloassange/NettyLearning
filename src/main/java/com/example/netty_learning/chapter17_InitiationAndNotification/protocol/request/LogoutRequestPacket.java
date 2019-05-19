package com.example.netty_learning.chapter17_InitiationAndNotification.protocol.request;

import com.example.netty_learning.chapter17_InitiationAndNotification.Constent;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.Packet;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return Constent.LOGOUT_REQUEST;
    }
}
