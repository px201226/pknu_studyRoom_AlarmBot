import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Telegram {
    private final static String TOKEN = "1538322780:AAFgL2WuW5yNLquVEvdDcIW16JSEOUWAaIw";
    private final static String CHAT_ID = "-1001379572061";
    private final TelegramBot bot = new TelegramBot(TOKEN);

    public  void sendMessage(String message){


        BufferedReader in = null;

        try {
            URI uri = new URIBuilder("https://api.telegram.org/bot" + TOKEN + "/sendmessage")
                    .addParameter("chat_id", CHAT_ID)
                    .addParameter("text", message)
                    .addParameter("parse_mode","HTML")
                    .addParameter("disable_notification","false").build();

            URL obj = uri.toURL();
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;

            while((line = in.readLine()) != null) { // response를 차례대로 출력
                //System.out.println(line);
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) try { in.close(); } catch(Exception e) { e.printStackTrace(); }
        }
    }
}
