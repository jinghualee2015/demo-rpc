package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.protocol.Header;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Request;
import com.nyqustdata.rpc.protocol.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class Connection implements Closeable {
    private final static AtomicLong ID_GENERATOR = new AtomicLong(0);

    public final static Map<Long, NettyResponseFuture<Response>> IN_FLIGHT_REQUEST_MAP =
            new ConcurrentHashMap<>();

    private ChannelFuture future;

    private AtomicBoolean isConnected = new AtomicBoolean();

    public Connection() {
        this.isConnected.set(false);
        this.future = null;
    }

    public Connection(ChannelFuture future, boolean isConnected) {
        this.future = future;
        this.isConnected.set(isConnected);
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected.set(isConnected);
    }

    public NettyResponseFuture<Response> request(Message<Request> message, long timeOut) {
        long messageId = ID_GENERATOR.incrementAndGet();

        message.getHeader().setMessageId(messageId);

        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(),
                timeOut, message, future.channel(), new DefaultPromise(new DefaultEventLoop()));

        IN_FLIGHT_REQUEST_MAP.put(messageId, responseFuture);

        try {
            future.channel().writeAndFlush(message);
        } catch (Exception e) {
            IN_FLIGHT_REQUEST_MAP.remove(messageId);
            throw e;
        }
        return responseFuture;
    }

    public boolean ping() {
        Header header = new Header(Constants.MAGIC, Constants.VERSION_1);
        header.setExtraInfo(Constants.HEART_EXTRA_INFO);
        Message message = new Message(header, null);
        NettyResponseFuture<Response> request = request(message, Constants.DEFAULT_TIMEOUT);
        try {
            Promise<Response> await = request.getPromise().await();
            return await.get().getCode() == Constants.HEARTBEAT_CODE;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void close() throws IOException {
        future.channel().close();
    }
}
