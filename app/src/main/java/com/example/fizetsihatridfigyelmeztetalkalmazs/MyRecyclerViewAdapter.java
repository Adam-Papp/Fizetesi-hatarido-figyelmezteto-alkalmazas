package com.example.fizetsihatridfigyelmeztetalkalmazs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>
{
    private List<Szamla> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Szamla> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        String tetelNev = mData.get(position).getTetelNev();
//        holder.textViewTetelNev.setText(tetelNev);

//        String szamlaHatarido = mData.get(position).getSzamlaHatarido();
//        holder.textViewSzamlaHatarido.setText(szamlaHatarido);
//
//        String szamlaOsszeg = String.valueOf(mData.get(position).getSzamlaOsszeg());
//        holder.textViewTetelNev.setText(szamlaOsszeg);

        holder.textViewTetelNev.setText(mData.get(position).getTetelNev());
        holder.textViewSzamlaOsszeg.setText(String.valueOf(mData.get(position).getSzamlaOsszeg()));
        holder.textViewSzamlaHatarido.setText(mData.get(position).getSzamlaHatarido());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textViewTetelNev, textViewSzamlaHatarido, textViewSzamlaOsszeg;

        ViewHolder(View itemView) {
            super(itemView);
            textViewTetelNev = itemView.findViewById(R.id.textViewTetelNev);
            textViewSzamlaHatarido = itemView.findViewById(R.id.textViewSzamlaHatarido);
            textViewSzamlaOsszeg = itemView.findViewById(R.id.textViewSzamlaOsszeg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Szamla getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}