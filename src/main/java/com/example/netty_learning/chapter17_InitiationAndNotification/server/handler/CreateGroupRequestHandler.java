package com.example.netty_learning.chapter17_InitiationAndNotification.server.handler;

import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.request.CreateGroupRequestPacket;
import com.example.netty_learning.chapter17_InitiationAndNotification.protocol.response.CreateGroupResponsePacket;
import com.example.netty_learning.chapter17_InitiationAndNotification.util.IDUtil;
import com.example.netty_learning.chapter17_InitiationAndNotification.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    /**
     * 1. 首先，我们这里创建一个 ChannelGroup。
     *    这里简单介绍一下 ChannelGroup：
     *        它可以把多个 chanel 的操作聚合在一起，可以往它里面添加删除 channel，可以进行 channel 的批量读写，关闭等操作，
     *        详细的功能读者可以自行翻看这个接口的方法。这里我们一个群组其实就是一个 channel 的分组集合，使用 ChannelGroup 非常方便。
     *
     * 2. 接下来，我们遍历待加入群聊的 userId，如果存在该用户，就把对应的 channel 添加到 ChannelGroup 中，用户昵称也添加到昵称列表中。
     *
     * 3. 然后，我们创建一个创建群聊响应的对象，其中 groupId 是随机生成的，群聊创建结果一共三个字段，这里就不展开对这个类进行说明了。
     *
     * 4. 最后，我们调用 ChannelGroup 的聚合发送功能，将拉群的通知批量地发送到客户端，
     *    接着在服务端控制台打印创建群聊成功的信息，至此，服务端处理创建群聊请求的逻辑结束。
     *
     * @param ctx
     * @param createGroupRequestPacket
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {

        List<String> userIdList = createGroupRequestPacket.getUserIdList();
        List<String> userNameList = new ArrayList<>();

        // 1. 创建一个 channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 2. 筛选出待加入群聊的用户的 channel 和 userName
        for (String userId : userIdList) {

            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                // channel 加入到 channelGroup
                channelGroup.add(channel);

                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }

        }

        // 3. 创建群聊创建结果的响应
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(IDUtil.randomId());
        createGroupResponsePacket.setUserNameList(userNameList);

        // 4. 给每个客户端发送拉群通知
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());

    }
}
