package com.example.netty_learning.chapter16_ClientChatInAction.protocol.response;

import com.example.netty_learning.chapter16_ClientChatInAction.Constent;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.Packet;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {
    private String userId;

    private String userName;

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return Constent.LOGIN_RESPONSE;
    }
}
