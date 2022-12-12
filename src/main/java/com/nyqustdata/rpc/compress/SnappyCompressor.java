package com.nyqustdata.rpc.compress;


import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class SnappyCompressor implements Compressor {
    @Override
    public byte[] compress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.compress(array);
    }

    @Override
    public byte[] unCompress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.uncompress(array);
    }
}
