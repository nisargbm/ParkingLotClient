package com.example.nisar.bookinggammaversion;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayOutput extends AppCompatActivity {
    TextView name,email,password,mobile,address;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String uid;
    private static final String FIREBASE_URL="https://taxi-booking-69de8.firebaseio.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_display_output);

        name=(TextView)findViewById(R.id.name);
        email  =(TextView)findViewById(R.id.email);
        password =(TextView)findViewById(R.id.password);
        mobile=(TextView)findViewById(R.id.mobile);
        address=(TextView)findViewById(R.id.address);
        mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL);

        if(auth.getCurrentUser()!=null)
               {
            uid=auth.getCurrentUser().getUid();
            System.out.println(uid);
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(uid);
                    String nameloc,addloc,mobloc,passloc,emailloc;
                    nameloc=dataSnapshot.child("name").getValue().toString();
                    addloc=dataSnapshot.child("mobile").getValue().toString();
                    mobloc=dataSnapshot.child("address").getValue().toString();
                    passloc=dataSnapshot.child("password").getValue().toString();
                    emailloc=dataSnapshot.child("email").getValue().toString();
                    //System.out.println(uid);
                    name.setText(nameloc);
                    email.setText(emailloc);
                    password.setText(passloc);
                    mobile.setText(mobloc);
                    address.setText(addloc);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//        mDatabase.child("users").child(uid).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        }

    }
}
