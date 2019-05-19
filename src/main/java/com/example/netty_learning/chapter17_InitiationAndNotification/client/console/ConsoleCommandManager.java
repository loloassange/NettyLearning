package com.example.netty_learning.chapter17_InitiationAndNotification.client.console;

import com.example.netty_learning.chapter17_InitiationAndNotification.util.SessionUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 管理控制台命令执行器
 *
 * 1. 我们在这个管理类中，把所有要管理的控制台指令都塞到一个 map 中。
 *
 * 2. 执行具体操作的时候，我们先获取控制台第一个输入的指令，这里以字符串代替，
 *    比较清晰（这里我们已经实现了上小节课后思考题中的登出操作），
 *    然后通过这个指令拿到对应的控制台命令执行器执行。
 *
 */
public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {

        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {

        //  获取第一个指令
        String command = scanner.next();

        if (!SessionUtil.hasLogin(channel)) {
            return;
        }

        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
    }

}
