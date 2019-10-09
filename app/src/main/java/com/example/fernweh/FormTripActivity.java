package com.example.fernweh;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTripActivity extends AppCompatActivity {
    EditText tripstart, tripend, tripname, triploc;
    Calendar tripcalendar;
    int year, month, day;
    Button tripButton, pdfbutt;
    String name, st, loc, end, pname;
    DBHelper db = new DBHelper(this);
    Date startdate, enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_form);

        //getting calendar instance
        tripcalendar = Calendar.getInstance();
        year = tripcalendar.get(Calendar.YEAR);
        month = tripcalendar.get(Calendar.MONTH);
        day = tripcalendar.get(Calendar.DAY_OF_MONTH);
        //accessing EditText and Button
        tripstart = findViewById(R.id.TripStartDate);


        tripstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth) {
                        Date yourDate = new Date(myyear, (mymonth), mydayOfMonth);

                        String strDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        strDate = dateFormatter.format(yourDate);

                        tripstart.setText(new StringBuilder()
                                .append(strDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });
        tripstart.addTextChangedListener(new TextValidator(tripstart) {
            @Override
            public void validate(TextView textView, String text) {
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                Date STdate = null;
                try {
                    STdate = inputFormat.parse(tripstart.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String st_new = outputFormat.format(STdate);
                Date today = new Date();
                try {
                    startdate = outputFormat.parse(st_new);
                    today = outputFormat.parse(today.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(startdate.compareTo(today) < 0){
                    tripstart.setError("Invalid Date");
                }
            }
        });

        tripend = findViewById(R.id.TripEndDate);

        tripend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth) {
                        Date yourDate = new Date(myyear, (mymonth), mydayOfMonth);

                        String strDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        strDate = dateFormatter.format(yourDate);

                        tripend.setText(new StringBuilder()
                                .append(strDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        tripend.addTextChangedListener(new TextValidator(tripend) {
            @Override
            public void validate(TextView textView, String text) {
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");
                Date STdate = null;
                try {
                    STdate = inputFormat.parse(tripend.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String st_new = outputFormat.format(STdate);
                Date today = new Date();
                try {
                    enddate = outputFormat.parse(st_new);
                    today = outputFormat.parse(today.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(enddate.compareTo(today) < 0 || enddate.compareTo(startdate) < 0){
                    tripend.setError("Invalid End Date");

                }
            }
        });

        tripButton = findViewById(R.id.trip_form_button);
        triploc = findViewById(R.id.TripLocation);
        tripname = findViewById(R.id.TripName);

        tripname.addTextChangedListener(new TextValidator(tripname) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mname = p.matcher(tripname.getText().toString());
                boolean bname = mname.find();

                if (bname){
                    tripname.setError("No special characters allowed");
                }
            }
        });

        triploc.addTextChangedListener(new TextValidator(triploc) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mcountry = p.matcher(triploc.getText().toString());
                boolean bcountry = mcountry.find();

                if (bcountry){
                    triploc.setError("No special characters allowed");
                }
            }
        });


        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tripname.getText().toString();
                loc = triploc.getText().toString().toLowerCase();
                st = tripstart.getText().toString();
                end = tripend.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    tripname.setError("All fields have to be filled");
                }
                if(TextUtils.isEmpty(loc)) {
                    triploc.setError("All fields have to be filled");
                }
                if(TextUtils.isEmpty(st)){
                    tripstart.setError("All fields have to be filled");
                }
                if(TextUtils.isEmpty(end)){
                    tripend.setError("All Fields have to be filled");
                }
                else{
                    DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                    DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");

                    Date STdate = null;
                    Date ENDdate = null;

                    try {
                        STdate = inputFormat.parse(st);
                        ENDdate = inputFormat.parse(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String st_new = outputFormat.format(STdate);
                    String end_new = outputFormat.format(ENDdate);

                    db.gimmeTRIP(new String[]{
                            name,
                            loc,
                            st_new,
                            end_new,
                    });
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    createReminder(getNotification());
                }
            }
        });
    }

    public int day(){
        String date = db.getDate();
        String str[] = date.split("-");
        int day = Integer.parseInt(str[2]);
        return day;
    }

    public int year(){
        String date = db.getDate();
        String str[] = date.split("-");
        int year = Integer.parseInt(str[0]);
        return year;
    }

    public int month(){
        String date = db.getDate();
        String str[] = date.split("-");
        int month = Integer.parseInt(str[1]);
        return month-1;
    }
    private void createReminder(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        Calendar cal = Calendar.getInstance();
        cal.set(day()-7, month(), year());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private Notification getNotification() {
        String channelId = "Reminders";
        PendingIntent newEntryActivityPendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Your trip starts in 7 days!")
                .setContentText("Return to Fernweh to check all your travel details. ")
                .setTicker(getString(R.string.app_name))
                .setSmallIcon(R.drawable.splashyboi)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(newEntryActivityPendingIntent);
        return builder.build();
    }



}

