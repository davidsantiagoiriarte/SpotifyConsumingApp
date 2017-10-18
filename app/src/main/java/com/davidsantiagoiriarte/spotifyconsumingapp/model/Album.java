package com.davidsantiagoiriarte.spotifyconsumingapp.model;

/**
 * Created by david on 17/10/17.
 */

public class Album {

    /**
     * Album's identifier
     */
    private String id;
    /**
     * Owner of the album
     */
    private String idArtist;
    /**
     * Album's name
     */
    private String name;
    /**
     * Album's image url
     */
    private String image;
    /**
     * list of countries in which the album is available.
     */
    private String[] countries;

    /**
     * Open album url
     */
    private String url;

    //CONSTRUCTORS

    public Album(String id, String idArtist, String name, String image,String url, String[] countries) {
        this.id = id;
        this.idArtist = idArtist;
        this.name = name;
        this.image = image;
        this.url=url;
        this.countries = countries;
    }

    public Album() {
    }
// GETTERS AND SETTERS


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(String idArtist) {
        this.idArtist = idArtist;
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

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
