package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.codec.DemoRpcDecoder;
import com.nyqustdata.rpc.codec.DemoRpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcClient implements Closeable {
    protected Bootstrap bootstrap;

    protected EventLoopGroup group;

    private String host;

    private int port;

    public DemoRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        bootstrap = new Bootstrap();
        group = NettyEventLoopFactory.eventLoopGroup(Constants.DEFAULT_IO_THREADS, "client_worker");
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("encoder", new DemoRpcEncoder());
                        ch.pipeline().addLast("decoder", new DemoRpcDecoder());
                        ch.pipeline().addLast("business", new DemoRpcClientHandler());
                    }
                });
    }

    public ChannelFuture connect(){
        ChannelFuture connect = bootstrap.connect(host,port);
        connect.awaitUninterruptibly();
        return connect;
    }

    @Override
    public void close(){
        group.shutdownGracefully();
    }
}
