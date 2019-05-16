package com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.response;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.Constent;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.MESSAGE_RESPONSE;
    }

}
