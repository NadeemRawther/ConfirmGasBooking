package com.example.confirmgasbooking;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String TAG = "Nadeem WAS";
    Button sendsms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
sendsms = (Button)findViewById(R.id.sendsms);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
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
        } else {
            // Permission already granted. Enable the message button.

        }


sendsms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        /*smsManager.sendTextMessage
                (destinationAddress, scAddress, smsMessage,
                        sentIntent, deliveryIntent);
    */
    }
});
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {











                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG,"permission failed");
                    Toast.makeText(MainActivity.this,
                            "permission failed",
                            Toast.LENGTH_SHORT).show();
                    // Disable the message button.

                }
            }
        }
    }









}
