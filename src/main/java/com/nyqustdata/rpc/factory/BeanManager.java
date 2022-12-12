package com.nyqustdata.rpc.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class BeanManager {
    private static Map<String, Object> services = new ConcurrentHashMap<>();

    public static void register(String serviceName, Object bean) {
        services.put(serviceName, bean);
    }

    public static Object get(String serviceName) {
        return services.get(serviceName);
    }
}
