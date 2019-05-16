package com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.response;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.Constent;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.Packet;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return Constent.LOGIN_RESPONSE;
    }

}
