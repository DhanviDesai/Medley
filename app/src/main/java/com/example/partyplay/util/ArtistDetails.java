package com.example.partyplay.util;

public class ArtistDetails {

    private String href;
    private String name;
    private String image640;
    private String image300;
    private String image64;
    private String uri;

    public ArtistDetails(String href, String name, String image640, String image300, String image64, String uri) {
        this.href = href;
        this.name = name;
        this.image640 = image640;
        this.image300 = image300;
        this.image64 = image64;
        this.uri = uri;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
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

    public String getUri() {
        return uri;
    }
}
