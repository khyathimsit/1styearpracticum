package com.example.wingman;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    FirebaseUser firebaseUser;
    String phone = "";
    String hash = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        phone = getIntent().getStringExtra("Number");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(HomePage.this, new String [] {Manifest.permission.SEND_SMS},
                    1);
        }
        assignHashKey();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    private void assignHashKey() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Users userdata = dataSnapshot.getValue(Users.class);
                hash = userdata.getHashkey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            Log.d("hash value", hash);

            String[] arr;
            if(text.contains(hash)){
                arr = text.split(" ");
                if(text.contains("wifi")){

                    Intent in = new Intent(HomePage.this, Wifi.class);
                    in.putExtra("value", arr[2]);
                    startActivity(in);
                }
                else if(text.contains("changemode")){
                    Intent in = new Intent(HomePage.this, changesilentmode.class);
                    in.putExtra("value", arr[2]);
                    startActivity(in);
                }
                else if(text.contains("getcontact")){
                    if(title.contains(":")){
                        String[] ar = title.split(":");
                        Log.d("getcontact", ar[1].trim() + arr[2]);

                        Intent in = new Intent(HomePage.this, loadcontacts.class);
                        in.putExtra("value", ar[1].trim() + ":" + arr[2]);
                        startActivity(in);
                    } else {
                        //Log.d("getcontact", ar[1].trim() + arr[2]);

                        Intent in = new Intent(HomePage.this, loadcontacts.class);
                        in.putExtra("value", title.trim() + ":" + arr[2]);
                        startActivity(in);
                    }

                } else if(text.contains("lockscreen")){
                    Intent in = new Intent(HomePage.this, lockScreen.class);
                    //in.putExtra("value", title.trim() + ":" + arr[2]);
                    startActivity(in);
                }
            }


        }
    };
}
