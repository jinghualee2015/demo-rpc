package com.nyqustdata.rpc.protocol;

import java.io.Serializable;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */

public class Message<T> implements Serializable {
    private Header header;

    private T content;

    public Message(Header header, T content) {
        this.content = content;
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
