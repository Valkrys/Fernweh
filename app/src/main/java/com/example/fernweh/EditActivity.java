package com.example.fernweh;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    EditText name, loc, stdate, stend, sttime, endtime;
    Calendar tripcalendar;
    int year, month, day;
    Button editBT;
    Intent intent;
    String planNamee, travelNamee, tripNamee;
    Calendar currtime;
    String n, l, sd, ed, st, et;
    DBHelper db;
    SQLiteDatabase datb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tripcalendar = Calendar.getInstance();
        year = tripcalendar.get(Calendar.YEAR);
        month = tripcalendar.get(Calendar.MONTH);
        day = tripcalendar.get(Calendar.DAY_OF_MONTH);

        intent = this.getIntent();
        name = findViewById(R.id.editName);
        loc = findViewById(R.id.editLoc);
        stdate = findViewById(R.id.editStartDate);
        stend = findViewById(R.id.editEndDate);
        sttime = findViewById(R.id.EditStartTime);
        endtime = findViewById(R.id.editEndTime);
        editBT = findViewById(R.id.editBT);
        db = new DBHelper(this);
        datb = db.getWritableDatabase();
        showData();

        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = name.getText().toString();
                l = loc.getText().toString();
                sd = stdate.getText().toString();
                ed = stend.getText().toString();
                st = sttime.getText().toString();
                et = endtime.getText().toString();
                replacedata();
            }
        });

        sttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currtime = Calendar.getInstance();
                int hour = currtime.get(Calendar.HOUR_OF_DAY);
                int minute = currtime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        sttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currtime = Calendar.getInstance();
                int hour = currtime.get(Calendar.HOUR_OF_DAY);
                int minute = currtime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });
        stdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth)
                    {
                        Date yourDate= new Date(myyear, (mymonth), mydayOfMonth);

                        String startDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "d MMM yy");
                        startDate = dateFormatter.format(yourDate);

                        stdate.setText(new StringBuilder()
                                .append(startDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        stend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth) {
                        Date yourDate = new Date(myyear, (mymonth), mydayOfMonth);

                        String strDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "d MMM yy");
                        strDate = dateFormatter.format(yourDate);

                        stend.setText(new StringBuilder()
                                .append(strDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.editclose);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editclose:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void replacedata() {
        if (intent != null) {
            String strdata = intent.getExtras().getString("activity");
            if (strdata.equals("Travel")) {
                ContentValues cv = new ContentValues();
                cv.put("travelsName", n);
                cv.put("travelsDestination", l);
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");

                Date STdate = null;
                Date ENDdate = null;

                String pend_new = "";

                try {
                    STdate = inputFormat.parse(sd);
                    if(ed != null){
                        ENDdate = inputFormat.parse(ed);
                        pend_new = outputFormat.format(ENDdate);
                        cv.put("travelsEndDate", pend_new);}
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String pst_new = outputFormat.format(STdate);
                cv.put("travelsStartDate", pst_new);
                cv.put("travelsStartTime", st);
                if (et != null) {
                    cv.put("travelsEndTime", et);
                }
                datb.update("travelsTable", cv, "travelsName =?", new String[]{travelNamee});
                finish();
            }
            if (strdata.equals("Plan")) {
                ContentValues cv = new ContentValues();
                cv.put("plansName", n);
                cv.put("plansDestination", l);
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");

                Date STdate = null;
                Date ENDdate = null;

                String pend_new = "";

                try {
                    STdate = inputFormat.parse(sd);
                    if(ed != null){
                        ENDdate = inputFormat.parse(ed);
                        pend_new = outputFormat.format(ENDdate);
                        cv.put("plansEndDate", pend_new);}
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String pst_new = outputFormat.format(STdate);
                cv.put("plansStartDate", pst_new);

                cv.put("plansStartTime", st);
                if(et != null) {
                    cv.put("plansEndTime", et);
                }
                datb.update("plansTable", cv, "plansName = ?", new String[]{planNamee});
                finish();
            }
            if (strdata.equals("Trip")) {
                ContentValues cv = new ContentValues();
                cv.put("tripName", n);
                cv.put("tripDestination", l);
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");

                Date STdate = null;
                Date ENDdate = null;

                String pend_new = "";

                try {
                    STdate = inputFormat.parse(sd);
                    ENDdate = inputFormat.parse(ed);
                    pend_new = outputFormat.format(ENDdate);}
                catch (ParseException e) {
                    e.printStackTrace();
                }
                String pst_new = outputFormat.format(STdate);
                cv.put("tripStartDate", pst_new);
                cv.put("tripEndDate", pend_new);
                datb.update("tripsTable", cv, "tripName =?", new String[]{tripNamee});
                String sql = "UPDATE travelsTable SET travelsTripName = '"+ n +"' WHERE travelsTripName = '"+ tripNamee+"'";
                String strSQL = "UPDATE plansTable SET plansTripName = '"+ n +"' WHERE plansTripName = '"+ tripNamee+"'";
                datb.execSQL(strSQL);
                datb.execSQL(sql);
                finish();
            }
        }
    }

    public void showData() {
        if (intent != null) {
            String strdata = intent.getExtras().getString("activity");
            if (strdata.equals("Travel")) {
                travelNamee = getIntent().getStringExtra("travelName");
                String selectQuery = "SELECT * FROM travelsTable WHERE travelsName = '" + travelNamee + "'";
                DBHelper db = new DBHelper(this);
                SQLiteDatabase mydb = db.getWritableDatabase();

                Cursor cursor = mydb.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        name.setText(cursor.getString(1));
                        loc.setText(cursor.getString(2));
                        String old_STdate = cursor.getString(3);
                        String old_ENDdate = cursor.getString(4);

                        DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

                        Date STdate = null;
                        Date ENDdate = null;
                        String new_ENDdate = "";

                        try {
                            STdate = inputFormat.parse(old_STdate);
                            if(old_ENDdate != null){
                                ENDdate = inputFormat.parse(old_ENDdate);
                                new_ENDdate  = outputFormat.format(ENDdate);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String new_STdate = outputFormat.format(STdate);
                        stdate.setText(new_STdate);
                        stend.setText(new_ENDdate);
                        sttime.setText(cursor.getString(5));
                        endtime.setText(cursor.getString(6));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
            }
            if (strdata.equals("Plan")) {
                planNamee = getIntent().getStringExtra("planName");
                String selectQuery = "SELECT * FROM plansTable WHERE plansName = '" + planNamee + "'";
                DBHelper db = new DBHelper(this);
                SQLiteDatabase mydb = db.getWritableDatabase();

                Cursor cursor = mydb.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        name.setText(cursor.getString(1));
                        loc.setText(cursor.getString(2));
                        String old_STdate = cursor.getString(3);
                        String old_ENDdate = cursor.getString(4);

                        DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

                        Date STdate = null;
                        Date ENDdate = null;
                        String new_ENDdate = "";

                        try {
                            STdate = inputFormat.parse(old_STdate);
                            if(old_ENDdate != null){
                                ENDdate = inputFormat.parse(old_ENDdate);
                                new_ENDdate  = outputFormat.format(ENDdate);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String new_STdate = outputFormat.format(STdate);
                        stdate.setText(new_STdate);
                        stend.setText(new_ENDdate);
                        sttime.setText(cursor.getString(5));
                        endtime.setText(cursor.getString(6));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
            }
            if (strdata.equals("Trip")) {
                tripNamee = getIntent().getStringExtra("tripName");
                String selectQuery = "SELECT * FROM tripsTable WHERE tripName = '" + tripNamee + "'";
                DBHelper db = new DBHelper(this);
                SQLiteDatabase mydb = db.getWritableDatabase();

                Cursor cursor = mydb.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        name.setText(cursor.getString(1));
                        loc.setText(cursor.getString(2));
                        String old_STdate = cursor.getString(3);
                        String old_ENDdate = cursor.getString(4);

                        DateFormat inputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat outputFormat =   new SimpleDateFormat("dd MMM yy");

                        Date STdate = null;
                        Date ENDdate = null;
                        String new_ENDdate = "";

                        try {
                            STdate = inputFormat.parse(old_STdate);
                            ENDdate = inputFormat.parse(old_ENDdate);
                            new_ENDdate  = outputFormat.format(ENDdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String new_STdate = outputFormat.format(STdate);
                        Log.d("NEW END DATE TRIP: ", " "+new_STdate+" - "+new_ENDdate);
                        stdate.setText(new_STdate);
                        stend.setText(new_ENDdate);
                        sttime.setText("N/A");
                        endtime.setText("N/A");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
            }
        }
    }
}
