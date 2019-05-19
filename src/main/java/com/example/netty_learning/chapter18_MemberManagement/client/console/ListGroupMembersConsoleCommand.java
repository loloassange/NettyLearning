package com.example.netty_learning.chapter18_MemberManagement.client.console;

import com.example.netty_learning.chapter18_MemberManagement.protocol.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;
import java.util.Scanner;

public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();

        System.out.print("输入 groupId，获取群成员列表：");
        String groupId = scanner.next();

        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
