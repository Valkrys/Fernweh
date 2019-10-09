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

public class TravelsAdapter extends RecyclerView.Adapter<TravelsAdapter.TravelHolder> {
    private Context context;
    private ArrayList<Travels> travel;
    String travelNme;
    DBHelper db;

    @Override
    public TravelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        db = new DBHelper(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_travel, parent, false);
        return new TravelHolder(view);
    }

    @Override
    public void onBindViewHolder(TravelHolder travelHolder, int position) {
        context = travelHolder.itemView.getContext();
        Travels travels = travel.get(position);
        travelHolder.setDetails(travels);

        travelHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TravelIndividual.class);
                TextView card_title = v.findViewById(R.id.txtTravelName);
                intent.putExtra("card_title", card_title.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return travel.size();
    }

    public TravelsAdapter(Context context, ArrayList<Travels> travel) {
        this.context = context;
        this.travel = travel;
    }

    public void removeItem(int position) {
        Travels travels = travel.get(position);
        travel.remove(position);
        travelNme = travels.getTravelName();
        SQLiteDatabase mydb = db.getWritableDatabase();
        String table = "travelsTable";
        String whereClause = "travelsName=?";
        String[] whereArgs = new String[] { travelNme };
        mydb.delete(table, whereClause, whereArgs);
        notifyItemRemoved(position);
    }

    public void addTravel(Travels travels){
        travel.add(travels);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        travel.removeAll(travel);
    }

    public class TravelHolder extends RecyclerView.ViewHolder {

        private TextView txtTravelName, txtDate;

        public TravelHolder(View itemView) {
            super(itemView);
            txtTravelName = itemView.findViewById(R.id.txtTravelName);
            txtDate = itemView.findViewById(R.id.txtDate);
        }

        public void setDetails(Travels travels) {
            txtTravelName.setText(travels.getTravelName());
            String old_date = travels.getTravelStartDate();

            DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

            Date date = null;

            try {
                date = inputFormat.parse(old_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String new_date = outputFormat.format(date);

            txtDate.setText(new_date);
        }
    }



}