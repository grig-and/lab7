package util;

import exceptions.NetException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class NetManager {
    private int port;
    InetAddress host;
    public Socket sock;
    public OutputStream os;
    public InputStream is;


    private static NetManager net;

    public NetManager(InetAddress host, int port) {
        this.host = host;
        this.port = port;
        this.net = this;
    }

    public static NetManager get() {
        return net;
    }

    public boolean connect() {
        try {
            sock = new Socket(host, port);
            os = sock.getOutputStream();
            is = sock.getInputStream();
            IO.green("Соединение установлено");
            return true;
        } catch (ConnectException e) {
            return false;
        } catch (IllegalArgumentException e) {
            IO.error("Неверно указан порт");
            System.exit(1);
            return false;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public void send(Request req) throws NetException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(req);
        try {
            os.write(baos.toByteArray());
        } catch (SocketException e) {
            throw new NetException("Ошибка подключения: " + e.getLocalizedMessage());
        } catch (IOException e) {
            throw new NetException("Ошибка IO: " + e.getLocalizedMessage());
        }
    }

    public Response read() throws ClassNotFoundException {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024 * 128);
            is.read(buf.array());
            ByteArrayInputStream bais = new ByteArrayInputStream(buf.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response) ois.readObject();
        } catch (java.io.IOException e) {
            IO.error("Ошибка чтения");
        }
        return null;
    }


    public void close() throws IOException {
        os.close();
        is.close();
        sock.close();
    }

    public String exchange(Request res) throws NetException, IOException, ClassNotFoundException {
        send(res);
        String msg = ((Response) read()).getMsg();
        return msg;
    }

    public boolean login() {
        IO.out("Введите логин: ");
        String login = IO.nextLine().trim();

        try {
            if (net.exchange(new Request("check_user", login)).equals("true")) {
                return checkPwd(login);
            } else {
                IO.error("Нет такого юзера");
                login();
            }

        } catch (NetException | IOException | ClassNotFoundException | NullPointerException e) {
        }
        return false;
    }

    public boolean checkPwd(String login) throws NetException, IOException, ClassNotFoundException {
        String pwd = "";
        IO.out("Введите пароль: ");
        pwd = IO.nextLine().trim();

        Auth.setLogin(login);
        Auth.setPwd(pwd);

        if (net.exchange(new Request("login")).equals("true")) {
            return true;
        } else {
            IO.error("Неверный пароль");
            checkPwd(login);
        }
        return false;
    }

    public boolean register() {
        IO.out("Введите логин: ");
        String login = IO.nextLine().trim();
        String pwd;

        if (!isUserExist(login)) {
            pwd = getPwd();
            return addUser(login, pwd);
        } else {
            IO.error("Уже есть такой юзер");
            register();
        }
        return false;
    }

    private String getPwd() {
        IO.out("Введите пароль: ");
        String pwd = IO.nextLine().trim();
        if (pwd.length() >= 8) {
            return pwd;
        } else {
            IO.error("В пароле должно быть от 8 символов");
            getPwd();
        }
        return null;
    }

    public boolean addUser(String login, String pwd) {
        Auth.setLogin(login);
        Auth.setPwd(pwd);
        Request req = new Request("register");
        try {
            return net.exchange(req).equals("true");
        } catch (NetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isUserExist(String login) {
        try {
            return net.exchange(new Request("check_user", login)).equals("true");
        } catch (NetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
