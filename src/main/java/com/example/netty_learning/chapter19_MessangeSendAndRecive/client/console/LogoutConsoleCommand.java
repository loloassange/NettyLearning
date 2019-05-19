package com.example.netty_learning.chapter19_MessangeSendAndRecive.client.console;

import com.example.netty_learning.chapter19_MessangeSendAndRecive.protocol.request.LogoutRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class LogoutConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        LogoutRequestPacket logoutRequestPacket = new LogoutRequestPacket();
        channel.writeAndFlush(logoutRequestPacket);
    }
}
