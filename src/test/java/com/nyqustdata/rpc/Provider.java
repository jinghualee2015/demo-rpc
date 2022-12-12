package com.nyqustdata.rpc;

import com.nyqustdata.rpc.factory.BeanManager;
import com.nyqustdata.rpc.registry.ServerInfo;
import com.nyqustdata.rpc.registry.ZookeeperRegistry;
import com.nyqustdata.rpc.transport.DemoRpcServer;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        int port = 20880;
        BeanManager.register("demoService", new DemoServiceImpl());
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();

        ServerInfo serverInfo = new ServerInfo("127.0.0.1", port);

        discovery.registerService(ServiceInstance.<ServerInfo>builder().name("demoService")
                .payload(serverInfo)
                .build());

        DemoRpcServer rpcServer = new DemoRpcServer(port);

        rpcServer.start();
        Thread.sleep(1000000000l);


    }
}
