import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Chair {

    private final String tag;
    private final int idx;
    private boolean isEmpty;
    private MyChangeListener<Chair> myChangeListener;
    private static Map<String, int[]> sideCharis = initMap();

    private static Map<String, int[]> initMap() {
        Map<String, int[]> map = new HashMap<>();
        map.put("노트북실", new int[]{73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89});
        map.put("1층열람실", new int[]{107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128});
        return map;
    }

    public Chair(String tag, int idx, boolean isEmpty) {
        this.tag = tag;
        this.idx = idx;
        this.isEmpty = isEmpty;
    }

    public Chair(String tag, int idx, boolean isEmpty, MyChangeListener<Chair> myChangeListener) {
        this.tag = tag;
        this.idx = idx;
        this.isEmpty = isEmpty;
        this.myChangeListener = myChangeListener;
    }

    public int getIdx() {
        return idx;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean bool) {
        isEmpty = bool;
        if (myChangeListener != null)
            myChangeListener.onChange(this);
    }

    @Override
    public String toString() {
        return String.format("%s %d번 [%s]", tag, idx, (isEmpty == true ? "O" : "X"));
    }

    public boolean isNotSideChair() {
        return !Arrays.stream(sideCharis.get(tag)).anyMatch(i -> i == getIdx());
    }


}