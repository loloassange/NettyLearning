package com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.request;

import com.example.netty_learning.chapter10_TransmitMessageInAction.Constent;
import com.example.netty_learning.chapter10_TransmitMessageInAction.protocol.Packet;
import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Constent.MESSAGE_REQUEST;
    }
}
