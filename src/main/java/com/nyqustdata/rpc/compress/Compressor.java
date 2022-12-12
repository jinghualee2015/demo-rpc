package com.nyqustdata.rpc.compress;

import java.io.IOException;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public interface Compressor {
    byte[] compress(byte[] array) throws IOException;

    byte[] unCompress(byte[] array) throws IOException;
}
