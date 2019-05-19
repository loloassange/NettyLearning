package com.example.netty_learning.chapter17_InitiationAndNotification.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);

}
