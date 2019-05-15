package com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command;

import lombok.Data;

import static com.example.netty_learning.chapter8_EncodeAndDecode.protocol.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {
    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
