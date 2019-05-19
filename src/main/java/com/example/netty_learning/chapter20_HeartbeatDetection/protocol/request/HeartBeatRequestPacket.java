package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.request;


import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Constent.HEARTBEAT_REQUEST;
    }
}
