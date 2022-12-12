package com.nyqustdata.rpc.serialization;

import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public interface Serialization {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}
