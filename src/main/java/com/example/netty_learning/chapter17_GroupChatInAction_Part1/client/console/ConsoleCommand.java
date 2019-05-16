package com.example.netty_learning.chapter17_GroupChatInAction_Part1.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);

}
