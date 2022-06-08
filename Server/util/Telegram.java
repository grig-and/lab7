package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Telegram {
    private String token = "5313269518:AAEQ4v1RngHz_gJBE6bske_p5DQKaBBSzJ4";
    private static long id = 491117935;
    private static long lastMsgId = 0;

    public void sendMessage(String msg) {
//        if (msg == null) return;
//        try {
//            String url = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + id + "&text=" + URLEncoder.encode(msg, "UTF-8");
//            URL obj = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//            connection.setRequestMethod("GET");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public String[] getCMD() {
        try {
            String url = "https://api.telegram.org/bot" + token + "/getUpdates";

            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String resp = response.toString();
            try {
                id = Long.parseLong(match(resp, "\"id\":(.*?),\""));
                Long msgId = Long.parseLong(match(resp, "\"message_id\":(.*?),\"from"));
                if (lastMsgId == 0) {
                    lastMsgId = msgId;
                    return null;
                }
                if (msgId > lastMsgId) {
                    lastMsgId = msgId;
                    String[] res = match(resp, "\"text\":\"(.*?)\"}}").split(" ");
                    for (int i = 0; i < res.length; i++) {
                        res[i] = res[i].toLowerCase();
                    }
                    return res;
                }
            } catch (NumberFormatException e) {

            }
        } catch (IOException e) {
        }
        return null;
    }

    private String match(String str, String pt) {
        Pattern pat = Pattern.compile(pt);
        Matcher m = pat.matcher(str);
        String res = "";
        while (m.find()) {
            res = m.group(1);
        }
        return res;
    }
}
