package util;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 8243L;

    public String getMsg() {
        return msg;
    }

    private String msg;


    public Response(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
