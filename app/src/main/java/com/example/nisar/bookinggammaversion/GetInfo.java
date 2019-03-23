package com.example.nisar.bookinggammaversion;

import android.app.Activity;

import android.app.ProgressDialog;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetInfo extends AppCompatActivity  {
   // private DBHelper db;
    EditText input_source;
    EditText input_dest;
    EditText input_time;
   // private String savedID;
    Button btn_sendReq;
    private TimePicker timePicker1;
    Button btnMap;
    Button btnTime;
    ScrollView scrollview;
    ProgressBar progressBar2;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String uid;
    private static final String FIREBASE_URL="https://taxi-booking-69de8.firebaseio.com/";
    String email;
    String name,mobile,output;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
       // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_get_info);
//        Bundle p=getIntent().getExtras();
//        savedID=p.getString("id");
        scrollview=(ScrollView)findViewById(R.id.ScrollView1);
        input_source = (EditText) findViewById(R.id.input_source);
        input_dest = (EditText) findViewById(R.id.input_destination);
        input_time = (EditText) findViewById(R.id.editTextTime);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
       // progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
      //  db = new DBHelper(this);
        btnTime=(Button)findViewById(R.id.btnTime);

        btn_sendReq = (Button) findViewById(R.id.btn_sendRequest);
        btnMap=(Button)findViewById(R.id.btnMap);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    input_time.setText(timePicker1.getHour()+":"+timePicker1.getMinute());
                }
            }
        });
//        timePicker1.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent) {
//                scrollview.setEnabled(false);
//                return false;
//            }
//        });

        btn_sendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x=new String();
                String y=new String();
                x=input_dest.getText().toString();
                y=input_time.getText().toString();
                if(x.isEmpty() || y.isEmpty())
                {
                    Toast.makeText(GetInfo.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }

              else
                { //Toast.makeText(GetInfo.this, "lets go", Toast.LENGTH_SHORT).show();
                    PD = new ProgressDialog(GetInfo.this);
                    PD.setTitle("Please Wait..");
                    PD.setMessage("Loading…");
                    PD.setCancelable(false);
                    PD.show();
                    sendEmail();
                }
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(GetInfo.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this,data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
//            String attributions = (String) place.getAttributions();
//            if (attributions == null) {
//                attributions = "";
//            }
            // String arr[]=address.toString().split(",");
            //int i=0;
            String newAdd=new String();
            newAdd=address.toString().split(",",2)[1];
            // {newAdd=arr[arr.length-i]+","+newAdd;}

            //mName.setText(name);
            input_dest.setText(newAdd);
            //  mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void sendEmail() {

//        String output = db.getDataToSend(savedID);


        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL);
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
            System.out.println(uid);
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(uid);
                    name = dataSnapshot.child("name").getValue().toString();
                    mobile = dataSnapshot.child("mobile").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();
                    output = "Name:" + name + "\nemail:" + email + "\nMobile number:" + mobile + "\n";
                    PD.dismiss();
                    Log.i("Send email", "");
                    String[] TO = {"nisargbmistry1@gmail.com"};
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Taxi Booking Request");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, output + "Destination:" + input_dest.getText() + "\nTime:" + input_time.getText());

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email.", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(GetInfo.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });}
        System.out.println(uid+"3rd time");
       // PD.dismiss();
                }
        }
