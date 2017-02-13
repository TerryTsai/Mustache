package email.com.gmail.ttsai0509.mustache.reddit;

public class Image {

    public static class Source {
        String url;
        Integer width;
        Integer height;
    }

    public static class Resolution {
        String url;
        Integer width;
        Integer height;
    }

    public Source source;
    public Resolution[] resolutions;
    public Object variants;
    public String id;

}
