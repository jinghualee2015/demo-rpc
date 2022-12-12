package com.nyqustdata.rpc;

import com.nyqustdata.rpc.proxy.DemoRpcProxy;
import com.nyqustdata.rpc.registry.ServerInfo;
import com.nyqustdata.rpc.registry.ZookeeperRegistry;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();

        DemoService demoService = DemoRpcProxy.newInstance(DemoService.class, discovery);

        String result = demoService.sayHello("World");

        System.out.println(result);



    }
}
