package com.example.netty_learning.chapter16_ClientChatInAction.protocol.request;

import com.example.netty_learning.chapter16_ClientChatInAction.Constent;
import com.example.netty_learning.chapter16_ClientChatInAction.protocol.Packet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {
    private String toUserId;
    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Constent.MESSAGE_REQUEST;
    }
}
