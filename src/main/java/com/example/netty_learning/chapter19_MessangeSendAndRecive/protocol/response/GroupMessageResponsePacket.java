package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.response;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.session.Session;
import lombok.Data;


@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return Constent.GROUP_MESSAGE_RESPONSE;
    }
}
