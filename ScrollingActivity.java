package com.patrickzhong.aplux;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ScrollingActivity extends AppCompatActivity {

    static FirebaseDatabase database;
    static DatabaseReference ref;
    NotificationCompat.Builder mBuilder;

    //temporary, crummy solution to double push problem
    public static boolean runOnce = false;


    public static ScrollingActivity instance;
    public boolean requesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            ref = database.getReference();



            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (runOnce == false){
                        runOnce = true;
                        return;
                    }
                    else{
                        System.out.println(dataSnapshot);

                        runOnce = false;


                        mBuilder = new NotificationCompat.Builder(instance)
                                .setSmallIcon(R.drawable.icon)
                                .setContentTitle("Event tracker")
                                .setContentText("Events received");
                        NotificationCompat.InboxStyle inboxStyle =
                                new NotificationCompat.InboxStyle();
                        String[] events = new String[6];
                        // Sets a title for the Inbox in expanded layout
                        inboxStyle.setBigContentTitle("Event tracker details:");
                        // Moves events into the expanded layout
                        for (int i=0; i < events.length; i++) {

                            inboxStyle.addLine(events[i]);
                        }
                        // Moves the expanded layout object into the notification object.
                        mBuilder.setStyle(inboxStyle);
                        // Issue the notification here.
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        notificationManager.notify(0, mBuilder.build());







                        return;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //don't do anything
                }
            });
        }

        instance = this;

        final AppCompatActivity act = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Changing to ReportActivity...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(ScrollingActivity.this, ReportActivity.class));
            }
        });



        //Util.go(act);

        //LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout);
        Button b = (Button) findViewById(R.id.buttonOne);
        b.setText("Test");

        if(Util.lastLoc == null) {
            requesting = true;
            Util.requestLoc(this);
        }
        else
            handle(Util.lastLoc);
    }

    @TargetApi(24)
    public static void handle(final Location location){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                LinearLayout ll = (LinearLayout) instance.findViewById(R.id.linearLayout);
                ll.removeAllViews();

                List<Report> reports = new ArrayList<Report>();

                for(DataSnapshot each : snapshot.getChildren()){
                    String desc = each.child("Description").getValue(String.class);
                    String id = each.getKey();
                    String loc = each.child("Location").getValue(String.class);

                    String[] arr = loc.split(" ");
                    float[] results = new float[3];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), results);
                    double dist = results[0];
                    if(dist <= 10){
                        reports.add(new Report(id, desc, dist));
                    }
                }

                Collections.sort(reports);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                instance.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                for(Report r : reports){
                    TextView bb = new TextView(instance);
                    bb.setText("\n"+r.id+"\n"+r.desc+"\n"+"Distance: "+format(r.dist)+" meters.\n");
                    bb.setTextSize(20);
                    bb.setTextColor(Color.WHITE);
                    bb.setTypeface(null, Typeface.BOLD);
                    bb.setGravity(Gravity.CENTER);
                    bb.setWidth(width);
                    bb.setBackgroundResource(R.drawable.border);
                    ll.addView(bb);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static String format(double dist){
        return ((int) (dist * 10) / 10.0)+"";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            //Util.go(this, requestCode);
            Util.requestLoc(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
