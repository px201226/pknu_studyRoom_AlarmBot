public enum Tag {

    UNDER("under","노트북실","http://210.125.122.79/webseat/roomview5.asp?room_no=6"),
    GROUND("ground","1층열람실","http://210.125.122.79/webseat/roomview5.asp?room_no=7");

    String key;
    String value;
    String url;

    Tag(String key, String value, String url) {
        this.key = key;
        this.value = value;
        this.url = url;
    }
}
