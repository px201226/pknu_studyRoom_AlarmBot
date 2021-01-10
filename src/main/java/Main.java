
public class Main {

    private static int duration = 60000;

    public static void main(String[] args) {

        if(args.length == 0){
            System.out.println(String.format("%-25s %s","java -jar alarm.jar -all","열람실 및 노트북실 모두 알림"));
            System.out.println(String.format("%-25s %s","java -jar alarm.jar -under","노트북실만 알림"));
            System.out.println(String.format("%-25s %s","java -jar alarm.jar -ground","열람실만 알림"));
            return;
        }

        if(args[0].equals("-all") || args[0].equals("-under") || args.equals("-ground")){
            Bot bot = new Bot();
            if(args.length == 2){
                duration = Integer.parseInt(args[1]);
            }
            System.out.println("Duration = " + duration);
            bot.start(args[0], duration);
        }else{
            System.out.println("입력이 올바르지 않습니다");
            System.out.println(args[0]);
        }
    }
}
