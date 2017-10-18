package com.davidsantiagoiriarte.spotifyconsumingapp.Util;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.davidsantiagoiriarte.spotifyconsumingapp.model.Album;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Artist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by david on 17/10/17.
 */

public class Util {

    public static final DecimalFormat decimalFormat = new DecimalFormat();

    public static final String SPOTIFY_SEARCH_URL ="https://api.spotify.com/v1/search?q=";
    public static final String SPOTIFY_ALBUM_URL ="https://api.spotify.com/v1/artists/";

    public static final String SPOTIFY_TYPE_URL ="&type=artist";
    public static String actualToken =null;

    public static final int ARTIST_TYPE=0;
    public static final int ALBUM_TYPE=1;
    /**
     * Builds an URL from given String
     * @param urlString
     * @return
     */
    public static URL buildUrl(String urlString){
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Util", "error " + e);
        }

        Log.e("Util", "Built URI " + url);

        return url;
    }
        /**
         * Gets the JSON response from http Url request using the actual token
         * @param url
         * @return
         * @throws IOException
         */
        public static String getResponseFromHttpUrl(URL url) throws IOException {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Accept","application/json");
            connection.addRequestProperty("Content-Type","application/json");
            connection.addRequestProperty("Authorization","Bearer "+actualToken);

            try {
                InputStream in = connection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            }
            catch (Exception e){
                Log.e("getResponseFromHttpUrl",""+e.getMessage());
            }
            finally {
                connection.disconnect();
            }
            return null;
        }


    /**
     * convert given JSON to an Artist
     * @param context
     * @param jsonString
     * @return
     */
    public  static Artist getArtist(Context context, String jsonString){
        Artist  artist = new Artist();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
                Log.e("json",jsonObject.toString());
            JSONObject artistobj = jsonObject.getJSONObject("artists");
            JSONArray items = artistobj.getJSONArray("items");

            //first item
           JSONObject item1 = (JSONObject) items.get(0);

            artist.setName(item1.getString("name"));
            artist.setId(item1.getString("id"));
            artist.setPopularity(item1.getInt("popularity"));

            JSONArray arrayImages = item1.getJSONArray("images");

            artist.setImage(((JSONObject)arrayImages.get(0)).getString("url"));

            JSONObject followers = item1.getJSONObject("followers");

            artist.setFollowers(followers.getInt("total"));

        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            ex.printStackTrace();
        }

        return artist;

    }

    /**
     * convert given JSON to a List of albums
     * @param context
     * @param jsonString
     * @return
     */
    public  static ArrayList<Album> getAlbums(Context context, String jsonString){
        ArrayList<Album>  albums = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i=0;i<items.length();i++){
                JSONObject item = (JSONObject) items.get(i);
                    Album album = new Album();
                album.setId(item.getString("id"));
                album.setName(item.getString("name"));
                JSONObject external = item.getJSONObject("external_urls");
                album.setUrl(external.getString("spotify"));
                JSONArray images = item.getJSONArray("images");
                album.setImage(((JSONObject)images.get(0)).getString("url"));

                JSONArray countries = item.getJSONArray("available_markets");
                if(countries.length()<5){
                    String [] count = (String[]) item.get("available_markets");
                  album.setCountries(count);
                }else {
                    String [] count = {};
                    album.setCountries(count);
                }

                albums.add(album);
            }



        }catch (Exception ex){
            Log.d("Error",ex.getMessage());
            ex.printStackTrace();
        }

        return albums;

    }



    /**
     * Creates the url and return the correspondent JSON
     * @return
     */
    public static String buildQuery(int type,String query){

        if(actualToken!=null) {
            try {
                String url ="";
                if(type==ARTIST_TYPE) {
                    url = SPOTIFY_SEARCH_URL;
                    url += query+"" + SPOTIFY_TYPE_URL;
                }else{
                    url = SPOTIFY_ALBUM_URL;
                    url += query+"/albums" ;
                }

                return getResponseFromHttpUrl(buildUrl(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
