package lab03.eim.systems.cs.pub.phonedialer;

import lab03.eim.systems.cs.pub.phonedialer.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PhoneDialerActivity extends AppCompatActivity {
    private EditText phoneNumber;

    private class GenericButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phoneNumber.setText(phoneNumber.getText().toString() + ((Button)view).getText().toString());
        }
    }

    private class BackButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String actualPhoneNumber =  phoneNumber.getText().toString();

            if (actualPhoneNumber.length() != 0) {
                phoneNumber.setText(actualPhoneNumber.substring(0, actualPhoneNumber.length() - 1));
            }
        }
    }

    private class CallButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constant.PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
                startActivity(intent);

            }
        }
    }

    private class HangupButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private class ContactsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String number = phoneNumber.getText().toString();
            if (number.length() > 0) {
                Intent intent = new Intent("lab03.eim.systems.cs.pub.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("lab03.eim.systems.cs.pub.contactsmanager.PHONE_NUMBER_KEY", number);
                startActivityForResult(intent, Constant.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case Constant.CONTACTS_MANAGER_REQUEST_CODE:
                Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
        phoneNumber = (EditText) findViewById(R.id.phone_number);

        for (int i = 0; i < Constant.btnId.length; i++) {
            Button numberBtn = (Button) findViewById(Constant.btnId[i]);
            numberBtn.setOnClickListener(new GenericButtonClickListener());
        }

        ImageButton backBtn = (ImageButton) findViewById(R.id.backspace);
        backBtn.setOnClickListener(new BackButtonClickListener());

        ImageButton callBtn = (ImageButton) findViewById(R.id.call_button);
        callBtn.setOnClickListener(new CallButtonClickListener());

        ImageButton hangupBtn = (ImageButton) findViewById(R.id.hangup_button);
        hangupBtn.setOnClickListener(new HangupButtonClickListener());

        ImageButton contactsBtn = (ImageButton) findViewById(R.id.contact_manager);
        contactsBtn.setOnClickListener(new ContactsButtonClickListener());
    }
}