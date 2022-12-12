package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.factory.BeanManager;
import com.nyqustdata.rpc.protocol.Header;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Request;
import com.nyqustdata.rpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class InvokeRunnable implements Runnable {
    private Message<Request> message;
    private ChannelHandlerContext ctx;

    public InvokeRunnable(Message<Request> message, ChannelHandlerContext context) {
        this.message = message;
        this.ctx = context;
    }

    @Override
    public void run() {
        Response response = new Response();
        Object result = null;
        try {
            Request request = message.getContent();
            String serviceName = request.getServiceName();
            Object bean = BeanManager.get(serviceName);
            Method method = bean.getClass().getMethod(request.getMethodName(), request.getArgTypes());
            result = method.invoke(bean, request.getArgs());
        } catch (Exception e) {

        } finally {

        }

        Header header = message.getHeader();
        header.setExtraInfo((byte) 1);
        response.setResult(result);
        ctx.writeAndFlush(new Message<Response>(header, response));
    }
}
