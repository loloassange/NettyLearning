package com.example.netty_learning.chapter17_InitiationAndNotification.protocol.response;

import com.example.netty_learning.chapter17_InitiationAndNotification.Constent;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.Packet;
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
