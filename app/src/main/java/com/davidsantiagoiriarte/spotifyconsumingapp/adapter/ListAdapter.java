package com.davidsantiagoiriarte.spotifyconsumingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidsantiagoiriarte.spotifyconsumingapp.R;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Album;
import com.davidsantiagoiriarte.spotifyconsumingapp.model.Artist;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by david on 17/10/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder> {


    private boolean mUseFirstLayout;
    private ArrayList<Album> albums ;
    private final Context mContext;

    final private ListAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListAdapterOnClickHandler {
        void onClick(Album album, ImageView imageView);
    }


    public ListAdapter(@NonNull Context context,ListAdapterOnClickHandler clickHandler ){
        mContext=context;
        mClickHandler=clickHandler;
        mUseFirstLayout=true;
    }


    @Override
    public ListAdapter.ListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {



        int layoutId = R.layout.list_item;


        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ListAdapterViewHolder holder, int position) {

        Album album = albums.get(position);


                holder.albumName.setText(album.getName());
                holder.albumCountries.setText(showCountries(album.getCountries()));
        if(mContext!=null)
            Picasso.with(mContext).load(album.getImage()) .placeholder(R.drawable.ic_search)
                    .error(R.drawable.ic_search).fit().into(holder.albumImage);



    }

    @Override
    public int getItemCount() {
        if(albums==null)return 0;
        return albums.size();
    }



    public void setData(ArrayList<Album> albums){
        this.albums=albums;
        notifyDataSetChanged();
    }

    public String showCountries(String [] countries){
        String result ="";
        for (int i=0;i<countries.length;i++){
            String country=countries[i];
            if(i==0)
            result +=country;
            else
                result +=", "+country;

        }
        return result;
    }

    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



         TextView albumName;
         TextView albumCountries;
         ImageView albumImage;



        public ListAdapterViewHolder(View itemView) {
            super(itemView);



                albumName =  (TextView) itemView.findViewById(R.id.album_name);
                albumImage=  (ImageView) itemView.findViewById(R.id.album_icon);
                albumCountries =  (TextView) itemView.findViewById(R.id.album_countries);






            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(albums.get(adapterPosition),albumImage);
        }
    }
}
