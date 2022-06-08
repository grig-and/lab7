package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public class Accepter {
    private NetManager net;
    private static Log log = new Log();
    private static Telegram tg = new Telegram();

    public Accepter(NetManager net) {
        this.net = net;
    }

    public void accept(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            String adr = net.accept(selectionKey).getRemoteAddress().toString();
            log.info("New client: " + adr);
            tg.sendMessage("New client: " + adr);
        }
    }
}
