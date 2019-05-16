package com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.request;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.Constent;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.Packet;
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
