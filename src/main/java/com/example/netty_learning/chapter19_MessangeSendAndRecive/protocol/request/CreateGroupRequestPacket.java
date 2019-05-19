package com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.Constent;
import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.Packet;
import lombok.Data;

import java.util.List;


@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {

        return Constent.CREATE_GROUP_REQUEST;
    }
}
