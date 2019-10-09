package com.example.fernweh;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.fernweh.PlansIndividual.FILE_INTENT;
import static java.util.Calendar.getInstance;

public class FormTravelActivity extends AppCompatActivity {
    Calendar calendar, travelcalendar;
    EditText travelstarttime, travelendtime, travelstart, travelend, traveldest, travelName;
    int year,month,day;
    Button butt, pdfbutt;
    String tname, tdest, tstdate, tenddate, tsttime, tendtime, pdfn;
    DBHelper db = new DBHelper(this);
    String triptitle;
    TextView pdfname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_travel);
        triptitle = getIntent().getStringExtra("trip_title");
        pdfname = findViewById(R.id.pdfnametextt);


        //getting calendar instance
        calendar = Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);

        travelstarttime = (EditText) findViewById(R.id.TravelStartTime) ;
        travelstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelcalendar = Calendar.getInstance();
                int hour = travelcalendar.get(Calendar.HOUR_OF_DAY);
                int minute = travelcalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FormTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        travelstarttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        travelendtime = (EditText) findViewById(R.id.TravelEndTime) ;
        travelendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelcalendar = Calendar.getInstance();
                int hour = travelcalendar.get(Calendar.HOUR_OF_DAY);
                int minute = travelcalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FormTravelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        travelendtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        travelstart=(EditText)findViewById(R.id.TravelStartDate);
        travelstart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormTravelActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth)
                    {
                        Date yourDate= new Date(myyear, (mymonth), mydayOfMonth);

                        String startDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        startDate = dateFormatter.format(yourDate);

                        travelstart.setText(new StringBuilder()
                                .append(startDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        travelend=(EditText)findViewById(R.id.TravelEndDate);

        travelend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormTravelActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth)
                    {
                        Date yourDate= new Date(myyear, (mymonth), mydayOfMonth);

                        String endDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        endDate = dateFormatter.format(yourDate);

                        travelend.setText(new StringBuilder()
                                .append(endDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        pdfbutt = findViewById(R.id.travel_pdf_button);

        pdfbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 1212);
            }
        });

        butt = findViewById(R.id.form_travel_button);
        traveldest = findViewById(R.id.TravelLocation);
        traveldest.addTextChangedListener(new TextValidator(traveldest) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mcountry = p.matcher(traveldest.getText().toString());
                boolean bcountry = mcountry.find();

                if (bcountry){
                    traveldest.setError("No special characters allowed");
                }
            }
        });
        travelName = findViewById(R.id.TravelName);
        travelName.addTextChangedListener(new TextValidator(travelName) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mcountry = p.matcher(travelName.getText().toString());
                boolean bcountry = mcountry.find();

                if (bcountry){
                    travelName.setError("No special characters allowed");
                }
            }
        });


        butt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                tname = travelName.getText().toString();
                tdest = traveldest.getText().toString();
                tstdate = travelstart.getText().toString();
                tenddate = travelend.getText().toString();
                tsttime = travelstarttime.getText().toString();
                tendtime = travelendtime.getText().toString();
                pdfn = pdfname.getText().toString();

                if(TextUtils.isEmpty(tname)){
                    travelName.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(tdest)){
                    traveldest.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(tstdate)){
                    travelstart.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(tsttime)){
                    travelstarttime.setError("All mandatory fields need to bee filled");
                }
                else {
                    DateFormat inputFormat = new SimpleDateFormat("dd MMM yy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date STdate = null;
                    Date ENDdate = null;
                    String tend_new = "";

                    try {
                        STdate = inputFormat.parse(tstdate);
                        if (tenddate != null) {
                            ENDdate = inputFormat.parse(tenddate);
                            tend_new = outputFormat.format(ENDdate);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String tst_new = outputFormat.format(STdate);

                    db.gimmeTRAVELS(new String[]{
                            tname,
                            tdest,
                            tst_new,
                            tend_new,
                            tsttime,
                            tendtime,
                            pdfn,
                            triptitle
                    });
                    finish();
                    Intent intent = new Intent(getApplicationContext(), TravelActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_INTENT || resultCode != RESULT_OK) {
            return;
        }
        Uri uri = intent.getData();

        String fileName = "";
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }

        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            File target = new File(getFilesDir(), fileName);
            is = new BufferedInputStream(getContentResolver().openInputStream(uri));
            os = new BufferedOutputStream(new FileOutputStream(target, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                os.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pdfname.setText(fileName);
    }
}
