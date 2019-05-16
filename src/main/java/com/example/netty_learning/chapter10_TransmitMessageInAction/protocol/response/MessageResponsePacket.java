package com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.response;

import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.MESSAGE_RESPONSE;
    }
}
