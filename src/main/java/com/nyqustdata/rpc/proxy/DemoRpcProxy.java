package com.nyqustdata.rpc.proxy;

import com.nyqustdata.rpc.Constants;
import com.nyqustdata.rpc.protocol.Header;
import com.nyqustdata.rpc.protocol.Message;
import com.nyqustdata.rpc.protocol.Request;
import com.nyqustdata.rpc.registry.Registry;
import com.nyqustdata.rpc.registry.ServerInfo;
import com.nyqustdata.rpc.transport.Connection;
import com.nyqustdata.rpc.transport.DemoRpcClient;
import com.nyqustdata.rpc.transport.NettyResponseFuture;
import io.netty.channel.ChannelFuture;
import org.apache.curator.x.discovery.ServiceInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.nyqustdata.rpc.Constants.MAGIC;
import static com.nyqustdata.rpc.Constants.VERSION_1;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoRpcProxy implements InvocationHandler {
    private String serviceName;

    public Map<Method, Header> headerCache = new ConcurrentHashMap<>();

    private Registry<ServerInfo> registry;

    public DemoRpcProxy(String serviceName, Registry<ServerInfo> registry) {
        this.serviceName = serviceName;
        this.registry = registry;
    }

    public static <T> T newInstance(Class<T> clazz, Registry<ServerInfo> registry) throws Exception {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                new DemoRpcProxy("demoService", registry));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<ServiceInstance<ServerInfo>> serviceInstances =
                registry.queryForInstance(serviceName);
        ServiceInstance<ServerInfo> serviceInstance =
                serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
        String methodName = method.getName();
        Header header = headerCache.computeIfAbsent(method, h -> new Header(MAGIC, VERSION_1));
        Message<Request> message = new Message<>(header, new Request(serviceName, methodName, args));
        return remoteCall(serviceInstance.getPayload(), message);
    }

    protected Object remoteCall(ServerInfo service, Message message) throws Exception {
        if (service == null) {
            throw new RuntimeException("get available server error");
        }
        Object result = null;
        try {
            DemoRpcClient demoRpcClient = new DemoRpcClient(service.getHost(), service.getPort());
            ChannelFuture channelFuture = demoRpcClient.connect().awaitUninterruptibly();
            Connection connection = new Connection(channelFuture, true);
            System.out.println("MAGIC:" + message.getHeader().getMagic());
            NettyResponseFuture responseFuture = connection.request(message, Constants.DEFAULT_TIMEOUT);
            result = responseFuture.getPromise().get(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
}
