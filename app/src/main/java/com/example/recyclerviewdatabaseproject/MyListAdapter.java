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


/**
 * Adapter class for the Cardviews contained in the RecyclerView, each of which represents the data
 * from a single record in the database
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<MyListData> listData; // a list of all the data from the database
    private Context context; // the context of the calling activity
    private DBmain dBmain; // an object corresponding to the library database that we can call the library's methods on

    /**
     * Constructor that initializes the fields to the parameters that were passed in
     *
     * @param listData a list of all the data in the database
     * @param context the context of the calling activity
     */
    public MyListAdapter(ArrayList<MyListData> listData, Context context) {
        this.dBmain = new DBmain(context);
        this.listData = listData;
        this.context = context;
    }


    /**
     * Method that inflates the layout within a single Cardview
     *
     * @param parent the parent ViewGroup
     * @param viewType an int signifying the view type
     * @return the view holder for the list containing all the data
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.singledata, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    /**
     * Method that initializes the text and buttons within the Cardview
     *
     * @param holder the ViewHolder object necessary to override the method
     * @param position the position in the list that we are in currently
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData mylistData = listData.get(position);
        holder.textView.setText(listData.get(position).getEventTime());
        holder.infoImageView.setImageResource(listData.get(position).getInfoImvId());

        // initializing the button that the user clicks to display all the data for a particular record
        holder.infoImageView.setOnClickListener(v -> {

            // placing all of the information into a bundle that is sent to the Display activity
            Bundle bundle = new Bundle();
            bundle.putInt("id", listData.get(holder.getAdapterPosition()).getId());
            bundle.putString("eventTime", listData.get(holder.getAdapterPosition()).getEventTime());
            bundle.putString("hostId", listData.get(holder.getAdapterPosition()).getSerialNum());
            bundle.putString("userId", listData.get(holder.getAdapterPosition()).getEmpId());
            bundle.putInt("locationNbr", listData.get(holder.getAdapterPosition()).getLocation());
            bundle.putInt("routeNbr", listData.get(holder.getAdapterPosition()).getRoute());
            bundle.putInt("day", listData.get(holder.getAdapterPosition()).getDay());
            bundle.putString("logger", listData.get(holder.getAdapterPosition()).getLogger());
            bundle.putInt("eventNbr", listData.get(holder.getAdapterPosition()).getEventNum());
            bundle.putString("addtDesc", listData.get(holder.getAdapterPosition()).getEventAdditionalDesc());
            bundle.putInt("addtNbr", listData.get(holder.getAdapterPosition()).getEventAdditionalNum());

            // switching to the Display activity
            Intent intent = new Intent(context, Display.class);
            intent.putExtra("record", bundle); //sending the bundle to the Display activity
            context.startActivity(intent);

        });
        holder.editImageView.setImageResource(listData.get(position).getEditImvId());

        // initializing the button that the user clicks to edit the data for a particular record
        holder.editImageView.setOnClickListener(v -> {

            // placing all of the information into a bundle that is sent to the homescreen activity
            Bundle bundle = new Bundle();
            bundle.putInt("id", listData.get(holder.getAdapterPosition()).getId());
            bundle.putString("eventTime", listData.get(holder.getAdapterPosition()).getEventTime());
            bundle.putString("hostId", listData.get(holder.getAdapterPosition()).getSerialNum());
            bundle.putString("userId", listData.get(holder.getAdapterPosition()).getEmpId());
            bundle.putInt("locationNbr", listData.get(holder.getAdapterPosition()).getLocation());
            bundle.putInt("routeNbr", listData.get(holder.getAdapterPosition()).getRoute());
            bundle.putInt("day", listData.get(holder.getAdapterPosition()).getDay());
            bundle.putString("logger", listData.get(holder.getAdapterPosition()).getLogger());
            bundle.putInt("eventNbr", listData.get(holder.getAdapterPosition()).getEventNum());
            bundle.putString("addtDesc", listData.get(holder.getAdapterPosition()).getEventAdditionalDesc());
            bundle.putInt("addtNbr", listData.get(holder.getAdapterPosition()).getEventAdditionalNum());
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("record", bundle); //sending the bundle to MainActivity
            context.startActivity(intent);
        });
        holder.deleteImageView.setImageResource(listData.get(position).getDeleteImvId());

        // initializing the button that the user clicks to delete a particular record
        holder.deleteImageView.setOnClickListener(v -> {
            boolean result = dBmain.removeRecord(listData.get(holder.getAdapterPosition()).getId());
            if (result) { // if the delete was successful, remove the data
                remove(holder.getAdapterPosition());
            } else { // otherwise, display a toast message saying the delete was unsuccessful
                Toast.makeText(context, "There was an error deleting the record. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Method that removes a Cardview from the RecyclerView and updates the RecyclerView so the user
     * no longer sees the record they deleted
     *
     * @param position the position in the list that the deleted record was in
     */
    private void remove(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * Method that returns the size of the list containing all of the data in the database
     *
     * @return the list's size
     */
    @Override
    public int getItemCount() {
        return listData.size();
    }


    /**
     * Class that represents a ViewHolder object; it initializes the icons in the Cardview object,
     * the TextView in the Cardview displaying the event time and the Cardview itself based on the
     * corresponding id's in the XML layout file
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView infoImageView;
        public ImageView deleteImageView;
        public ImageView editImageView;
        public TextView textView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.infoImageView = (ImageView) itemView.findViewById(R.id.imv_single_data);
            this.deleteImageView = (ImageView) itemView.findViewById(R.id.imv_delete);
            this.editImageView = (ImageView) itemView.findViewById(R.id.imv_edit);
            this.textView = (TextView) itemView.findViewById(R.id.tv_single_data);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}
