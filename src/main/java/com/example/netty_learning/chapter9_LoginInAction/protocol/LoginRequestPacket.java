package com.example.netty_learning.chapter9_LoginInAction.protocol;

import com.example.netty_learning.chapter9_LoginInAction.Constent;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Constent.LOGIN_REQUEST;
    }

}
