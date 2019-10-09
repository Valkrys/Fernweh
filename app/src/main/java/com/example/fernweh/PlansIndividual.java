package com.example.fernweh;

import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.support.v4.content.FileProvider.getUriForFile;

public class PlansIndividual extends AppCompatActivity {
    String name, pdfname;
    TextView plan, plandest, planstd, planendd, planstt, planendt;
    Button pdfindie;
    ShareActionProvider share;
    private static Context context;
    public final static int FILE_INTENT = 1212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans_individual);
        PlansIndividual.context = getApplicationContext();
        name = getIntent().getStringExtra("card_title");
        plan = findViewById(R.id.plan);
        plandest = findViewById(R.id.planwhere);
        planstd = findViewById(R.id.planstartdate);
        planendd = findViewById(R.id.planenddate);
        planstt = findViewById(R.id.planstarttime);
        planendt = findViewById(R.id.planendtime);
        pdfindie = findViewById(R.id.planpdfticket);
        populate();

        pdfindie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfname.equals("")) {
                    return;
                }
                File file = new File(getFilesDir(), pdfname);
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

        plandest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whereto =  plandest.getText().toString();
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
        String selectQuery = "SELECT * FROM plansTable WHERE plansName = '" + name + "'";
        DBHelper db = new DBHelper(this);
        SQLiteDatabase mydb = db.getWritableDatabase();

        Cursor cursor = mydb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                plan.setText(cursor.getString(1));
                plandest.setText(cursor.getString(2));
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

                planstd.setText(new_STdate);
                planendd.setText(new_ENDdate);
                planstt.setText(cursor.getString(5));
                planendt.setText(cursor.getString(6));
                pdfindie.setText(cursor.getString(7));
                pdfname = cursor.getString(7);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plans_share_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.plans_share);
        MenuItem menuItem1 = menu.findItem(R.id.plans_edit);
        share = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
     //   share.setShareIntent(createShareIntent());
        return true;
    }

    private Intent createShareIntent() {
        File file = new File(getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + "/" + pdfindie.getText());
        Uri uri = getUriForFile(this, "com.example.fernweh_fuck_off.MyFileProvider", file);
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

    private void editPlan() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("activity", "Plan");
        intent.putExtra("planName", plan.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.plans_share) {
            createShareIntent();
            return true;
        }
        if (id == R.id.plans_edit) {
            editPlan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
