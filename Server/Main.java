import java.io.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

import util.*;


/**
 * Main class
 */
public class Main {
    private static Log log = new Log();
    private static final ForkJoinPool recPool = new ForkJoinPool(4);
    private static final ExecutorService procPool = Executors.newFixedThreadPool(4);
    private static final ForkJoinPool sendPool = new ForkJoinPool(4);

    volatile static List<RequestTask> requests = new ArrayList<>();
    volatile static List<ResponseTask> responses = new ArrayList<>();


    public static void main(String[] args) {

        NetManager net = null;
        try {
            net = new NetManager(Integer.parseInt(args[0]));
//            net = new NetManager(6789);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            log.error("Неверный формат порта");
            return;
        }
        net.init();
        CommandManager commandManager = null;
        CollectionManager collectionManager = null;

        try {
            collectionManager = new CollectionManager();
            commandManager = new CommandManager(collectionManager);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Не передан путь");
            return;
        }


        CommandManager finalCommandManager = commandManager;

        new Thread(() -> {
            while (true) {
                while (requests.iterator().hasNext()) {
                    RequestTask rt = requests.iterator().next();
                    procPool.submit(() -> {
                        String res = finalCommandManager.runPrepared(rt.getReq());
                        Response resp = new Response(res);
                        responses.add(new ResponseTask(resp, rt.getSc()));
                    });
                    requests.remove(rt);
                }

                while (responses.iterator().hasNext()) {
                    ResponseTask respt = responses.iterator().next();
                    sendPool.submit(() -> {
                        Sender.send(respt.getResp(), respt.getSc());
                    });
                    responses.remove(respt);
                }
            }
        }).start();

        while (true) {
            try {
                net.getSelector().select();
            } catch (IOException e) {
            }
            Set keys = net.getSelector().selectedKeys();
            Iterator i = keys.iterator();
            while (i.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) i.next();
                i.remove();
                Accepter acc = new Accepter(net);
                try {
                    acc.accept(selectionKey);
                } catch (IOException e) {
                }
                Receiver receiver = new Receiver(net);

                if (selectionKey.isReadable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.channel();
                    receiver.init(sc);

                    recPool.invoke(new RecursiveAction() {
                        @Override
                        protected void compute() {
                            while (true) {
                                try {
                                    Request tmpReq = receiver.read(sc);
                                    if (tmpReq != null) {
                                        requests.add(new RequestTask(tmpReq, sc));
                                    }
                                } catch (NullPointerException | IOException e) {
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        }

    }
}


