package com.example.fernweh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {
    String name;
    TextView header;
    static final String STATE_HEADER = "header";
    DBHelper db = new DBHelper(this);
    TextView datess;
    String date_string;
    String countray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        datess = findViewById(R.id.datess);

        name = getIntent().getStringExtra("card_title");
        header = findViewById(R.id.TripsHeader);
        header.setText(name);
        INEEDDATES(name);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        INEEDDATES(name);
    }

    public void INEEDDATES(String tripNAME){
        String query = "SELECT * FROM tripsTable WHERE " + DBContract.TripsEntry.COLUMN_NAME + " = " + "'" + tripNAME + "'";
        SQLiteDatabase mydb = db.getWritableDatabase();
        Cursor cursor = mydb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat =  new SimpleDateFormat("dd MMM yy");

                Date STdate = null;
                Date ENDdate = null;

                try {
                    STdate = inputFormat.parse(cursor.getString(3));
                    ENDdate = inputFormat.parse(cursor.getString(4));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String st_new = outputFormat.format(STdate);
                String end_new = outputFormat.format(ENDdate);
                date_string = st_new  +" - "+ end_new;
                datess.setText(date_string);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    public void getValue(){
        String selectQuery = "SELECT * FROM tripsTable WHERE tripName = '"+name+"'";
        DBHelper db = new DBHelper(this);
        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                countray = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_HEADER, name);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name = savedInstanceState.getString(STATE_HEADER);
        header.setText(name);
    }

    private void editPlan(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("activity", "Trip");
        intent.putExtra("tripName", name);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.edit_second);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.edit_second){
            editPlan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchMapsActivity(View view) {
        getValue();
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="+countray);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void launchTravelActivity(View view) {
        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("label", header.getText().toString());
        startActivity(intent);
    }

    public void launchPlansActivity(View view) {
        Intent intent = new Intent(this, PlansActivity.class);
        intent.putExtra("label", header.getText().toString());
        startActivity(intent);
    }
    public void launchCustomsActivity(View view) {
        Intent intent = new Intent(this, CustomsActivity.class);
        intent.putExtra("label", header.getText().toString());
        startActivity(intent);
    }
}
