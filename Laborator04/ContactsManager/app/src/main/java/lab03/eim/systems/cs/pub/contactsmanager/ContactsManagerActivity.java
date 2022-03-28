package lab03.eim.systems.cs.pub.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {
    private Button showAdditionalFieldsBtn;
    private Button saveBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);
        showAdditionalFieldsBtn = (Button) findViewById(R.id.show_additional_fields);
        saveBtn = (Button) findViewById(R.id.save);
        cancelBtn = (Button) findViewById(R.id.cancel);

        ButtonOnClickListener genericButtonListener = new ButtonOnClickListener();
        showAdditionalFieldsBtn.setOnClickListener(genericButtonListener);
        saveBtn.setOnClickListener(genericButtonListener);
        cancelBtn.setOnClickListener(genericButtonListener);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("lab03.eim.systems.cs.pub.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                EditText phoneEditText = (EditText) findViewById(R.id.phone_number);
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    private class ButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.show_additional_fields:
                    LinearLayout additionalFieldsLayout = (LinearLayout) findViewById(R.id.more_fields);
                    Button b = (Button) view;
                    String buttonText = b.getText().toString();

                    if (additionalFieldsLayout.getVisibility() == View.VISIBLE) {
                        additionalFieldsLayout.setVisibility(View.INVISIBLE);
                        showAdditionalFieldsBtn.setText(R.string.show_additional_fields);
                    } else if (additionalFieldsLayout.getVisibility() == View.INVISIBLE) {
                        additionalFieldsLayout.setVisibility(View.VISIBLE);
                        showAdditionalFieldsBtn.setText(R.string.hide_additional_fields);
                    }

                    break;

                case R.id.save:
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    String name = ((EditText)findViewById(R.id.name)).getText().toString();
                    String phone = ((EditText)findViewById(R.id.phone_number)).getText().toString();
                    String email = ((EditText)findViewById(R.id.email)).getText().toString();
                    String address = ((EditText)findViewById(R.id.address)).getText().toString();
                    String jobTitle = ((EditText)findViewById(R.id.job_title)).getText().toString();
                    String company = ((EditText)findViewById(R.id.company)).getText().toString();
                    String website = ((EditText)findViewById(R.id.website)).getText().toString();
                    String im = ((EditText)findViewById(R.id.im)).getText().toString();

                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }

                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }

                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }

                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }

                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }

                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }

                    ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }

                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }

                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    startActivityForResult(intent, Constant.CONTACTS_MANAGER_REQUEST_CODE);
                    break;

                case R.id.cancel:
                    Intent intentToParent = new Intent();
                    setResult(Activity.RESULT_CANCELED, intentToParent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case Constant.CONTACTS_MANAGER_REQUEST_CODE:
                Intent intentToParent = new Intent();
                setResult(resultCode, intentToParent);
                finish();
                break;
        }
    }

}