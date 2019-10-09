package com.example.fernweh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class PlansActivity extends AppCompatActivity {
    private RecyclerView planrecyclerView;
    public PlansAdapter plansadapter;
    public ArrayList<Plans> planArrayList = new ArrayList<>();
    String planlabel;
    DBHelper db = new DBHelper(this);
    RequestManager requestManager;
    CoordinatorLayout coordinatorLayout;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planlabel = getIntent().getStringExtra("label");
        setContentView(R.layout.activity_plans);

        planrecyclerView = findViewById(R.id.planrecyclerView);
        coordinatorLayout = findViewById(R.id.plan_content);
        planrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        plansadapter = new PlansAdapter(this, planArrayList);
        planrecyclerView.setAdapter(plansadapter);
        createData();
        enableSwipeToDeleteAndUndo();

        requestManager = Glide.with(this);
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        image = findViewById(R.id.plansbg);

        RequestBuilder requestBuilder = requestManager.load( R.drawable.backb);
        requestBuilder.into(image);
        requestBuilder.apply(RequestOptions.centerCropTransform()).into(image);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlansActivity.this, FormPlansActivity.class);
                intent.putExtra("trip_title", planlabel);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        createData();
    }

    public void createData() {
        String selectQuery = "SELECT * FROM plansTable WHERE plansTripName = '"+planlabel+"' ORDER BY plansStartDate ASC";

        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);
        plansadapter.clearAdapter();
        if (cursor.moveToFirst()) {
            do {
                Plans plan = new Plans(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                plansadapter.addPlan(plan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        plansadapter.notifyDataSetChanged();
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

                plansadapter.removeItem(position);

                Toast toast =Toast.makeText(getApplicationContext(),"Plan was deleted",Toast.LENGTH_LONG);
                toast.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(planrecyclerView);
    }

}
