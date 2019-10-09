package com.example.fernweh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class TravelActivity extends AppCompatActivity {
    private RecyclerView travelrecyclerView;
    public TravelsAdapter travelsadapter;
    public ArrayList<Travels> travelsArrayList = new ArrayList<>();
    String travellabel;
    RequestManager requestManager;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        travellabel = getIntent().getStringExtra("label");
        setContentView(R.layout.activity_travel);

        travelrecyclerView = findViewById(R.id.travelrecyclerView);
        travelrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        travelsadapter = new TravelsAdapter(this, travelsArrayList);
        travelrecyclerView.setAdapter(travelsadapter);
        createData();
        enableSwipeToDeleteAndUndo();

        requestManager = Glide.with(this);
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        image = findViewById(R.id.travelbg);

        RequestBuilder requestBuilder = requestManager.load( R.drawable.backboo);
        requestBuilder.into(image);
        requestBuilder.apply(RequestOptions.centerCropTransform()).into(image);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TravelActivity.this, FormTravelActivity.class);
                intent.putExtra("trip_title", travellabel);
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
        String selectQuery = "SELECT * FROM travelsTable WHERE travelsTripName = '"+travellabel+"' ORDER BY travelsStartDate ASC";
        DBHelper db = new DBHelper(this);
        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);
        travelsadapter.clearAdapter();
        if (cursor.moveToFirst()) {
            do {
                Travels travels = new Travels(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                travelsadapter.addTravel(travels);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        travelsadapter.notifyDataSetChanged();
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

                travelsadapter.removeItem(position);

                Toast toast =Toast.makeText(getApplicationContext(),"Travel was deleted",Toast.LENGTH_LONG);
                toast.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(travelrecyclerView);
    }

}
