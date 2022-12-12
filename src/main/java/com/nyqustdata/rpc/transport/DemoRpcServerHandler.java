package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcServerHandler extends SimpleChannelInboundHandler<Message<Request>> {
    private static Executor executor = Executors.newFixedThreadPool(16);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                Message<Request> message) throws Exception {
        byte extraInfo = message.getHeader().getExtraInfo();

        if (Constants.isHeartBeat(extraInfo)) {
            ctx.writeAndFlush(message);
            return;
        }
        executor.execute(new InvokeRunnable(message, ctx));


    }
}
