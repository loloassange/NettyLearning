package com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.response;

import com.example.netty_learning.chapter17_GroupChatInAction_Part1.Constent;
import com.example.netty_learning.chapter17_GroupChatInAction_Part1.protocol.Packet;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupResponsePacket extends Packet {
    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {

        return Constent.CREATE_GROUP_RESPONSE;
    }
}
