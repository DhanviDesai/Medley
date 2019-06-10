package com.example.partyplay.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.partyplay.R;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private ItemClickListener mItemClickListener;

    public interface ItemClickListener{
        void onItemClick(int position);
    }

    public void setmItemClickListener(ItemClickListener itm){
        mItemClickListener = itm;
    }

    private Context context;
    private ArrayList<BluetoothDevice> details;

    public DeviceAdapter(Context c, ArrayList<BluetoothDevice> d){
        context = c;
        details = d;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.device,viewGroup,false);
        return new DeviceHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder deviceHolder,final int i) {
        deviceHolder.textView.setText(details.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        TextView textView;
        DeviceHolder(View v){
            super(v);
            textView = v.findViewById(R.id.deviceName);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mItemClickListener.onItemClick(position);
                }
            });

        }

    }
}
