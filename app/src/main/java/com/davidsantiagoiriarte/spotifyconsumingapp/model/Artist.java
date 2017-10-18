package com.davidsantiagoiriarte.spotifyconsumingapp.model;

import java.util.ArrayList;

/**
 * Created by david on 17/10/17.
 */

public class Artist {
    /**
     * Artist's identifier
     */
    private String id;
    /**
     * Artist's name
     */
    private String name;
    /**
     * Artist's image url
     */
    private String image;
    /**
     * Artist's number of followers
     */
    private int followers;
    /**
     * Artist's popularity (0 to 100)
     */
    private int popularity;


    private ArrayList<Album> albums;

    //CONSTRUCTORS

    public Artist() {
    }

    public Artist(String id, String name, String image, int followers, int popularity,ArrayList<Album> albums) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.followers = followers;
        this.popularity = popularity;
        this.albums=albums;
    }

// GETTERS AND SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
