package com.example.netty_learning.chapter18_MemberManagement.client.console;

import com.example.netty_learning.chapter18_MemberManagement.protocol.request.JoinGroupRequestPacket;
import io.netty.channel.Channel;
import java.util.Scanner;

public class JoinGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();

        System.out.print("输入 groupId，加入群聊：");
        String groupId = scanner.next();

        joinGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(joinGroupRequestPacket);
    }

}
