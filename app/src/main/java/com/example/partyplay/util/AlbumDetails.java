package com.example.partyplay.util;

public class AlbumDetails {

    private String href;
    private String uri;
    private String image640;
    private String image300;
    private String image64;
    private String type;
    private String name;

    public AlbumDetails(String href, String uri, String image640, String image300, String image64, String name,String type) {
        this.href = href;
        this.uri = uri;
        this.image640 = image640;
        this.image300 = image300;
        this.image64 = image64;
        this.name = name;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public String getUri() {
        return uri;
    }

    public String getImage640() {
        return image640;
    }

    public String getImage300() {
        return image300;
    }

    public String getImage64() {
        return image64;
    }

    public String getName() {
        return  name;
    }

    public String getType(){
        return type.replace(type.charAt(0),(char) (type.charAt(0)-32));
    }
}
