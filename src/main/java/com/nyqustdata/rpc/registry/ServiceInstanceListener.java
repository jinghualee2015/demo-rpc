package com.nyqustdata.rpc.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public interface ServiceInstanceListener<T> {

    void onRegister(ServiceInstance<T> instance);

    void onRemove(ServiceInstance<T> instance);

    void onUpdate(ServiceInstance<T> instance);

    void onFresh(ServiceInstance<T> instance, ServerInfoEvent event);

    enum ServerInfoEvent {
        ON_REGISTER,
        ON_UPDATE,
        ON_REMOVE
    }
}
