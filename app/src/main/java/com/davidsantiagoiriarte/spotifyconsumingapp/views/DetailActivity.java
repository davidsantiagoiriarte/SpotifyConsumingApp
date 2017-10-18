package com.davidsantiagoiriarte.spotifyconsumingapp.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.davidsantiagoiriarte.spotifyconsumingapp.R;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Album;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();


       if (intent != null) {
            album = new Album();
            album.setName(intent.getStringExtra("name"));
            album.setUrl(intent.getStringExtra("url"));
            album.setImage(intent.getStringExtra("image"));
           refresh();
        }


    }

    /**
     * refresh components
     */
    public void refresh(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_detail);
        ImageView image = (ImageView) findViewById(R.id.poster);
        Button button =(Button)findViewById(R.id.detail_button);

        toolbar.setTitle(album.getName());
        setSupportActionBar(toolbar);
        if( getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(album.getImage()) .placeholder(R.drawable.ic_search)
                .error(R.drawable.ic_search).fit().into(image);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Uri url = Uri.parse(album.getUrl());

       Intent intent = new Intent(Intent.ACTION_VIEW, url);

       if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
});

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
