package com.example.netty_learning.chapter12_PipelineInAction.protocol.request;

import com.example.netty_learning.chapter12_PipelineInAction.Constent;
import com.example.netty_learning.chapter12_PipelineInAction.protocol.Packet;
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
