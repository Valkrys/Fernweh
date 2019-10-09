package com.example.fernweh;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelIndividual extends AppCompatActivity {
    String name, pdfn;
    TextView travel, traveldest, travelstd, travelendd, travelstt, travelendt;
    Button pdfindiet;
    private ShareActionProvider share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_individual);
        name = getIntent().getStringExtra("card_title");
        travel = findViewById(R.id.travel);
        traveldest = findViewById(R.id.travelwhere);
        travelstd = findViewById(R.id.travelstartdate);
        travelendd = findViewById(R.id.travelenddate);
        travelstt = findViewById(R.id.travelstarttime);
        travelendt = findViewById(R.id.travelendtime);
        pdfindiet = findViewById(R.id.travelpdfticket);
        populate();

        pdfindiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfn.equals("")) {
                    return;
                }
                File file = new File(getFilesDir(), pdfn);
                if (null != file) {
                    Uri uri = FileProvider.getUriForFile(
                            getBaseContext(),
                            getApplicationContext().getPackageName() +
                                    ".MyFileProvider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.e("PLANS", "Unable to find activity.");
                    }
                } else {
                    Log.d("GABE", "This is not working!");
                }
            }
        });

        traveldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whereto =  traveldest.getText().toString();
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query="+whereto);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        populate();
    }

    public void populate() {
        String selectQuery = "SELECT * FROM travelsTable WHERE travelsName = '" + name + "'";
        DBHelper db = new DBHelper(this);
        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                travel.setText(cursor.getString(1));
                traveldest.setText(cursor.getString(2));
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

                travelstd.setText(new_STdate);
                travelendd.setText(new_ENDdate);
                travelstt.setText(cursor.getString(5));
                travelendt.setText(cursor.getString(6));
                pdfindiet.setText(cursor.getString(7));
                pdfn = cursor.getString(7);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travels_share_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.travel_share);
        share = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        share.setShareIntent(createShareIntent());
        return true;
    }

    private Intent createShareIntent() {
        File file = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + "/" + pdfindiet.getText());
        Uri uri = FileProvider.getUriForFile(this, "com.example.fernweh.MyFileProvider", file);
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("application/pdf")
                .setStream(uri)
                .setSubject(this.getString(R.string.share_subject))
                .setText(this.getString(R.string.share_message))
                .getIntent();
        shareIntent.setData(uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      //  this.startActivity(Intent.createChooser(shareIntent, this.getString(R.string.share_chooser_title)));
        return shareIntent;
    }

    private void editPlan(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("activity", "Travel");
        intent.putExtra("travelName", travel.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.travel_share) {
            createShareIntent();
            return true;
        }
        if(id == R.id.travel_edit){
            editPlan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
