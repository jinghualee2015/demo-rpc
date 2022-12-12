package com.nyqustdata.rpc.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class HessianSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput out = new HessianOutput(bos);
        out.writeObject(obj);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        HessianInput input = new HessianInput(new ByteArrayInputStream(data));

        return (T) input.readObject(clazz);
    }
}
