package com.example.fernweh;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public TripAdapter adapter;
    public ArrayList<Trip> tripArrayList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    DBHelper db = new DBHelper(this);
    PendingIntent pendingIntent;
    RequestManager requestManager;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //db.clearDatabase();
        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.main_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter(this, tripArrayList);
        recyclerView.setAdapter(adapter);
        createData();
        adapter.notifyDataSetChanged();
        enableSwipeToDeleteAndUndo();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.appbar).bringToFront();

        requestManager = Glide.with(this);
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        image = findViewById(R.id.bgboi);

        RequestBuilder requestBuilder = requestManager.load( R.drawable.bg_main);
        requestBuilder.into(image);
        requestBuilder.apply(RequestOptions.centerCropTransform()).into(image);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FormTripActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void createData() {
        String selectQuery = "SELECT * FROM tripsTable ORDER BY tripStartDate ASC";

        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                adapter.addTrip(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

                adapter.removeItem(position);

                Toast toast =Toast.makeText(getApplicationContext(),"Trip was deleted",Toast.LENGTH_LONG);
                toast.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            launchSettingsActivity(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchSettingsActivity(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
