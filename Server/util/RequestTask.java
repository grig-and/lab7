package util;

import java.nio.channels.SocketChannel;

public class RequestTask {
    private Request req;


    private SocketChannel sc;

    public RequestTask(Request req, SocketChannel sc) {
        this.req = req;
        this.sc = sc;
    }

    public Request getReq() {
        return req;
    }

    public SocketChannel getSc() {
        return sc;
    }

    @Override
    public String toString() {
        return "RequestTask{" +
                "req=" + req +
                ", sc=" + sc +
                '}';
    }
}