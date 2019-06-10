package com.example.partyplay.util;

import java.io.Serializable;

public class TrackDetails implements Serializable {

    private String name;
    private String image640;
    private String image300;
    private String image64;
    private String album_href;
    private String album_name;
    private String artist_name;
    private String artist_href;
    private String track_uri;
    private String album_uri;
    private String artist_uri;
    private String type;
    private int duration;

    public TrackDetails(String name, String image640, String image300,
                        String image64, String album_href, String album_name,
                        String artist_name, String artist_href, String track_uri,
                        String album_uri, String artist_uri, int duration,String type) {
        this.name = name;
        this.image640 = image640;
        this.image300 = image300;
        this.image64 = image64;
        this.album_href = album_href;
        this.album_name = album_name;
        this.artist_name = artist_name;
        this.artist_href = artist_href;
        this.track_uri = track_uri;
        this.album_uri = album_uri;
        this.artist_uri = artist_uri;
        this.duration = duration;
        this.type = type;
    }

    public String getName() {
        return  name;
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

    public String getAlbum_href() {
        return album_href;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getArtist_href() {
        return artist_href;
    }

    public String getTrack_uri() {
        return track_uri;
    }

    public String getAlbum_uri() {
        return album_uri;
    }

    public String getArtist_uri() {
        return artist_uri;
    }

    public String getType() {
        return type.replace(type.charAt(0),(char) (type.charAt(0)-32));
    }

    public double getDuration() {
        return duration/6000.0;
    }
}
