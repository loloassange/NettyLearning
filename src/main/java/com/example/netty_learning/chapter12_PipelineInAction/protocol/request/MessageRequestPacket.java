package com.example.netty_learning.chapter12_PipelineInAction.protocol.request;

import com.example.netty_learning.chapter12_PipelineInAction.Constent;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.Packet;
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
