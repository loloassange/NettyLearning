package com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.response;

import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.Packet;
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
