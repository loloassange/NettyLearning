package com.example.netty_learning.chapter19_MessangeSendAndRecive.client.console;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request.GroupMessageRequestPacket;
import io.netty.channel.Channel;
import java.util.Scanner;

public class SendToGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个某个群组：");

        String toGroupId = scanner.next();
        String message = scanner.next();
        channel.writeAndFlush(new GroupMessageRequestPacket(toGroupId, message));

    }
}
