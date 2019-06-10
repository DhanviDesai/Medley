package com.example.partyplay.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.TransitionRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.partyplay.R;
import com.example.partyplay.util.TrackDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddedAdapter extends RecyclerView.Adapter<AddedAdapter.AddHolder> {

    Context mContext;
    ArrayList<TrackDetails> trackDetails;

    public AddedAdapter(Context c, ArrayList<TrackDetails> t){
        mContext =c;
        trackDetails = t;
    }

    @NonNull
    @Override
    public AddHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_results,viewGroup,false);
        return new AddHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddHolder addHolder, int i) {
        Picasso.get().load(trackDetails.get(i).getImage64()).into(addHolder.im);
        addHolder.name.setText(trackDetails.get(i).getName());
        addHolder.subtitle.setText(trackDetails.get(i).getType());
        addHolder.options.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return trackDetails.size();
    }

    class AddHolder extends RecyclerView.ViewHolder{

        ImageView im,options;
        TextView name,subtitle;

        public AddHolder(@NonNull View itemView) {
            super(itemView);
            im = itemView.findViewById(R.id.coverImage);
            options = itemView.findViewById(R.id.options);
            name = itemView.findViewById(R.id.name);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
