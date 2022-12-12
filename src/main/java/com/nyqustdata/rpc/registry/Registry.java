package com.nyqustdata.rpc.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public interface Registry<T> {
    void registerService(ServiceInstance<T> service) throws Exception;

    void unRegisterService(ServiceInstance<T> service) throws Exception;

    List<ServiceInstance<T>> queryForInstance(String name) throws Exception;
}
