import exceptions.NetException;
import util.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.NoSuchElementException;

/**
 * Main class
 */
public class Main {
    public static void main(String[] args) {
        CommandManager cm = new CommandManager();
        IO io = new IO();
        NetManager net = null;
        try {
            net = new NetManager(InetAddress.getLoopbackAddress(), Integer.parseInt(args[0]));
//            net = new NetManager(InetAddress.getLoopbackAddress(), 6789);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            IO.error("Неверно указан порт");
            System.exit(1);
        }
        if (!net.connect()) {
            IO.error("Сервер не отвечает");
            while (!net.connect()) {
            }
        }

        auth(net);

        while (true) {
            Request res = null;
            try {
                String[] arr = io.promptArgs();

                res = cm.run(arr);
                if (res != null) {
                    System.out.println(net.exchange(res));
                }
                if (arr[0].equals("exit")) {
                    net.close();
                    break;
                }
            } catch (NoSuchElementException e) {
                IO.error("Нажат Ctrl+D - выхожу из программы");
                System.exit(0);
            } catch (NetException e) {
                IO.error(e.getMessage());
                resend(res, net);
            } catch (NullPointerException e) {
                IO.error("Ошибка отправки");
                resend(res, net);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private static void resend(Request res, NetManager net) {
        System.out.println("Попытка переподключения");
        while (!net.connect()) {
        }
        try {
            System.out.println("Повторная отправка");
            System.out.println(net.exchange(res));
        } catch (NetException | ClassNotFoundException | IOException | NullPointerException ex) {
            IO.error("Ошибка при повторной отправке");
        }
    }

    private static void auth(NetManager net) {
        IO.out("Регистрируете(r) или логинитесь(l)? ");
        switch (IO.nextLine().trim().toLowerCase()) {
            case "r":
                if (net.register()) {
                    IO.green("Зарегистрирован");
                }
                break;
            case "l":
                if (net.login()) {
                    IO.green("Авторизовано");
                }
                break;
            default:
                IO.error("Введите r или l");
                auth(net);
        }
    }
}
