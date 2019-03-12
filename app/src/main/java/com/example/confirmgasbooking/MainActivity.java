package com.example.confirmgasbooking;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

Button acceptbutton,rejectbutton, reject;
TextView messageforalert;

    private static MainActivity inst;
    SmsManager smsManager = SmsManager.getDefault();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String TAG = "Nadeem WAS";
    ListView messages;
    ArrayList<String> smsMessagesList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;

    Button sendsms;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendsms = (Button)findViewById(R.id.sendsms);
        messages = (ListView) findViewById(R.id.listview);
reject = (Button)findViewById(R.id.reject);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            refreshSmsInbox();
        }




        sendsms.setOnClickListener(new View.OnClickListener() {



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            smsManager.sendTextMessage("+919207419064", null, "Accepted", null, null);
            Toast.makeText(MainActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
        }
        sendsms.setVisibility(View.INVISIBLE);
        reject.setVisibility(View.INVISIBLE);


    }
});
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendsms.setVisibility(View.INVISIBLE);
                reject.setVisibility(View.INVISIBLE);

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }





    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
        sendsms.setVisibility(View.VISIBLE);
        reject.setVisibility(View.VISIBLE);
        methForAlertBox(smsMessage);
    }
    public void refreshSmsInbox() {
        checkpermission();
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());




    }


    public void methForAlertBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = View.inflate(MainActivity.this,R.layout.alert_box_for_message,null);
        acceptbutton = (Button)mView.findViewById(R.id.acceptbutton);
        rejectbutton  = (Button)mView.findViewById(R.id.rejectbutton);
        messageforalert = (TextView)mView.findViewById(R.id.messageforalert);

        builder.setView(mView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager.sendTextMessage("+919207419064", null, "Accepted", null, null);
                Toast.makeText(MainActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
            }
        });

        rejectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        messageforalert.setText(message);



    }


public void checkpermission(){
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
        Log.d(TAG, "permission not granted");
        // Permission not yet granted. Use requestPermissions().
        // MY_PERMISSIONS_REQUEST_SEND_SMS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
    }else{
        return;
    }



}


    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        }










}
