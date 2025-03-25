package edu.gannon.smsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_SEND_SMS_ID = 15;

    EditText mEditTextPhoneNumber;
    EditText mEditTextMessage;

    String smsPhoneNumber;
    String smsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mEditTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        mEditTextMessage = findViewById(R.id.edit_text_message);
    }

    public void sendSMS(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestSMSPermission();
        }
        else
        {
            smsPhoneNumber = mEditTextPhoneNumber.getText().toString();
            smsMessage = mEditTextMessage.getText().toString();

            sendSMSMessage(smsPhoneNumber, smsMessage);

            Toast.makeText(this,
                    "SMS Message has been successfully sent",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void sendSMSMessage(String phoneNumber, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                smsPhoneNumber,
                null,
                smsMessage,
                null,
                null
        );
    }

    private void requestSMSPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {
            Toast.makeText(this,
                    "SMS Permission is required to send text messages from this app",
                    Toast.LENGTH_LONG).show();
        }

        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.SEND_SMS },
                PERMISSIONS_REQUEST_SEND_SMS_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_SEND_SMS_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                sendSMSMessage(smsPhoneNumber, smsMessage);
            } else
            {
                Toast.makeText(this, "SMS Permission Denied. Cannot send message",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}