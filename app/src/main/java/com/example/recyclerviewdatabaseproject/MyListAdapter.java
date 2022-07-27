package com.example.recyclerviewdatabaseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databaselibrary.DBmain;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<MyListData> listdata;
    private Context context;
    private DBmain dBmain;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata, Context context) {
        this.dBmain = new DBmain(context);
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.singledata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata.get(position);
        holder.textView.setText(listdata.get(position).getEventTime());
        holder.infoImageView.setImageResource(listdata.get(position).getInfoImvId());
        holder.infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", listdata.get(holder.getAdapterPosition()).getId());
                bundle.putString("eventTime", listdata.get(holder.getAdapterPosition()).getEventTime());
                bundle.putString("hostId", listdata.get(holder.getAdapterPosition()).getSerialNum());
                bundle.putString("userId", listdata.get(holder.getAdapterPosition()).getEmpId());
                bundle.putInt("locationNbr", listdata.get(holder.getAdapterPosition()).getLocation());
                bundle.putInt("routeNbr", listdata.get(holder.getAdapterPosition()).getRoute());
                bundle.putInt("day", listdata.get(holder.getAdapterPosition()).getDay());
                bundle.putString("logger", listdata.get(holder.getAdapterPosition()).getLogger());
                bundle.putInt("eventNbr", listdata.get(holder.getAdapterPosition()).getEventNum());
                bundle.putString("addtDesc", listdata.get(holder.getAdapterPosition()).getEventAdditionalDesc());
                bundle.putInt("addtNbr", listdata.get(holder.getAdapterPosition()).getEventAdditionalNum());
                Intent intent = new Intent(context, Display.class);
                intent.putExtra("record", bundle); //sending the bundle to MainActivity
                context.startActivity(intent);

            }
        });
        holder.editImageView.setImageResource(listdata.get(position).getEditImvId());
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", listdata.get(holder.getAdapterPosition()).getId());
                bundle.putString("eventTime", listdata.get(holder.getAdapterPosition()).getEventTime());
                bundle.putString("hostId", listdata.get(holder.getAdapterPosition()).getSerialNum());
                bundle.putString("userId", listdata.get(holder.getAdapterPosition()).getEmpId());
                bundle.putInt("locationNbr", listdata.get(holder.getAdapterPosition()).getLocation());
                bundle.putInt("routeNbr", listdata.get(holder.getAdapterPosition()).getRoute());
                bundle.putInt("day", listdata.get(holder.getAdapterPosition()).getDay());
                bundle.putString("logger", listdata.get(holder.getAdapterPosition()).getLogger());
                bundle.putInt("eventNbr", listdata.get(holder.getAdapterPosition()).getEventNum());
                bundle.putString("addtDesc", listdata.get(holder.getAdapterPosition()).getEventAdditionalDesc());
                bundle.putInt("addtNbr", listdata.get(holder.getAdapterPosition()).getEventAdditionalNum());
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("record", bundle); //sending the bundle to MainActivity
                context.startActivity(intent);
            }
        });
        holder.deleteImageView.setImageResource(listdata.get(position).getDeleteImvId());
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = dBmain.removeRecord(listdata.get(holder.getAdapterPosition()).getId());
                if (result) {
                    Toast.makeText(context, "The entry was successfully deleted!", Toast.LENGTH_SHORT).show();
                    remove(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "There was an error deleting the record. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void remove(int position) {
        listdata.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView infoImageView;
        public ImageView deleteImageView;
        public ImageView editImageView;
        public TextView textView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.infoImageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.deleteImageView = (ImageView) itemView.findViewById(R.id.imv_delete);
            this.editImageView = (ImageView) itemView.findViewById(R.id.imv_edit);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
