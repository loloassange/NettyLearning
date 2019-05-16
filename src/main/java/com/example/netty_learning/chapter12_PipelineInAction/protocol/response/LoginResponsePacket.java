package com.example.netty_learning.chapter12_PipelineInAction.protocol.response;

import com.example.netty_learning.chapter12_PipelineInAction.Constent;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.Packet;
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
