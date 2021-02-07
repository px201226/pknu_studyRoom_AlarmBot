
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static int duration = 60000;

    public static void main(String[] args) {


        try {

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Telegram());
            System.out.println(nowTIme() + " 미래로 좌석 알림봇 시작");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

//        if(args.length == 0){
//            System.out.println(String.format("%-25s %s","java -jar alarm.jar -all","열람실 및 노트북실 모두 알림"));
//            System.out.println(String.format("%-25s %s","java -jar alarm.jar -under","노트북실만 알림"));
//            System.out.println(String.format("%-25s %s","java -jar alarm.jar -ground","열람실만 알림"));
//            return;
//        }
//
//        if(args[0].equals("-all") || args[0].equals("-under") || args.equals("-ground")){
//            Bot bot = new Bot();
//            if(args.length == 2){
//                duration = Integer.parseInt(args[1]);
//            }
//            System.out.println("Duration = " + duration);
//            bot.start(args[0], duration);
//        }else{
//            System.out.println("입력이 올바르지 않습니다");
//            System.out.println(args[0]);
//        }
    }

    private static String nowTIme(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("[yyyy-mm-dd hh:MM:ss]"));
    }
}
