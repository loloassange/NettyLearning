package com.example.netty_learning.chapter12_PipelineInAction.protocol.response;

import com.example.netty_learning.chapter12_PipelineInAction.Constent;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.MESSAGE_RESPONSE;
    }
}
