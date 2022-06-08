package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class NetManager {
    private final int port;

    public ServerSocketChannel getSsc() {
        return ssc;
    }

    private ServerSocketChannel ssc;
    private SocketChannel sc;
    private Selector selector;
    private static Log log = new Log();

    public NetManager(int port) {
        this.port = port;
    }

    public Selector getSelector() {
        return selector;
    }

    public void init() {
        try {
            InetSocketAddress addr = new InetSocketAddress(port);
            ssc = ServerSocketChannel.open();
            ssc.bind(addr);
            ssc.configureBlocking(false);
            selector = Selector.open();
            ssc.register(selector, OP_ACCEPT);
        } catch (SocketException e) {
            log.error("Порт занят, необходимо выбрать другой");
            System.exit(1);
        }catch (IllegalArgumentException e) {
            log.error("Неверно указан порт");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка при запуске сервера");
        }
    }


    public static Request readBuf(ByteBuffer buf) {
        try {
            if (buf.position() != 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(buf.array());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Request request = (Request) ois.readObject();
                return request;
            }
        } catch (ClassNotFoundException e) {
            log.error("Класс не найден");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка получения запроса");
        }
        return null;
    }

    public SocketChannel accept(SelectionKey sk) {
        try {
            sc =((ServerSocketChannel) sk.channel()).accept();
        } catch (IOException e) {
            e.printStackTrace();
            sk.cancel();
        }
        if (sc != null) {
            try {
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
                sk.cancel();
            }
        }
        return sc;
    }

    public SocketChannel forceAccept() {
        try {
            sc = ssc.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            try {
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sc;
    }

}