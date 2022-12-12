package com.nyqustdata.rpc.protocol;

import java.io.Serializable;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2022/12/12
 * @description:
 */
public class Response implements Serializable {

    private int code = 0; //响应码，正常为0，非0表示失败
    private String errMsg;
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
