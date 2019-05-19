package com.example.netty_learning.chapter20_HeartbeatDetection.protocol.response;


import com.example.netty_learning.chapter20_HeartbeatDetection.Constent;
import com.example.netty_learning.chapter20_HeartbeatDetection.protocol.Packet;

public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Constent.HEARTBEAT_RESPONSE;
    }

}
