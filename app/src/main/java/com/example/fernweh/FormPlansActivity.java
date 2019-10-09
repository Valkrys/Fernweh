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
import android.widget.Toast;

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

public class FormPlansActivity extends AppCompatActivity {
    Calendar plancalendar;
    int year, month, day;
    Calendar currtime;
    EditText planendtime, planstarttime, PlanName, planstart, planend, plandest;
    String pname, ploc, pstdate, penddate, psttime, pendtime,ppdf;
    DBHelper db = new DBHelper(this);
    Button bt, pdfbt;
    String triptitle;
    TextView pdflabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_plans_actitvity);
        triptitle = getIntent().getStringExtra("trip_title");
        pdflabel = findViewById(R.id.pdfnametextp);

        //getting calendar instance
        plancalendar = getInstance();
        year = plancalendar.get(Calendar.YEAR);
        month = plancalendar.get(Calendar.MONTH);
        day = plancalendar.get(Calendar.DAY_OF_MONTH);

        planstarttime = (EditText) findViewById(R.id.PlanStartTime) ;
        planstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currtime = Calendar.getInstance();
                int hour = currtime.get(Calendar.HOUR_OF_DAY);
                int minute = currtime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FormPlansActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        planstarttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        planendtime = (EditText) findViewById(R.id.PlanEndTime) ;
        planendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currtime = Calendar.getInstance();
                int hour = currtime.get(Calendar.HOUR_OF_DAY);
                int minute = currtime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FormPlansActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        planendtime.setText(selectedHour + ":" + selectedMinute);
                    }
                }
                        , hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        planstart = (EditText) findViewById(R.id.PlanStartDate);
        planstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormPlansActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth) {
                        Date yourDate = new Date(myyear, (mymonth), mydayOfMonth);

                        String strDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        strDate = dateFormatter.format(yourDate);

                        planstart.setText(new StringBuilder()
                                .append(strDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        planend = (EditText) findViewById(R.id.PlanEndDate);
        planend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormPlansActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myyear, int mymonth, int mydayOfMonth) {
                        Date yourDate = new Date(myyear, (mymonth), mydayOfMonth);

                        String strDate = null;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "dd MMM yy");
                        strDate = dateFormatter.format(yourDate);

                        planend.setText(new StringBuilder()
                                .append(strDate));
                    }
                }, year, month, day);
                //shows DatePickerDialog
                datePickerDialog.show();
            }
        });

        pdfbt = findViewById(R.id.plans_pdf_button);

        pdfbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, FILE_INTENT);
            }
        });

        bt = findViewById(R.id.plans_form_button);
        plandest = findViewById(R.id.PlanLocation);
        plandest.addTextChangedListener(new TextValidator(plandest) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mname = p.matcher(plandest.getText().toString());
                boolean bname = mname.find();

                if (bname){
                    plandest.setError("No special characters allowed");
                }
            }
        });
        PlanName = findViewById(R.id.PlanName);
        PlanName.addTextChangedListener(new TextValidator(PlanName) {
            @Override public void validate(TextView textView, String text) {
                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mname = p.matcher(PlanName.getText().toString());
                boolean bname = mname.find();

                if (bname){
                    PlanName.setError("No special characters allowed");
                }
            }
        });


        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                pname = PlanName.getText().toString();
                ploc = plandest.getText().toString();
                pstdate = planstart.getText().toString();
                penddate = planend.getText().toString();
                psttime = planstarttime.getText().toString();
                pendtime = planendtime.getText().toString();
                ppdf = pdflabel.getText().toString();

                if(TextUtils.isEmpty(pname)){
                    PlanName.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(ploc)){
                    plandest.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(pstdate)){
                    planstart.setError("All mandatory fields need to bee filled");
                }
                if(TextUtils.isEmpty(psttime)){
                    planstarttime.setError("All mandatory fields need to bee filled");
                }
                else {
                DateFormat inputFormat =  new SimpleDateFormat("dd MMM yy");
                DateFormat outputFormat =  new SimpleDateFormat("yyyy-MM-dd");

                Date STdate = null;
                Date ENDdate = null;

                String pend_new = "";

                try {
                    STdate = inputFormat.parse(pstdate);
                    if(penddate != null){
                    ENDdate = inputFormat.parse(penddate);
                    pend_new = outputFormat.format(ENDdate);}
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String pst_new = outputFormat.format(STdate);

                if(pname != null && ploc != null && pstdate != null && psttime != null) {
                    db.gimmePLANS(new String[]{
                            pname,
                            ploc,
                            pst_new,
                            pend_new,
                            psttime,
                            pendtime,
                            ppdf,
                            triptitle
                    });
                    finish();
                    Intent intent = new Intent(getApplicationContext(), PlansActivity.class);
                    startActivity(intent);
                }
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
        pdflabel.setText(fileName);
    }

}
