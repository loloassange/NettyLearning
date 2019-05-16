package com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.request;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.Constent;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.Packet;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupRequestPacket extends Packet {

    // 群聊的用户列表
    private List<String> userIdList;

    @Override
    public Byte getCommand() {

        return Constent.CREATE_GROUP_REQUEST;
    }
}
