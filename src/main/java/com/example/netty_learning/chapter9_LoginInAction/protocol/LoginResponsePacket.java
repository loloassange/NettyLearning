package com.example.netty_learning.chapter9_LoginInAction.protocol;

import com.example.netty_learning.chapter9_LoginInAction.Constent;
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
