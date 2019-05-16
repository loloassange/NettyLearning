package com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.request;

import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.Packet;
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
