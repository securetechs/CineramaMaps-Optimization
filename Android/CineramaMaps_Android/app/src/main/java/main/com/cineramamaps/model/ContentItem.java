package main.com.cineramamaps.model;

public class ContentItem {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_MAP = 3;

    public int type;
    public String content;
    public boolean isExpanded = false;

    public double lat, lng;

    public ContentItem(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public ContentItem(double lat, double lng) {
        this.type = TYPE_MAP;
        this.lat = lat;
        this.lng = lng;
    }
}
