package com.davidsantiagoiriarte.spotifyconsumingapp.views;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.davidsantiagoiriarte.spotifyconsumingapp.R;
import com.davidsantiagoiriarte.spotifyconsumingapp.Util.Util;
import com.davidsantiagoiriarte.spotifyconsumingapp.adapter.ListAdapter;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Album;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Artist;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        ListAdapter.ListAdapterOnClickHandler{


    private static final String CLIENT_ID = "5c51a203c7474a3ba88be5265579d300";
    private static final String REDIRECT_URI ="yourcustomprotocol://callback" ;
    private static final int REQUEST_CODE = 1337;
    private static final String ARTIST_NAME_KEY ="artist-name" ;
    private RecyclerView mRecyclerView;
    private ListAdapter listAdapter;
    private View artistView;
    private ProgressBar mLoadingIndicator;
    private EditText artistName;
    private Button searchButton;

    private Artist currentArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
artistView = (View) findViewById(R.id.artist_view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        listAdapter = new ListAdapter(this,this);
        mRecyclerView.setAdapter(listAdapter);

        artistName = (EditText) findViewById(R.id.search_artist_name);
        searchButton= (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButton.requestFocus();
                clickSearch();
            }
        });


        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARTIST_NAME_KEY)) {
                String artistnamet = savedInstanceState
                        .getString(ARTIST_NAME_KEY);
                artistName.setText(artistnamet);
                clickSearch();
            }
        }


    }
    private void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        artistView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        artistView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    /**
     * Manage click event on search button
     */

    public void clickSearch(){

        new RefreshListTask().execute(artistName.getText().toString().replace(" ","+"));

    }

    @Override
    public void onClick(Album album, ImageView imageView) {
       Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("name",album.getName());
        intent.putExtra("image",album.getImage());
        intent.putExtra("url",album.getUrl());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)imageView, "poster");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent,options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(ARTIST_NAME_KEY,artistName.getText().toString());
    }

    /**
     * show artist data in the respective components
     * @param artist
     */
    public  void showArtistData(Artist artist){
        currentArtist=artist;
        TextView name =(TextView) artistView.findViewById(R.id.artist_name);
        TextView followers = (TextView) artistView.findViewById(R.id.artist_followers);
        TextView popularity =(TextView)  artistView.findViewById(R.id.artist_popularity);
        ImageView image =(ImageView)  artistView.findViewById(R.id.artist_icon);
        Picasso.with(this).load(artist.getImage()) .placeholder(R.drawable.ic_search)
                .error(R.drawable.ic_search).fit().into(image);


        RatingBar ratingPopularity = (RatingBar)artistView.findViewById(R.id.artist_popularity_rating);
        name.setText(artist.getName());
        followers.setText(Util.decimalFormat.format(artist.getFollowers()));
        popularity.setText("("+artist.getPopularity()+"/100)");
        ratingPopularity.setRating((5*artist.getPopularity())/100);

        listAdapter.setData(artist.getAlbums());
        showData();

    }


    /**
     * To get the actual token after login
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            if (response.getType() == AuthenticationResponse.Type.TOKEN) {

                Util.actualToken=response.getAccessToken();
                 }

            }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Class wich run refresing list on background
     */
    public class  RefreshListTask extends AsyncTask<String,Void,Artist>{

        @Override
        protected void onPreExecute() {

            showLoading();
        }

        @Override
        protected Artist doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }



            Artist artist = Util.getArtist(MainActivity.this,Util.buildQuery(Util.ARTIST_TYPE,params[0]));

            artist.setAlbums(Util.getAlbums(MainActivity.this,Util.buildQuery(Util.ALBUM_TYPE,artist.getId())));
            return artist;
        }

        @Override
        protected void onPostExecute(Artist data) {

            if(data!=null){
                try {
                    showArtistData(data);

                }catch (Exception e){
                    Log.e("error", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


}
