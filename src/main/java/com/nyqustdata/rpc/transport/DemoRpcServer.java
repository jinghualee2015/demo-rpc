package com.nyqustdata.rpc.transport;

import com.nyqustdata.rpc.codec.DemoRpcDecoder;
import com.nyqustdata.rpc.codec.DemoRpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcServer {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap serverBootstrap;

    private Channel channel;

    int port;

    public DemoRpcServer(int port) {
        this.port = port;

        bossGroup = NettyEventLoopFactory.eventLoopGroup(1, "BossGroup");

        workerGroup = NettyEventLoopFactory.eventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, "workGroup");

        serverBootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new DemoRpcDecoder());
                        ch.pipeline().addLast("encoder", new DemoRpcEncoder());
                        ch.pipeline().addLast("business-handler", new DemoRpcServerHandler());
                    }
                });
    }

    public ChannelFuture start() throws InterruptedException {
        ChannelFuture future = serverBootstrap.bind(this.port);
        Channel channel = future.channel();
        channel.closeFuture();
        return future;
    }

    public void startAndWait() throws InterruptedException {
        try {
            channel.closeFuture().wait();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void shutdown() throws InterruptedException {
        channel.close().sync();
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().await(150000);
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().await(150000);
        }
    }
}
