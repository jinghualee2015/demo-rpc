package com.nyqustdata.rpc;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String param) {
        System.out.println("param: " + param);
        return "Hello " + param;
    }
}
