package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcClientHandler extends SimpleChannelInboundHandler<Message<Response>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Response> msg) throws Exception {
        NettyResponseFuture responseFuture =
                Connection.IN_FLIGHT_REQUEST_MAP.remove(msg.getHeader().getMessageId());
        Response response = msg.getContent();
        if (response == null && Constants.isHeartBeat(msg.getHeader().getExtraInfo())) {
            response = new Response();
            response.setCode(Constants.HEARTBEAT_CODE);
        }
        responseFuture.getPromise().setSuccess(response.getResult());
    }
}
