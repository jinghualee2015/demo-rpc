package com.nyqustdata.rpc.registry;

import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public abstract class AbstractServiceInstanceListener <T> implements ServiceInstanceListener<T> {
    public void onFresh(ServiceInstance<T> serviceInstance, ServerInfoEvent event) {
        switch (event) {
            case ON_REGISTER:
                onRegister(serviceInstance);
                break;
            case ON_UPDATE:
                onUpdate(serviceInstance);
                break;
            case ON_REMOVE:
                onRemove(serviceInstance);
                break;
        }
    }
}

