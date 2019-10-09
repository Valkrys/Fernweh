package com.example.fernweh;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansHolder> {
    Context context;
    private ArrayList<Plans> plans;
    String planNme;
    DBHelper db;

    @Override
    public PlansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        db = new DBHelper(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_plans, parent, false);
        return new PlansHolder(view);
    }

    @Override
    public void onBindViewHolder(PlansHolder plansHolder, int position) {
        context = plansHolder.itemView.getContext();
        Plans plan = this.plans.get(position);
        plansHolder.setDetails(plan);

        plansHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlansIndividual.class);
                TextView card_title = v.findViewById(R.id.txtPlanName);
                intent.putExtra("card_title", card_title.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public PlansAdapter(Context context, ArrayList<Plans> plans) {
        this.context = context;
        this.plans = plans;
    }
    public void removeItem(int position) {
        Plans plan = plans.get(position);
        plans.remove(position);
        planNme = plan.getplanName();
        SQLiteDatabase mydb = db.getWritableDatabase();
        String table = "plansTable";
        String whereClause = "plansName=?";
        String[] whereArgs = new String[] { planNme };
        mydb.delete(table, whereClause, whereArgs);
        notifyItemRemoved(position);
    }

    public void addPlan(Plans plan){
        plans.add(plan);
        Log.d("ADD PLANSSSSSS", "I have added this"+plans.size());
        notifyDataSetChanged();
    }
    public void clearAdapter(){
        plans.removeAll(plans);
    }

    public class PlansHolder extends RecyclerView.ViewHolder {

        private TextView txtplanDate;
        private TextView txtPlanName;

        public PlansHolder(View itemView) {
            super(itemView);
            txtPlanName = itemView.findViewById(R.id.txtPlanName);
            txtplanDate = itemView.findViewById(R.id.txtplanDate);
        }

        public void setDetails(Plans plans) {
            txtPlanName.setText(plans.getplanName());

            String old_date = plans.getPlanstartDate();

            DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

            Date date = null;

            try {
                date = inputFormat.parse(old_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String new_date = outputFormat.format(date);

            txtplanDate.setText(new_date);
        }
    }
}