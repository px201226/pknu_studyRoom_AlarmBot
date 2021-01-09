public class Chair  {

    private final String tag;
    private final int idx;
    private boolean isEmpty;
    private MyChangeListener<Chair> myChangeListener;
    private int[] sideCharis;

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

    public int getIdx(){
        return idx;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setIsEmpty(boolean bool){
        isEmpty = bool;
        if(myChangeListener != null)
            myChangeListener.onChange(this);
    }

    @Override
    public String toString() {
        return String.format("%s %d번 [%s]", tag,idx,(isEmpty == true ? "O" : "X")  );
    }

    public boolean isNotSideChair(){
        return true;
    }


}