package com.nyqustdata.rpc.registry;

import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class ZookeeperRegistry<T> implements Registry<T> {
    private Map<String, List<ServiceInstance<T>>> listeners = Maps.newConcurrentMap();

    private InstanceSerializer serializer = new JsonInstanceSerializer(ServerInfo.class);

    private ServiceDiscovery<T> serviceDiscovery;

    private ServiceCache<T> serviceCache;

    private String address = "localhost:2181";

    public void start() throws Exception {
        String root = "/demo/rpc";

        CuratorFramework client = CuratorFrameworkFactory.newClient(address,
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServerInfo.class)
                .client(client)
                .basePath(root)
                .serializer(serializer)
                .build();

        serviceCache = serviceDiscovery.serviceCacheBuilder()
                .name("/demoService")
                .build();

        client.blockUntilConnected();

        serviceDiscovery.start();
        serviceCache.start();


    }


    @Override
    public void registerService(ServiceInstance<T> service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    @Override
    public void unRegisterService(ServiceInstance<T> service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    @Override
    public List<ServiceInstance<T>> queryForInstance(String name) throws Exception {
        return serviceCache.getInstances().stream()
                .filter(s -> s.getName().equals(name))
                .collect(Collectors.toList());
    }
}
