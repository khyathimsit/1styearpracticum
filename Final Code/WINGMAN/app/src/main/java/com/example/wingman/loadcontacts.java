package com.example.wingman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class loadcontacts extends AppCompatActivity {
    private String in;
    private String[] arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadcontacts);
        in = getIntent().getStringExtra("value");
        arr = in.split(":");
        String p1 = loadContacts(arr[0]);
        String p2 = loadContacts(arr[1]);
        if(p1.equals("not found")){
            p1 = arr[0].trim();
        }
        Intent in = new Intent(this, sendSMS.class);
        in.putExtra("value", p1 + ";" + arr[1] +";" + p2);
        startActivity(in);
    }

    private String loadContacts(String sam) {
        StringBuilder builder = new StringBuilder();
        ContentResolver contentResolver = getContentResolver();


        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (cursor2.moveToNext()) {
                        String phoneNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (sam.equals(name)) {
                            return phoneNumber;

                        }
                        builder.append("Contact : ").append(name).append(", PhoneNumber : ").append(phoneNumber).append("\n\n");
                    }

                    cursor2.close();
                }
            }
        }
        cursor.close();
        //listContacts.setText(builder.toString());
        return "not found";
    }
}
