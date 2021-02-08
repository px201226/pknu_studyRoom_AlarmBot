
import org.apache.http.client.utils.URIBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Telegram extends TelegramLongPollingBot {
    private final static String TOKEN = "1538322780:AAFgL2WuW5yNLquVEvdDcIW16JSEOUWAaIw";
    private final static String CHAT_ID = "-1001379572061";
    //private final static String CHAT_ID = "1503508262";
    private final static int DURATION = 60000;
    private Bot bot;

    public void sendMessage(String message) {
        try {
            execute(getSendMessage(CHAT_ID, message));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
//
//    public void sendMessage(String message) {
//
//        BufferedReader in = null;
//
//        try {
//            URI uri = new URIBuilder("https://api.telegram.org/bot" + TOKEN + "/sendmessage")
//                    .addParameter("chat_id", CHAT_ID)
//                    .addParameter("text", message)
//                    .addParameter("parse_mode", "HTML")
//                    .addParameter("disable_notification", "false").build();
//
//            URL obj = uri.toURL();
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//            String line;
//
////            while((line = in.readLine()) != null) { // response를 차례대로 출력
////                //System.out.println(line);
////            }
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (in != null) try {
//                in.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    public void onUpdateReceived(Update update) {

        if (!isValidUpdate(update)) {
            sendMessage(getHelpMessage());
            return;
        }

        System.out.printf("%s Recived Message : [%s] %s\n", nowTime(),getUserName(update), update.getMessage().getText());

        String chatId = String.valueOf(update.getMessage().getChatId());
        String message = "";
        if (update.getMessage().getText().equals("중지")) {
            if (bot != null)
                bot.setStop();
            message = getTextMessage("중지되었습니다.");
        }

        // start 명령인데 봇이 진행중이라면
        if (!(update.getMessage().getText().equals("중지")) && bot != null && bot.getRunning()) {
            message = getTextMessage("이미 작동 중 입니다.");
        }

        if (update.getMessage().getText().equals("모두")) {
            message = getTextMessage("노트북실, 1층열람실 모두 모니터링합니다.");
            bot = new Bot(DURATION, this);
            bot.setOption("-all");
            bot.start();
        }

        if (update.getMessage().getText().equals("노트북실")) {
            message = getTextMessage("노트북실을 모니터링합니다.");
            bot = new Bot(DURATION, this);
            bot.setOption("-under");
            bot.start();

        }

        if (update.getMessage().getText().equals("1층")) {
            message = getTextMessage("1층열람실을 모니터링합니다.");
            bot = new Bot(DURATION, this);
            bot.setOption("-ground");
            bot.start();
        }

        System.out.println(message);
        sendMessage(message);

        return;

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update : updates)
            onUpdateReceived(update);
    }

    @Override
    public String getBotUsername() {
        return "미래로봇";
    }

    @Override
    public String getBotToken() {
        return "1538322780:AAFgL2WuW5yNLquVEvdDcIW16JSEOUWAaIw";
    }

    private String nowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]"));
    }

    private SendMessage getSendMessage(String chatId, String msg) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(msg);
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    private String getTextMessage(String msg) {
        return nowTime() + " " + msg;
    }

    private boolean isValidUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("노트북실")) return true;
            if (update.getMessage().getText().equals("1층")) return true;
            if (update.getMessage().getText().equals("모두")) return true;
            if (update.getMessage().getText().equals("중지")) return true;
        }
        return false;
    }

    private String getHelpMessage(){
        return "노트북실,1층,모두,중지\n 중에 하나를 입력하세요.";
    }
    private String getUserName(Update update){
        return update.getMessage().getChat().getLastName() + update.getMessage().getChat().getFirstName();
    }

}
