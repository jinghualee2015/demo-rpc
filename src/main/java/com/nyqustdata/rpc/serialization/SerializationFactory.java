package com.nyqustdata.rpc.serialization;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class SerializationFactory {
    public static Serialization get(byte extraInfo) {
        switch (extraInfo & 0x7) {
            case 0x0:
                return new HessianSerialization();
            default:
                return new HessianSerialization();
        }
    }
}
