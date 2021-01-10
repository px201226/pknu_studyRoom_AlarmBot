import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Bot {

    private static final int UNDER_COUNT = 89;
    private static final int GROUND_COUNT = 128;
    private final Queue<String> messageQueue = new LinkedList<>();
    private final Telegram bot = new Telegram();
    private final MyChangeListener<Chair> myChangeListener = (chair) -> messageQueue.add(chair.toString());
    private int duration;
    public void start(String option, int duration) {

        bot.sendMessage("<pre> 봇을 시작합니다 \n " + new Date() + "</pre>");
        System.out.println("봇을 시작합니다 \n " + new Date());

        List<Chair> observeUnderChairs = initUnderChairs(UNDER_COUNT);
        List<Chair> observeGroundCharis = initGroundChairs(GROUND_COUNT);

        while (true) {

            messageQueue.clear();

            if (option.equals("-all") || option.equals("-under"))
                processChairs(observeUnderChairs, Tag.UNDER);

            if (option.equals("-all") || option.equals("-ground"))
                processChairs(observeGroundCharis, Tag.GROUND);

            if (!messageQueue.isEmpty()) {
                String s = toStringHTML(messageQueue);
                System.out.println(s);
                bot.sendMessage(s);
                this.duration = 6000;
            } else {
                this.duration = duration;
            }

            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "...");

            try {
                Thread.sleep(this.duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processChairs(List<Chair> observeChairs, Tag tag) {
        try {
            List<Chair> chairsSource = getChairsSource(tag.url, tag.value);
            compareChairs(observeChairs, chairsSource);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private String toStringHTML(Queue<String> msgs) {

        StringBuilder builder = new StringBuilder("");
        int emptyChairs = 0;
        int fillChairs = 0;

        Iterator<String> iterator = msgs.iterator();
        while (iterator.hasNext()) {
            String msg = iterator.next();
            if (msg.contains("O")) emptyChairs++;
            else fillChairs++;
            builder.append(msg + "\n");
        }
        builder.append("</pre>");

        String reportMsg = String.format("<pre>빈 좌석이 +%d개, 찬 좌석 -%d개 갱신되었습니다.\n", emptyChairs,fillChairs);

        builder.insert(0, reportMsg);
        return builder.toString();
    }

    private void compareChairs(List<Chair> observe, List<Chair> source) throws Exception {
        if (observe.size() != source.size()) {
            throw new Exception("사이즈가 다릅니다");
        }

        for (int i = 0; i < observe.size(); i++) {
            Chair target = observe.get(i);
            Chair src = source.get(i);
            if (target.getIdx() != src.getIdx()) {
                throw new Exception("이상해");
            }

            if (target.isEmpty() != src.isEmpty()) {
                target.setIsEmpty(src.isEmpty());
            }
        }
    }

    private List<Chair> initUnderChairs(int n) {
        List<Chair> chairs = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            chairs.add(new Chair("노트북실", i, false, myChangeListener));
        }
        return chairs.stream().filter(Chair::isNotSideChair).collect(Collectors.toList());
    }

    private List<Chair> initGroundChairs(int n) {
        List<Chair> chairs = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            chairs.add(new Chair("1층열람실", i, false, myChangeListener));
        }
        return chairs.stream().filter(Chair::isNotSideChair).collect(Collectors.toList());
    }

    private List<Chair> getChairsSource(String url, String tag) throws URISyntaxException {
        List<Chair> underChairs = new ArrayList<>();

        String get = httpGet(url);
        Pattern pattern = Pattern.compile("Layer([0-9]+).*bgcolor='([a-z]+)'");
        Matcher matcher = pattern.matcher(get);
        while (matcher.find()) {
            int idx = Integer.parseInt(matcher.group(1));
            boolean isEmpty = matcher.group(2).equals("red") ? false : true;
            underChairs.add(new Chair(tag, idx, isEmpty));
        }

        return underChairs.stream().filter(Chair::isNotSideChair).collect(Collectors.toList());
    }

    private String httpGet(String url) throws URISyntaxException {

        URI uri = new URI(url);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
