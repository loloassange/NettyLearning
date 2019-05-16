package com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.request;

import com.example.netty_learning.chapter15_HasLoginCheckInAction.Constent;
import com.example.netty_learning.chapter15_HasLoginCheckInAction.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Constent.MESSAGE_REQUEST;
    }
}
