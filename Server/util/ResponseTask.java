package util;

import java.nio.channels.SocketChannel;

public class ResponseTask {
    private Response resp;
    private SocketChannel sc;

    public ResponseTask(Response resp, SocketChannel sc) {
        this.resp = resp;
        this.sc = sc;
    }

    public Response getResp() {
        return resp;
    }

    public SocketChannel getSc() {
        return sc;
    }

    @Override
    public String toString() {
        return "ResponseTask{" +
                "resp=" + resp +
                ", sc=" + sc +
                '}';
    }
}