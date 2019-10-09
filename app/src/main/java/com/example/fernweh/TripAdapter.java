package com.example.fernweh;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {
    private ArrayList<Trip> trips;
    Context context;
    DBHelper db;

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        db = new DBHelper(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(final TripHolder tripHolder, final int position) {
        context = tripHolder.itemView.getContext();
        Trip trip = trips.get(position);
        tripHolder.setDetails(trip);

        tripHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SecondActivity.class);
                TextView card_title = v.findViewById(R.id.TripNamecl);
                intent.putExtra("card_title", card_title.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }


    public TripAdapter(Context context, ArrayList<Trip> trips) {
        this.context = context;
        this.trips = trips;
    }

    public void removeItem(int position) {
        Trip trip = trips.get(position);
        trips.remove(position);
        String tripNme = trip.getTripName();
        SQLiteDatabase mydb = db.getWritableDatabase();
        String table = "tripsTable";
        String whereClause = "tripName=?";
        String[] whereArgs = new String[] { tripNme };
        mydb.delete(table, whereClause, whereArgs);
        notifyItemRemoved(position);
    }

    public void addTrip(Trip trip){
        trips.add(trip);
        notifyDataSetChanged();
    }

    public class TripHolder extends RecyclerView.ViewHolder {

        private TextView tripName, tripStartDate;

        public TripHolder(View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.TripNamecl);
            tripStartDate = itemView.findViewById(R.id.TripStartDatecl);
        }

        public void setDetails(Trip trip) {
            tripName.setText(trip.getTripName());
            String old_date = trip.getTripStartDate();

            DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

            Date date = null;

            try {
                date = inputFormat.parse(old_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String new_date = outputFormat.format(date);

            tripStartDate.setText(new_date);
        }
    }
}

