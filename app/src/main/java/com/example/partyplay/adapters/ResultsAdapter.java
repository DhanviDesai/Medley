package com.example.partyplay.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.partyplay.R;
import com.example.partyplay.util.AlbumDetails;
import com.example.partyplay.util.ArtistDetails;
import com.example.partyplay.util.PlaylistDetails;
import com.example.partyplay.util.TrackDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultHolder> {

    ItemClickListener mItemClickListener;

    ArrayList<AlbumDetails> albumDetails;
    ArrayList<TrackDetails> trackDetails;
    ArrayList<ArtistDetails> artistDetails;
    ArrayList<PlaylistDetails> playlistDetails;
    Context mContext;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setmItemClickListener(ItemClickListener m){
        mItemClickListener = m;
    }


   public ResultsAdapter(Context c,ArrayList<AlbumDetails> a, ArrayList<TrackDetails> t, ArrayList<ArtistDetails> ar, ArrayList<PlaylistDetails> p){

        albumDetails = a;
        trackDetails = t;
        artistDetails = ar;
        playlistDetails = p;
        mContext = c;

    }


    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_results,viewGroup,false);
        return new ResultHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ResultHolder resultHolder, int i) {

        if(i<10){
            Picasso.get().load(trackDetails.get(i).getImage64()).into(resultHolder.im);
            resultHolder.name.setText(trackDetails.get(i).getName());
            resultHolder.subname.setText(trackDetails.get(i).getType());
            resultHolder.options.setImageResource(R.drawable.ic_more_vert_black_24dp);

        }
        if(i>=10 && i<20){
            Picasso.get().load(albumDetails.get(i-10).getImage64()).into(resultHolder.im);
            resultHolder.name.setText(albumDetails.get(i-10).getName());
            resultHolder.subname.setText(albumDetails.get(i-10).getType());
            resultHolder.options.setImageResource(R.drawable.ic_navigate_next_black_24dp);

        }
        if(artistDetails.size()>0){

        }
        if(playlistDetails.size()>0){

        }

    }

    @Override
    public int getItemCount() {
        return trackDetails.size()+albumDetails.size()+artistDetails.size()+playlistDetails.size();
    }

    class ResultHolder extends  RecyclerView.ViewHolder{


        LinearLayout layout;
        ImageView im;
        TextView name;
        TextView subname;
        ImageView options;

         ResultHolder(@NonNull View itemView) {
            super(itemView);
            im = itemView.findViewById(R.id.coverImage);
            layout = itemView.findViewById(R.id.clickListener);
            name = itemView.findViewById(R.id.name);
            options = itemView.findViewById(R.id.options);
            subname = itemView.findViewById(R.id.subtitle);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != -1){
                        mItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }
}
